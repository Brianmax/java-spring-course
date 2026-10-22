import { readFile, writeFile } from 'node:fs/promises';
import { fileURLToPath } from 'node:url';
import path from 'node:path';
import postcss from 'postcss';
import tailwindcss from '@tailwindcss/postcss';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const stylesheets = [
  ['assets/styles.css', 'assets/tailwind.css'],
  ['assets/decks/databases.styles.css', 'assets/decks/databases.css'],
  ['assets/decks/java-introduction.styles.css', 'assets/decks/java-introduction.css'],
  ['assets/decks/java-poo.styles.css', 'assets/decks/java-poo.css'],
];

for (const [input, target] of stylesheets) {
  const source = path.join(root, input);
  const output = path.join(root, target);
  const css = await readFile(source, 'utf8');
  const result = await postcss([tailwindcss()]).process(css, { from: source, to: output });
  await writeFile(output, result.css);
  console.log(`Compiled ${target}`);
}
