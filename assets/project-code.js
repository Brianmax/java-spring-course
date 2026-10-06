(() => {
  'use strict';
  const project = window.COURSE_PROJECT;
  const pane = document.querySelector('#file-pane');
  if (!project || !Array.isArray(project.files) || !project.files.length) {
    const error = document.createElement('p');
    error.className = 'viewer-error';
    error.textContent = 'No se pudo cargar el visor. Puedes descargar el ZIP y abrir el proyecto en tu computadora.';
    pane.replaceChildren(error);
    return;
  }
  const byPath = new Map(project.files.map(file => [file.path, file]));
  const tree = document.querySelector('#file-tree');
  const sidebar = document.querySelector('#file-sidebar');
  const code = document.querySelector('#source-code');
  const scroller = document.querySelector('#code-scroll');
  const status = document.querySelector('#viewer-status');
  const copyButton = document.querySelector('#copy-code');
  const defaultPath = document.querySelector('#project-browser').dataset.defaultFile;
  const fallback = byPath.has(defaultPath) ? defaultPath : project.files[0].path;
  const mobile = window.matchMedia('(max-width: 720px)');
  let currentFile = null;
  let copyTimer;

  function size(bytes) {
    return bytes < 1024 ? `${bytes} B` : `${(bytes / 1024).toLocaleString('es-PE', { maximumFractionDigits: 1 })} KB`;
  }
  function fileHash(path, line) {
    const params = new URLSearchParams({ file: path });
    if (line) params.set('line', String(line));
    return `#${params}`;
  }
  function fileURL(path) {
    return `${path.split('/').map(encodeURIComponent).join('/')}?v=${project.revision}`;
  }
  function icon() {
    const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svg.setAttribute('viewBox', '0 0 16 18');
    svg.setAttribute('class', 'file-icon');
    svg.setAttribute('aria-hidden', 'true');
    const shape = document.createElementNS(svg.namespaceURI, 'path');
    shape.setAttribute('d', 'M3 1.5h6l4 4v11H3zM9 1.5v4h4M5.5 9h5M5.5 12h5');
    shape.setAttribute('fill', 'none');
    shape.setAttribute('stroke', 'currentColor');
    shape.setAttribute('stroke-linejoin', 'round');
    svg.append(shape);
    return svg;
  }

  // Native disclosure controls keep folder navigation usable with the keyboard.
  const root = { directories: new Map(), files: [] };
  for (const file of project.files) {
    const parts = file.path.split('/');
    parts.pop();
    let parent = root;
    for (const part of parts) {
      if (!parent.directories.has(part)) parent.directories.set(part, { directories: new Map(), files: [] });
      parent = parent.directories.get(part);
    }
    parent.files.push(file);
  }
  function appendTree(node, parent) {
    for (const [name, child] of [...node.directories].sort(([a], [b]) => a.localeCompare(b))) {
      const group = document.createElement('details');
      group.open = true;
      const title = document.createElement('summary');
      title.textContent = name;
      group.append(title);
      appendTree(child, group);
      parent.append(group);
    }
    for (const file of node.files) {
      const link = document.createElement('a');
      link.className = 'file-link';
      link.dataset.path = file.path;
      link.href = fileHash(file.path);
      link.title = file.path;
      link.append(icon(), document.createTextNode(file.path.split('/').pop()));
      parent.append(link);
    }
  }
  appendTree(root, tree);
  document.querySelector('#file-count').textContent = String(project.files.length);
  document.querySelector('#source-count').textContent = `${project.sourceCount} archivos Java`;
  const zipLink = document.querySelector('#download-project');
  zipLink.href = fileURL(project.archive);
  zipLink.download = project.archive;
  document.querySelector('#archive-size').textContent = `Solo este proyecto · ZIP · ${size(project.archiveBytes)}`;
  sidebar.open = !mobile.matches;
  mobile.addEventListener('change', event => { sidebar.open = !event.matches; });

  const fileLinks = [...tree.querySelectorAll('.file-link')];
  document.querySelector('#file-filter').addEventListener('input', event => {
    const query = event.target.value.trim().toLocaleLowerCase('es');
    let count = 0;
    for (const link of fileLinks) {
      link.hidden = !link.dataset.path.toLocaleLowerCase('es').includes(query);
      if (!link.hidden) count++;
    }
    for (const folder of [...tree.querySelectorAll('details')].reverse()) {
      folder.hidden = !folder.querySelector('.file-link:not([hidden])');
      if (query && !folder.hidden) folder.open = true;
    }
    document.querySelector('#search-status').textContent = query ? (count ? `${count} archivos encontrados` : 'No hay archivos con ese nombre.') : '';
  });

  // Highlight lexical tokens only. All source text is inserted through textContent.
  const javaTokens = /(\/\*[\s\S]*?(?:\*\/|$)|\/\/[^\r\n]*)|("""[\s\S]*?(?:"""|$)|"(?:\\[\s\S]|[^"\\])*"|'(?:\\[\s\S]|[^'\\])*')|\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|if|implements|import|instanceof|int|interface|long|native|new|null|package|private|protected|public|record|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|true|false|try|var|void|volatile|while|yield)\b|\b(0[xX][\da-fA-F_]+[lL]?|\d[\d_]*(?:\.[\d_]+)?(?:[eE][+-]?[\d_]+)?[fFdDlL]?)\b|\b([A-Z][A-Za-z\d_]*)\b/g;
  function fragments(content, language) {
    if (language !== 'Java') return [{ text: content, kind: '' }];
    const result = [];
    let position = 0;
    for (const match of content.matchAll(javaTokens)) {
      if (match.index > position) result.push({ text: content.slice(position, match.index), kind: '' });
      const kind = match[1] ? 'comment' : match[2] ? 'string' : match[3] ? 'keyword' : match[4] ? 'number' : 'type';
      result.push({ text: match[0], kind });
      position = match.index + match[0].length;
    }
    if (position < content.length) result.push({ text: content.slice(position), kind: '' });
    return result;
  }
  function renderCode(file) {
    // A final newline terminates the last line; it does not add an empty numbered row.
    const text = file.content.replace(/\r\n/g, '\n').replace(/\n$/, '');
    const lines = [document.createElement('code')];
    for (const token of fragments(text, file.language)) {
      const chunks = token.text.split('\n');
      chunks.forEach((chunk, i) => {
        if (i > 0) lines.push(document.createElement('code'));
        const span = document.createElement('span');
        if (token.kind) span.className = `syntax-${token.kind}`;
        span.textContent = chunk;
        lines[lines.length - 1].append(span);
      });
    }
    const rows = document.createDocumentFragment();
    lines.forEach((content, index) => {
      const row = document.createElement('div');
      row.className = 'code-line';
      row.dataset.line = String(index + 1);
      const number = document.createElement('a');
      number.className = 'line-number';
      number.href = fileHash(file.path, index + 1);
      number.textContent = String(index + 1);
      number.setAttribute('aria-label', `Enlace a la línea ${index + 1}`);
      content.className = 'line-content';
      // A blank line still has the same height as a line with code.
      if (!content.textContent) content.append(document.createTextNode('\u200b'));
      row.append(number, content);
      rows.append(row);
    });
    code.replaceChildren(rows);
    document.querySelector('#file-meta').textContent = `${file.language} · ${lines.length} líneas · ${size(file.bytes)}`;
  }
  function readLocation() {
    const params = new URLSearchParams(location.hash.slice(1));
    const requested = params.get('file');
    const file = byPath.get(requested) || byPath.get(fallback);
    const line = Number(params.get('line'));
    if (currentFile !== file) {
      currentFile = file;
      clearTimeout(copyTimer);
      copyButton.textContent = 'Copiar';
      status.textContent = requested && !byPath.has(requested) ? 'Archivo no encontrado. Se muestra un ejemplo del proyecto.' : '';
      const filename = file.path.split('/').pop();
      const breadcrumb = document.querySelector('#file-path');
      const name = document.createElement('strong');
      name.textContent = filename;
      const prefix = file.path.slice(0, file.path.length - filename.length).replaceAll('/', ' / ');
      breadcrumb.replaceChildren(document.createTextNode(`${project.id} / ${prefix}`), name);
      document.querySelector('#file-name').textContent = filename;
      document.title = `${filename} · Java G19 · Curso de Java Tecsup`;
      renderCode(file);
      scroller.scrollTop = 0;
      scroller.scrollLeft = 0;
      const raw = document.querySelector('#raw-file');
      raw.href = fileURL(file.path);
      const download = document.querySelector('#download-file');
      download.href = fileURL(file.path);
      download.download = filename;
      for (const link of fileLinks) {
        if (link.dataset.path === file.path) {
          link.setAttribute('aria-current', 'true');
          let parent = link.parentElement;
          while (parent && parent !== tree) {
            if (parent.tagName === 'DETAILS') parent.open = true;
            parent = parent.parentElement;
          }
        } else link.removeAttribute('aria-current');
      }
    }
    code.querySelectorAll('.is-selected').forEach(row => row.classList.remove('is-selected'));
    if (Number.isInteger(line) && line > 0) {
      const row = code.querySelector(`[data-line="${line}"]`);
      if (row) {
        row.classList.add('is-selected');
        scroller.scrollTop += row.getBoundingClientRect().top - scroller.getBoundingClientRect().top - 72;
      }
    }
  }
  tree.addEventListener('click', event => {
    if (!event.target.closest('.file-link') || event.ctrlKey || event.metaKey || event.shiftKey || event.altKey) return;
    if (mobile.matches) {
      sidebar.open = false;
      document.querySelector('#file-name').focus({ preventScroll: true });
    }
  });
  document.querySelector('#wrap-code').addEventListener('click', event => {
    const wrapped = code.classList.toggle('wrap');
    event.currentTarget.setAttribute('aria-pressed', String(wrapped));
  });
  copyButton.addEventListener('click', async () => {
    const content = currentFile.content;
    let copied = false;
    try {
      if (navigator.clipboard && window.isSecureContext) {
        await navigator.clipboard.writeText(content);
        copied = true;
      }
    } catch (_) { /* Fall back to copying a selected text field below. */ }
    if (!copied) {
      const field = document.createElement('textarea');
      field.value = content;
      field.style.cssText = 'position:fixed;left:-9999px;top:0';
      document.body.append(field);
      field.select();
      try { copied = document.execCommand('copy'); } catch (_) { copied = false; }
      field.remove();
      copyButton.focus({ preventScroll: true });
    }
    status.textContent = copied ? 'Código copiado al portapapeles.' : 'No se pudo copiar. Selecciona el código o descarga el archivo.';
    copyButton.textContent = copied ? 'Copiado' : 'Copiar';
    clearTimeout(copyTimer);
    copyTimer = setTimeout(() => { copyButton.textContent = 'Copiar'; }, 2500);
  });
  window.addEventListener('hashchange', readLocation);
  readLocation();
})();
