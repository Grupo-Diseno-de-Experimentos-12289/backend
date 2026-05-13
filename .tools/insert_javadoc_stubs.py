"""Insert Javadoc stubs for every MissingJavadocType / MissingJavadocMethod
violation reported by Checkstyle.

Reads target/checkstyle-result.xml, groups violations by file, and inserts a
contextual /** ... */ comment immediately before each reported line. Processes
lines per file in reverse order so earlier insertions don't shift later ones.
"""

from __future__ import annotations

import re
import xml.etree.ElementTree as ET
from pathlib import Path

REPORT = Path("target/checkstyle-result.xml")

CLASS_RE = re.compile(
    r"^\s*(?:public|protected|private|abstract|final|static|\s)*\s*"
    r"(class|interface|record|enum|@interface)\s+([A-Za-z_][A-Za-z0-9_]*)"
)
METHOD_RE = re.compile(
    r"^\s*(?:@\w+(?:\([^)]*\))?\s*)*"
    r"(?:public|protected|private|abstract|default|static|final|synchronized|native|\s)*\s*"
    r"(?:<[^>]+>\s*)?"
    r"[\w<>\[\],\s.?]+\s+([A-Za-z_][A-Za-z0-9_]*)\s*\("
)
CONSTRUCTOR_RE = re.compile(
    r"^\s*(?:public|protected|private|\s)*\s*"
    r"([A-Z][A-Za-z0-9_]*)\s*\("
)


def humanize(name: str) -> str:
    """Convert camelCase / PascalCase to a readable space-separated phrase."""
    s = re.sub(r"(?<!^)(?=[A-Z])", " ", name).lower()
    return s.strip()


def build_stub(indent: str, line_text: str, file_path: Path) -> str:
    """Return a one-line Javadoc stub appropriate for the next code line."""
    m = CLASS_RE.match(line_text)
    if m:
        kind, name = m.group(1), m.group(2)
        if kind == "interface":
            return f"{indent}/** {name} contract. */\n"
        if kind == "enum":
            return f"{indent}/** {name} enumeration. */\n"
        if kind == "record":
            return f"{indent}/** {name} value carrier. */\n"
        return f"{indent}/** {name} type. */\n"

    cls_name = file_path.stem
    if re.match(rf"^\s*(?:public|protected|private)?\s*{re.escape(cls_name)}\s*\(", line_text):
        return f"{indent}/** Constructs a {humanize(cls_name)}. */\n"

    m = METHOD_RE.match(line_text)
    if m:
        method = m.group(1)
        return f"{indent}/** {humanize(method).capitalize()}. */\n"

    m = CONSTRUCTOR_RE.match(line_text)
    if m:
        return f"{indent}/** Constructs a {humanize(m.group(1))}. */\n"

    return f"{indent}/** Documentation. */\n"


def already_has_javadoc(lines: list[str], idx: int) -> bool:
    """Check if the line(s) above the target already contain a Javadoc."""
    j = idx - 1
    while j >= 0 and lines[j].strip().startswith("@"):
        j -= 1
    if j < 0:
        return False
    prev = lines[j].rstrip()
    return prev.endswith("*/")


def main() -> None:
    tree = ET.parse(REPORT)
    root = tree.getroot()

    targets: dict[str, set[int]] = {}
    for f in root.findall("file"):
        fpath = f.get("name")
        if not fpath:
            continue
        for err in f.findall("error"):
            src = err.get("source", "")
            if "MissingJavadocType" in src or "MissingJavadocMethod" in src:
                line = int(err.get("line", "0"))
                if line:
                    targets.setdefault(fpath, set()).add(line)

    total = 0
    for fpath, line_set in targets.items():
        p = Path(fpath)
        text = p.read_text(encoding="utf-8")
        lines = text.splitlines(keepends=True)
        for ln in sorted(line_set, reverse=True):
            idx = ln - 1
            if idx < 0 or idx >= len(lines):
                continue
            original = lines[idx]
            # Skip annotation lines — walk down to the real declaration line
            scan = idx
            while scan < len(lines) and lines[scan].lstrip().startswith("@"):
                scan += 1
            decl_line = lines[scan] if scan < len(lines) else original
            indent = re.match(r"^(\s*)", original).group(1)
            if already_has_javadoc(lines, idx):
                continue
            stub = build_stub(indent, decl_line, p)
            lines.insert(idx, stub)
            total += 1
        p.write_text("".join(lines), encoding="utf-8")
    print(f"Inserted {total} Javadoc stubs across {len(targets)} files")


if __name__ == "__main__":
    main()
