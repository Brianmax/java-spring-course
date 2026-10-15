/** Offline Chromium regression audit for the POO deck. Node 22+, no npm packages.
 * node scripts/check_poo_deck.mjs <deck.html> <evidence-directory> [--strict]
 * Set CHROME_BIN if Chromium is not at the macOS default path.
 * Evidence and isolated browser profiles remain in the output directory.
 */
import assert from 'node:assert/strict';
import { spawn } from 'node:child_process';
import { mkdir, mkdtemp, stat, writeFile } from 'node:fs/promises';
import path from 'node:path';
import { pathToFileURL } from 'node:url';

const [input, output, strict] = process.argv.slice(2);
if (!input || !output || (strict && strict !== '--strict') || process.argv.length > 5) {
  console.error('Usage: node scripts/check_poo_deck.mjs <deck.html> <evidence-directory> [--strict]');
  process.exit(2);
}
assert.ok(input.endsWith('.html') && (await stat(input)).isFile(), 'Input must be an existing HTML deck');
await mkdir(output, { recursive: true });
const profile = await mkdtemp(path.join(path.resolve(output), 'chrome-profile-'));
const chrome = spawn(process.env.CHROME_BIN || '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',
  ['--headless=new', '--remote-debugging-port=0', '--remote-debugging-address=127.0.0.1',
    `--user-data-dir=${profile}`, '--no-first-run', '--no-default-browser-check',
    '--disable-background-networking', '--disable-ipc-flooding-protection', 'about:blank'], { stdio: ['ignore', 'ignore', 'pipe'] });
let socket, session, serial = 0;
const pending = new Map();
const waiters = new Map();
const exceptions = [], consoleErrors = [], requests = [], checks = [], layouts = [];

try {
  const endpoint = await new Promise((resolve, reject) => {
    let stderr = '';
    const timer = setTimeout(() => reject(new Error('Chromium startup timeout')), 15000);
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
      pending.delete(message.id); clearTimeout(operation.timer);
      if (message.error) operation.reject(new Error(JSON.stringify(message.error)));
      else operation.resolve(message.result);
    } else {
      if (message.method === 'Runtime.exceptionThrown') exceptions.push(message.params.exceptionDetails.text);
      if (message.method === 'Runtime.consoleAPICalled' && message.params.type === 'error') {
        consoleErrors.push(message.params.args.map(arg => arg.value || arg.description).join(' '));
      }
      if (message.method === 'Network.requestWillBeSent') requests.push(message.params.request.url);
      const key = `${message.sessionId}:${message.method}`;
      if (waiters.has(key)) { const callback = waiters.get(key); waiters.delete(key); callback(message.params); }
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
      waiters.set(key, value => { clearTimeout(timer); resolve(value); });
    });
  }
  async function evaluate(expression) {
    let result;
    try { result = await send('Runtime.evaluate', { expression, returnByValue: true, awaitPromise: true }); }
    catch (error) { throw new Error(`${error.message}: ${expression.slice(0, 240)}`); }
    assert.equal(result.exceptionDetails, undefined, JSON.stringify(result.exceptionDetails));
    return result.result.value;
  }
  async function check(name, operation) {
    try { await operation(); checks.push({ name, result: 'PASS' }); }
    catch (error) { checks.push({ name, result: 'FAIL', evidence: error.message }); }
  }
  async function key(value, modifiers = 0) {
    await send('Input.dispatchKeyEvent', { type: 'keyDown', key: value,
      code: value === ' ' ? 'Space' : value, modifiers,
      ...(value === ' ' ? { text: ' ', windowsVirtualKeyCode: 32 } : {}) });
    await send('Input.dispatchKeyEvent', { type: 'keyUp', key: value, modifiers });
  }
  const target = await send('Target.createTarget', { url: 'about:blank' }, null);
  session = (await send('Target.attachToTarget', { targetId: target.targetId, flatten: true }, null)).sessionId;
  await send('Page.enable'); await send('Runtime.enable'); await send('Network.enable');
  await send('Network.emulateNetworkConditions', { offline: true, latency: 0, downloadThroughput: -1, uploadThroughput: -1 });
  await send('Emulation.setEmulatedMedia', { features: [{ name: 'prefers-reduced-motion', value: 'reduce' }] });
  const loaded = once('Page.loadEventFired');
  await send('Page.navigate', { url: pathToFileURL(path.resolve(input)).href }); await loaded;
  const total = await evaluate(`document.querySelectorAll('.slide').length`);
  const current = () => evaluate(`Number(location.hash.replace('#slide-', ''))`);
  async function go(number) {
    await evaluate(`new Promise(resolve => {
      const target = '#slide-${number}';
      if (location.hash === target) return resolve(true);
      window.addEventListener('hashchange', () => resolve(true), { once: true });
      location.hash = target;
    })`);
  }
  const blur = () => evaluate(`document.activeElement.blur()`);

  await check('offline file startup', async () => { assert.equal(await current(), 1); assert.ok(total >= 2); });
  await check('valid and invalid initial hashes', async () => {
    for (const [hash, expected] of [['slide-2', 2], ['slide-9999', 1]]) {
      const loaded = once('Page.loadEventFired');
      await send('Page.navigate', { url: `${pathToFileURL(path.resolve(input)).href}?initial=${hash}#${hash}` });
      await loaded; assert.equal(await current(), expected);
    }
  });
  await check('arrows, Space, Home, End and boundaries', async () => {
    await blur();
    for (const forward of ['ArrowRight', 'ArrowDown', ' ']) {
      await key('Home'); await key(forward); assert.equal(await current(), 2);
    }
    for (const backward of ['ArrowLeft', 'ArrowUp']) {
      await key('End'); await key(backward); assert.equal(await current(), total - 1);
    }
    await key('End'); await key('ArrowRight'); assert.equal(await current(), total);
    assert.equal(await evaluate(`document.querySelector('#next').disabled`), true);
    await key('Home'); await key('ArrowLeft'); assert.equal(await current(), 1);
    assert.equal(await evaluate(`document.querySelector('#prev').disabled`), true);
  });
  await check('modifier shortcuts do not navigate', async () => {
    await go(3); await blur(); await key('Home', 2); assert.equal(await current(), 3);
  });
  await check('focused help button Space and Escape', async () => {
    await go(1); await evaluate(`document.querySelector('#help').focus()`); await key(' ');
    assert.equal(await evaluate(`document.querySelector('dialog').open`), true);
    assert.equal(await current(), 1); await key('Escape');
    assert.equal(await evaluate(`document.querySelector('dialog').open`), false);
  });
  await check('topic menu selection does not steal keyboard input', async () => {
    const second = await evaluate(`Number(document.querySelector('#topic-menu').options[1].value)`);
    await evaluate(`const menu = document.querySelector('#topic-menu'); menu.value = menu.options[1].value; menu.dispatchEvent(new Event('change')); menu.focus()`);
    assert.equal(await current(), second); await key('Home'); assert.equal(await current(), second); await blur();
  });
  await check('invalid hash falls back safely', async () => { await go(9999); assert.equal(await current(), 1); });
  await check('final-slide progress and hidden-state isolation', async () => {
    await go(total);
    assert.equal(await evaluate(`document.querySelector('#progress-fill').style.width`), '100%');
    assert.equal(await evaluate(`document.querySelectorAll('.slide:not([hidden])').length`), 1);
    assert.equal(await evaluate(`document.querySelector('#slide-indicator').textContent`), `${total} / ${total}`);
  });
  await check('fullscreen entry/exit synchronizes state', async () => {
    await blur(); await evaluate(`window.fullscreenReady = new Promise(resolve => {
      document.addEventListener('fullscreenchange', () => resolve(true), { once: true });
    }); true`);
    await key('f'); await evaluate('window.fullscreenReady');
    assert.equal(await evaluate(`Boolean(document.fullscreenElement)`), true);
    assert.equal(await evaluate(`document.querySelector('#fullscreen').getAttribute('aria-pressed')`), 'true');
    await evaluate(`new Promise(resolve => {
      document.addEventListener('fullscreenchange', () => resolve(true), { once: true });
      document.querySelector('#fullscreen').click();
    })`);
    assert.equal(await evaluate(`Boolean(document.fullscreenElement)`), false);
  });

  const captures = new Set([1, 3, 6, 7, 8, 9, 10, 11, 13, 18, 20, 23, 31, 35, 38, 39, 40, 44, 47, 48, 49, 52, 55, 56, 64, 70, total]);
  for (const [width, height] of [[1366, 768], [1280, 720], [390, 844]]) {
    await send('Emulation.setDeviceMetricsOverride', { width, height, deviceScaleFactor: 1, mobile: false });
    for (let number = 1; number <= total; number++) {
      await go(number);
      const layout = await evaluate(`(() => {
        const slide = document.querySelector('.slide:not([hidden])');
        const rect = slide.getBoundingClientRect();
        const controls = document.querySelector('.deck-controls').getBoundingClientRect();
        const clippedCode = [...slide.querySelectorAll('pre')].filter(el => el.scrollWidth > el.clientWidth + 2).length;
        const diagrams = [...slide.querySelectorAll('.diagram')].filter(el => el.scrollWidth > el.clientWidth + 2).length;
        return { id: slide.id, title: slide.dataset.title,
          verticalOverflow: Math.max(0, slide.scrollHeight - slide.clientHeight),
          horizontalOverflow: Math.max(0, slide.scrollWidth - slide.clientWidth),
          controlsOverlap: Math.max(0, rect.bottom - controls.top), clippedCode, diagrams };
      })()`);
      layouts.push({ width, height, ...layout });
      if (captures.has(number) || layout.verticalOverflow > 2 || layout.horizontalOverflow > 2) {
        const screenshot = await send('Page.captureScreenshot', { format: 'png' });
        await writeFile(path.join(output, `${width}x${height}-${layout.id}.png`), Buffer.from(screenshot.data, 'base64'));
      }
    }
  }
  await check('no runtime exceptions, console errors, or page network requests', async () => {
    assert.deepEqual(exceptions, []); assert.deepEqual(consoleErrors, []);
    assert.deepEqual(requests.filter(url => /^(https?|wss?):/.test(url)), []);
  });
  const desktopIssues = layouts.filter(item => item.width > 700 &&
    (item.verticalOverflow > 2 || item.horizontalOverflow > 2 || item.controlsOverlap > 2 || item.clippedCode));
  const mobileIssues = layouts.filter(item => item.width < 700 && (item.horizontalOverflow > 2 || item.controlsOverlap > 2));
  const report = { deck: input, browser: (await send('Browser.getVersion', {}, null)).product, total, checks,
    desktopIssues, mobileIssues, layouts,
    limitations: ['DOM bounds are not a visual or pedagogical judgment.', 'No screen-reader certification.'] };
  await writeFile(path.join(output, 'report.json'), JSON.stringify(report, null, 2));
  console.log(JSON.stringify({ total, checks, desktopIssues, mobileIssues }, null, 2));
  if (checks.some(item => item.result === 'FAIL') || (strict === '--strict' && (desktopIssues.length || mobileIssues.length))) process.exitCode = 1;
} catch (error) {
  await writeFile(path.join(output, 'failure.json'), JSON.stringify({ error: error.message, checks, layouts: layouts.length }, null, 2));
  throw error;
} finally {
  for (const operation of pending.values()) clearTimeout(operation.timer);
  socket?.close(); chrome.kill();
}
