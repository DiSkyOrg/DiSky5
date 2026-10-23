#!/usr/bin/env bash
# Usage: changelog.sh <tag>  ->  Markdown release notes on stdout.
set -euo pipefail

tag="$1"
golden="src/test/resources/golden/syntax-digest.txt"

# A stable release is compared with the previous stable one, so its notes cover its own pre-releases.
case "$tag" in
  *-*) prev=$(git describe --tags --abbrev=0 "$tag^" 2>/dev/null || true) ;;
  *)   prev=$(git describe --tags --abbrev=0 --exclude '*-*' "$tag^" 2>/dev/null || true) ;;
esac
range="${prev:+$prev..}$tag"

section() {
  local title="$1" pattern="$2" lines
  lines=$(git log --no-merges --format='%s (%h)' "$range" \
    | grep -E "$pattern" \
    | sed -E 's/^[a-z]+\(([^)]+)\)!?: /- **\1**: /; s/^[a-z]+!?: /- /' || true)
  [ -n "$lines" ] && printf '### %s\n\n%s\n\n' "$title" "$lines"
  return 0
}

section "Breaking changes" '^[a-z]+(\([^)]+\))?!: '
section "Features" '^feat(\([^)]+\))?: '
section "Fixes" '^fix(\([^)]+\))?: '
section "Performance" '^perf(\([^)]+\))?: '

if [ -z "$prev" ]; then
  echo "First DiSky V5 release: every syntax is listed on the Atlas, https://atlas.disky.me."
  exit 0
fi

diff=$(git diff --unified=0 "$prev" "$tag" -- "$golden" | grep -E '^[+-]syntax ' || true)
[ -z "$diff" ] && exit 0

echo "### Syntax changes"
echo
printf '%s\n' "$diff" | awk -F' [|] ' '
  {
    sign = substr($1, 1, 1); split($1, head, " "); id = head[2]
    for (i = 2; i <= NF; i++) if ($i ~ /^patterns=/) { p = substr($i, 10); sub(/ ;; .*/, "", p) }
    if (sign == "+") added[id] = p; else removed[id] = p
  }
  END {
    for (id in added) if (!(id in removed)) out("Added", added[id])
    for (id in removed) if (!(id in added)) out("Removed", removed[id])
    for (id in added) if (id in removed) out("Changed", added[id])
  }
  function out(kind, p) { printf "- %s: `%s`\n", kind, p }
' | sort
echo
echo "Full reference: https://atlas.disky.me"
echo
echo "**Full diff**: https://github.com/${GITHUB_REPOSITORY:-DiSkyOrg/DiSky5}/compare/$prev...$tag"
