/** Optional real-browser smoke test. Node 22+ and a Chromium binary; no npm packages.
 * Usage: node scripts/check_starter.mjs /path/to/chrome /path/to/evidence-directory
 * Tests the bundled starter, NOT arbitrary lessons or pedagogical quality.
 */
import assert from 'node:assert/strict';
import { spawn } from 'node:child_process';
import { mkdir, mkdtemp, readFile, writeFile } from 'node:fs/promises';
import path from 'node:path';
import { fileURLToPath, pathToFileURL } from 'node:url';

const [chromePath, output] = process.argv.slice(2);
if (!chromePath || !output) {
  console.error('Usage: node check_starter.mjs <chromium-binary> <evidence-directory>');
  process.exit(2);
}
await mkdir(output, { recursive: true });
const profile = await mkdtemp(path.join(path.resolve(output), 'chrome-profile-'));
const chrome = spawn(chromePath, ['--headless=new', '--remote-debugging-port=0',
  '--remote-debugging-address=127.0.0.1', `--user-data-dir=${profile}`,
  '--no-first-run', '--no-default-browser-check', '--disable-background-networking', 'about:blank'],
{ stdio: ['ignore', 'ignore', 'pipe'] });
let socket;
const pending = new Map();
const waiters = new Map();
const exceptions = [];
const consoleErrors = [];
const requests = [];
const checks = [];
let serial = 0;
let session;

try {
  const endpoint = await new Promise((resolve, reject) => {
    let stderr = '';
    const timer = setTimeout(() => reject(new Error('Chromium did not start within 15s')), 15000);
    chrome.once('error', error => { clearTimeout(timer); reject(error); });
    chrome.once('exit', code => { clearTimeout(timer); reject(new Error(`Chromium exited: ${code}`)); });
    chrome.stderr.on('data', chunk => {
      stderr += chunk;
      const match = /DevTools listening on (ws:\/\/[^\s]+)/.exec(stderr);
      if (match) { clearTimeout(timer); resolve(match[1]); }
    });
  });
  socket = new WebSocket(endpoint);
  await new Promise((resolve, reject) => {
    socket.addEventListener('open', resolve, { once: true });
    socket.addEventListener('error', reject, { once: true });
  });
  socket.addEventListener('message', event => {
    const message = JSON.parse(event.data);
    if (message.id) {
      const operation = pending.get(message.id);
      if (!operation) return;
      pending.delete(message.id);
      clearTimeout(operation.timer);
      if (message.error) operation.reject(new Error(JSON.stringify(message.error)));
      else operation.resolve(message.result);
    } else {
      if (message.method === 'Runtime.exceptionThrown') exceptions.push(message.params.exceptionDetails.text);
      if (message.method === 'Runtime.consoleAPICalled' && message.params.type === 'error') {
        consoleErrors.push(message.params.args.map(arg => arg.value || arg.description).join(' '));
      }
      if (message.method === 'Network.requestWillBeSent') requests.push(message.params.request.url);
      const key = `${message.sessionId}:${message.method}`;
      const waiter = waiters.get(key);
      if (waiter) { waiters.delete(key); waiter(message.params); }
    }
  });
  function send(method, params = {}, sessionId = session) {
    const id = ++serial;
    return new Promise((resolve, reject) => {
      const timer = setTimeout(() => { pending.delete(id); reject(new Error(`Timeout: ${method}`)); }, 10000);
      pending.set(id, { resolve, reject, timer });
      socket.send(JSON.stringify({ id, method, params, ...(sessionId ? { sessionId } : {}) }));
    });
  }
  function once(method) {
    const key = `${session}:${method}`;
    return new Promise((resolve, reject) => {
      const timer = setTimeout(() => { waiters.delete(key); reject(new Error(`Timeout: ${method}`)); }, 10000);
      waiters.set(key, params => { clearTimeout(timer); resolve(params); });
    });
  }
  async function evaluate(expression) {
    const result = await send('Runtime.evaluate', { expression, returnByValue: true, awaitPromise: true });
    assert.equal(result.exceptionDetails, undefined, JSON.stringify(result.exceptionDetails));
    return result.result.value;
  }
  const target = await send('Target.createTarget', { url: 'about:blank' }, null);
  session = (await send('Target.attachToTarget', { targetId: target.targetId, flatten: true }, null)).sessionId;
  await send('Page.enable');
  await send('Runtime.enable');
  await send('Network.enable');
  await send('Network.emulateNetworkConditions', { offline: true, latency: 0, downloadThroughput: -1, uploadThroughput: -1 });
  const deck = fileURLToPath(new URL('../assets/deck-starter.html', import.meta.url));
  const source = await readFile(deck, 'utf8');
  assert.ok(source.includes('data-title="Situación concreta"'));
  const loaded = once('Page.loadEventFired');
  await send('Page.navigate', { url: pathToFileURL(deck).href });
  await loaded;
  const state = () => evaluate(`({
    count: document.querySelector('#slide-count').textContent,
    previous: document.querySelector('#previous').disabled,
    next: document.querySelector('#next').disabled,
    progress: document.querySelector('#progress').value,
    hash: location.hash,
    visible: [...document.querySelectorAll('.slide')].filter(s => !s.hidden).length
  })`);
  const focusDeck = () => evaluate(`document.querySelector('#deck').focus()`);
  async function key(value, code = value) {
    await send('Input.dispatchKeyEvent', { type: 'keyDown', key: value, code,
      ...(value === ' ' ? { text: ' ', windowsVirtualKeyCode: 32 } : {}) });
    await send('Input.dispatchKeyEvent', { type: 'keyUp', key: value, code });
  }
  function record(name) { checks.push({ check: name, result: 'PASS' }); }

  assert.deepEqual(await state(), { count: '1 / 2 · Situación concreta', previous: true,
    next: false, progress: 1, hash: '#slide-1', visible: 1 });
  record('file:// startup while offline, first-slide state');
  await focusDeck();
  for (const value of ['ArrowRight', 'ArrowDown', ' ']) {
    await key('Home'); await key(value, value === ' ' ? 'Space' : value);
    assert.equal((await state()).progress, 2);
  }
  for (const value of ['ArrowLeft', 'ArrowUp']) {
    await key('End'); await key(value);
    assert.equal((await state()).progress, 1);
  }
  await key('End'); await key('ArrowRight');
  assert.equal((await state()).progress, 2);
  assert.equal((await state()).next, true);
  await key('Home'); await key('ArrowLeft');
  assert.equal((await state()).progress, 1);
  record('all navigation keys, progress, hidden state, and boundaries');

  await evaluate(`document.querySelector('#help').focus()`);
  await key(' ', 'Space');
  assert.equal(await evaluate(`document.querySelector('#help-dialog').open`), true);
  assert.equal((await state()).progress, 1);
  await key('Escape');
  assert.equal(await evaluate(`document.querySelector('#help-dialog').open`), false);
  record('focused button Space opens help without navigating; Escape closes it');

  await focusDeck(); await key('n');
  assert.equal(await evaluate(`document.querySelector('#notes').getAttribute('aria-pressed')`), 'true');
  await key('End');
  assert.equal(await evaluate(`document.querySelector('#notes-text').textContent`), 'Sin notas para esta diapositiva.');
  await key('Escape');
  assert.equal(await evaluate(`document.querySelector('#notes-panel').hidden`), true);
  record('notes toggle, slide-specific content, and Escape');

  for (const hash of ['#slide-1', '#slide-2', '#slide-999', '#not-a-slide']) {
    await evaluate(`new Promise(resolve => {
      window.addEventListener('hashchange', () => resolve(true), { once: true });
      location.hash = ${JSON.stringify(hash)};
    })`);
    assert.equal((await state()).progress, hash === '#slide-2' ? 2 : 1);
  }
  record('valid and invalid live hashes');

  await evaluate(`window.originalRequestFullscreen = document.documentElement.requestFullscreen;
    document.documentElement.requestFullscreen = () => Promise.reject(new Error('test rejection'));
    document.querySelector('#fullscreen').click()`);
  assert.equal(await evaluate(`document.querySelector('#runtime-status').textContent`), 'El navegador no permitió la pantalla completa.');
  record('fullscreen rejection is handled without an unhandled exception');
  await evaluate(`document.documentElement.requestFullscreen = window.originalRequestFullscreen;
    delete window.originalRequestFullscreen;
    document.querySelector('#runtime-status').textContent = ''`);

  for (const [width, height] of [[1366, 768], [1280, 720], [390, 844]]) {
    await send('Emulation.setDeviceMetricsOverride', { width, height, deviceScaleFactor: 1, mobile: false });
    for (const slide of [1, 2]) {
      await focusDeck(); await key(slide === 1 ? 'Home' : 'End');
      await evaluate(`new Promise(resolve => requestAnimationFrame(() => requestAnimationFrame(resolve)))`);
      await evaluate(`Promise.all(document.getAnimations().map(animation => animation.finished.catch(() => {})))`);
      const layout = await evaluate(`(() => {
        const deck = document.querySelector('#deck');
        const slide = document.querySelector('.slide:not([hidden])');
        return { horizontal: deck.scrollWidth <= deck.clientWidth + 1,
          fits: slide.scrollHeight <= deck.clientHeight + 1 };
      })()`);
      assert.equal(layout.horizontal, true, `horizontal overflow ${width} slide ${slide}`);
      if (width > 700) assert.equal(layout.fits, true, `vertical overflow ${width} slide ${slide}`);
      const screenshot = await send('Page.captureScreenshot', { format: 'png' });
      await writeFile(path.join(output, `${width}x${height}-slide-${slide}.png`), Buffer.from(screenshot.data, 'base64'));
    }
    record(`layout bounds and screenshots at ${width}x${height}`);
  }
  await send('Emulation.setEmulatedMedia', { features: [{ name: 'prefers-reduced-motion', value: 'reduce' }] });
  assert.equal(await evaluate(`getComputedStyle(document.querySelector('.slide:not([hidden])')).animationName`), 'none');
  record('reduced motion disables slide animation');
  assert.deepEqual(exceptions, []);
  assert.deepEqual(consoleErrors, []);
  assert.deepEqual(requests.filter(url => /^(https?|wss?):/.test(url)), []);
  record('no runtime exceptions, console errors, or page network requests');
  const report = { artifact: deck, browser: (await send('Browser.getVersion', {}, null)).product, checks,
    limitations: ['No pedagogical grading or old/new skill generation benchmark.',
      'Actual fullscreen entry/exit, screen readers, and all focus-order cases still require manual checks.'] };
  await writeFile(path.join(output, 'starter-report.json'), JSON.stringify(report, null, 2));
  console.log(JSON.stringify(report, null, 2));
} catch (error) {
  await writeFile(path.join(output, 'starter-report.json'), JSON.stringify({
    checks, failure: error.message, exceptions, consoleErrors,
    limitations: ['Run stopped on first failure; remaining checks are NOT VERIFIED.']
  }, null, 2));
  throw error;
} finally {
  for (const operation of pending.values()) clearTimeout(operation.timer);
  socket?.close();
  chrome.kill();
}
