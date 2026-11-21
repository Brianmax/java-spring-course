#!/usr/bin/env python3
"""Sync a project folder from this monorepo to its standalone GitHub repository."""

import argparse
import os
from pathlib import Path
import shutil
import stat
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[1]
EXCLUDED_ROOT_FILES = {"index.html", "project-data.js"}


def run(cmd, cwd=None, env=None, capture=False):
    result = subprocess.run(
        cmd,
        cwd=cwd,
        env=env,
        text=True,
        capture_output=capture,
        check=True,
    )
    return result.stdout if capture else ""


def tracked_project_files(source_rel: str) -> list[Path]:
    raw = subprocess.check_output(
        ["git", "ls-files", "-z", "--", source_rel], cwd=ROOT
    ).decode("utf-8")
    source_dir = ROOT / source_rel
    files = []
    for entry in filter(None, raw.split("\0")):
        path = ROOT / entry
        if not path.is_file() or path.is_symlink():
            continue
        rel = path.relative_to(source_dir)
        if len(rel.parts) == 1 and (
            rel.name in EXCLUDED_ROOT_FILES or rel.suffix == ".zip"
        ):
            continue
        files.append(rel)
    return sorted(files)


def latest_commit_info(source_rel: str) -> tuple[str, str, str, str]:
    raw = run(
        ["git", "log", "-1", "--format=%H%x00%an%x00%ae%x00%s", "--", source_rel],
        cwd=ROOT,
        capture=True,
    ).strip()
    if not raw:
        raw = run(
            ["git", "log", "-1", "--format=%H%x00%an%x00%ae%x00%s"],
            cwd=ROOT,
            capture=True,
        ).strip()
    sha, author_name, author_email, subject = raw.split("\0", 3)
    return sha, author_name, author_email, subject


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--source", required=True, help="Relative path to project folder")
    parser.add_argument("--repo", required=True, help="Target GitHub repo (owner/name)")
    parser.add_argument("--branch", default="main", help="Target branch (default: main)")
    parser.add_argument(
        "--key-env",
        default="",
        help="Optional environment variable containing the SSH deploy private key",
    )
    args = parser.parse_args()

    source_dir = (ROOT / args.source).resolve()
    if not source_dir.is_dir():
        raise SystemExit(f"Source directory not found: {args.source}")

    files = tracked_project_files(args.source)
    if not files:
        raise SystemExit(f"No tracked project files found in {args.source}")

    sha, author_name, author_email, subject = latest_commit_info(args.source)
    short_sha = sha[:7]
    remote_url = f"git@github.com:{args.repo}.git"

    with tempfile.TemporaryDirectory(prefix="sync-project-") as tmp:
        tmp_path = Path(tmp)
        env = os.environ.copy()

        if args.key_env:
            key_content = os.environ.get(args.key_env, "").strip()
            if not key_content:
                raise SystemExit(f"Environment variable {args.key_env} is empty or not set")
            key_file = tmp_path / "deploy_key"
            key_file.write_text(key_content + "\n", encoding="utf-8")
            os.chmod(key_file, stat.S_IRUSR | stat.S_IWUSR)
            env["GIT_SSH_COMMAND"] = (
                f'ssh -i "{key_file}" -o IdentitiesOnly=yes '
                "-o StrictHostKeyChecking=accept-new"
            )

        work_dir = tmp_path / "repo"
        clone = subprocess.run(
            ["git", "clone", "--depth", "1", "--branch", args.branch, remote_url, str(work_dir)],
            env=env,
            text=True,
            capture_output=True,
        )
        if clone.returncode != 0:
            # Handle brand-new empty repositories cleanly.
            work_dir.mkdir(parents=True, exist_ok=True)
            run(["git", "init"], cwd=work_dir, env=env)
            run(["git", "checkout", "-B", args.branch], cwd=work_dir, env=env)
            run(["git", "remote", "add", "origin", remote_url], cwd=work_dir, env=env)

        for item in list(work_dir.iterdir()):
            if item.name == ".git":
                continue
            if item.is_dir():
                shutil.rmtree(item)
            else:
                item.unlink()

        for rel in files:
            src = source_dir / rel
            dst = work_dir / rel
            dst.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(src, dst)

        run(["git", "add", "-A"], cwd=work_dir, env=env)
        status = run(["git", "status", "--porcelain"], cwd=work_dir, env=env, capture=True).strip()
        if not status:
            print(f"{args.repo}: already up to date ({len(files)} files)")
            return

        run(["git", "config", "user.name", author_name], cwd=work_dir, env=env)
        run(["git", "config", "user.email", author_email], cwd=work_dir, env=env)
        commit_message = (
            f"{subject}\n\n"
            f"Synced from Brianmax/java-spring-course@{short_sha} ({args.source})"
        )
        run(["git", "commit", "-m", commit_message], cwd=work_dir, env=env)
        run(["git", "push", "-u", "origin", args.branch], cwd=work_dir, env=env)
        print(f"{args.repo}: pushed {len(files)} files from {short_sha}")


if __name__ == "__main__":
    main()
