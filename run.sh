#!/usr/bin/env bash
# ==============================================================================
# MagicBricks Automation Suite Runner
# Usage:
#   ./run.sh            -> Runs the whole test suite (Home + Login + Sell)
#   ./run.sh all        -> Runs the whole test suite
#   ./run.sh homelogin  -> Runs Home and Login modules together
#   ./run.sh sell       -> Runs Sell module separately
#   ./run.sh home       -> Runs Home module only
#   ./run.sh login      -> Runs Login module only
# ==============================================================================

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ -d "$DIR/MagicBricks" ]; then
  PROJECT_DIR="$DIR/MagicBricks"
else
  PROJECT_DIR="$DIR"
fi

run_mvn() {
  mvn -Dstyle.color=always "$@" 2>&1 | grep -v -E "sun.misc.Unsafe|HiddenClassDefiner|maintainers of class|removed in a future release|package sun.misc not in"
}

case "$1" in
  homelogin|home-login)
    echo "▶ Running Home and Login Modules..."
    run_mvn -f "$PROJECT_DIR" test -P homelogin
    ;;
  sell)
    echo "▶ Running Sell Module..."
    run_mvn -f "$PROJECT_DIR" test -P sell
    ;;
  home)
    echo "▶ Running Home Page Module..."
    run_mvn -f "$PROJECT_DIR" test -Dtest=HomePageTest
    ;;
  login)
    echo "▶ Running Login Module..."
    run_mvn -f "$PROJECT_DIR" test -Dtest=LoginTest
    ;;
  *)
    echo "▶ Running Complete Automation Suite (All Modules)..."
    run_mvn -f "$PROJECT_DIR" clean test
    ;;
esac
