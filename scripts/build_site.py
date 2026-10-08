#!/usr/bin/env python3
"""Build the Pages artifact, regenerating project downloads from current sources."""

import hashlib
from pathlib import Path
import shutil
import subprocess

from package_projects import ROOT, PROJECTS, artifacts

OUTPUT = ROOT / "_site"
PUBLIC_DIRS = {"assets", "lessons", "exercises", "projects"}


def main():
    # Publish only tracked site files; editor settings and local build outputs stay out.
    tracked = subprocess.check_output(
        ["git", "ls-files", "-z"], cwd=ROOT
    ).decode("utf-8").split("\0")
    if OUTPUT.exists():
        shutil.rmtree(OUTPUT)
    OUTPUT.mkdir()
    for name in filter(None, tracked):
        relative = Path(name)
        if name != "index.html" and relative.parts[0] not in PUBLIC_DIRS:
            continue
        source = ROOT / relative
        if source.is_symlink() or not source.is_file():
            continue
        destination = OUTPUT / relative
        destination.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(source, destination)

    for project in sorted(PROJECTS.iterdir()):
        if not (project / "src").is_dir():
            continue
        page = OUTPUT / project.relative_to(ROOT) / "index.html"
        if not page.is_file():
            raise ValueError(f"Missing tracked project page: {project.name}/index.html")
        generated = artifacts(project)
        for path, content in generated.items():
            destination = OUTPUT / path.relative_to(ROOT)
            destination.write_bytes(content)

        # A new snapshot gets a new URL so a cached script cannot hide new source files.
        data = generated[project / "project-data.js"]
        version = hashlib.sha256(data).hexdigest()[:16]
        html = page.read_text(encoding="utf-8")
        script = 'src="project-data.js"'
        if script not in html:
            raise ValueError(f"Missing project-data.js script in {page}")
        html = html.replace(script, f'src="project-data.js?v={version}"')
        html = html.replace(f'href="{project.name}.zip"',
                            f'href="{project.name}.zip?v={version}"')
        page.write_text(html, encoding="utf-8")
        print(f"Packaged {project.name} from current sources ({version})")

    (OUTPUT / ".nojekyll").touch()
    print(f"Site ready: {OUTPUT}")


if __name__ == "__main__":
    main()
