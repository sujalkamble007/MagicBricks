#!/usr/bin/env bash
# ==============================================================================
# MagicBricks Automation Suite Runner
# Usage:
#   ./run.sh              -> Runs the whole test suite (Home + Login + Sell)
#   ./run.sh all          -> Runs the whole test suite
#   ./run.sh parallel     -> Runs test classes concurrently in parallel (3 threads)
#   ./run.sh crossbrowser -> Runs tests across Chrome, Firefox, and Edge simultaneously
#   ./run.sh homelogin    -> Runs Home and Login modules together
#   ./run.sh sell         -> Runs Sell module separately
#   ./run.sh home         -> Runs Home module only
#   ./run.sh login        -> Runs Login module only
#   ./run.sh chrome       -> Runs suite explicitly on Google Chrome
#   ./run.sh firefox      -> Runs suite explicitly on Mozilla Firefox
#   ./run.sh edge         -> Runs suite explicitly on Microsoft Edge
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
  parallel)
    echo "▶ Running Test Suite in PARALLEL (Thread Count = 3)..."
    run_mvn -f "$PROJECT_DIR" test -P parallel
    ;;
  crossbrowser)
    echo "▶ Running CROSS-BROWSER Test Suite (Chrome + Firefox + Edge in Parallel)..."
    run_mvn -f "$PROJECT_DIR" test -P crossbrowser
    ;;
  chrome)
    echo "▶ Running Test Suite on GOOGLE CHROME Browser..."
    run_mvn -f "$PROJECT_DIR" test -Dbrowser=chrome
    ;;
  firefox)
    echo "▶ Running Test Suite on MOZILLA FIREFOX Browser..."
    run_mvn -f "$PROJECT_DIR" test -Dbrowser=firefox
    ;;
  edge)
    echo "▶ Running Test Suite on MICROSOFT EDGE Browser..."
    run_mvn -f "$PROJECT_DIR" test -Dbrowser=edge
    ;;
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
