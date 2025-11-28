#!/bin/bash

# --- Function to display usage and exit ---
usage() {
    echo "Usage: $0"
    echo "This script compiles and runs selected WebAssembly Text (wat) test files."
    echo "It expects the 'wat' files to be in the 'test/wat/' directory."
    echo "The compiled 'wasm' files will be placed in 'test/wasm/'."
    exit 1
}

# --- Check if essential tools are available ---
if ! command -v wat2wasm &> /dev/null || ! command -v wasm-interp &> /dev/null; then
    echo "Error: 'wat2wasm' or 'wasm-interp' command not found."
    echo "Please ensure the WebAssembly Binary Toolkit (wabt) is installed and in your PATH."
    exit 1
fi

# --- Main script execution ---

echo "✨ WebAssembly Test Runner ✨"
echo "------------------------------"

# Prompt the user for the list of test file names
# Example input: green1 blue5 red9
read -p "Enter the test file names (e.g., green1 blue5 red9): " TEST_FILES

# Check if the user provided any input
if [ -z "$TEST_FILES" ]; then
    echo "No test files entered. Exiting."
    usage
fi

# Set the counter for successful tests
SUCCESS_COUNT=0
TOTAL_COUNT=0

# Loop through each file name provided by the user
for FILE_NAME in $TEST_FILES; do

    # Increment total test count
    ((TOTAL_COUNT++))

    echo -e "\n--- Running test: **$FILE_NAME** ---"

    # Define the full path for the .wat and .wasm files
    WAT_FILE="test/wat/${FILE_NAME}.wat"
    WASM_FILE="test/wasm/${FILE_NAME}.wasm"

    # Check if the .wat source file exists
    if [ ! -f "$WAT_FILE" ]; then
        echo "❌ Error: Source file $WAT_FILE not found. Skipping."
        continue
    fi

    # 1. Compile the .wat file to a .wasm file
    echo "Compiling $WAT_FILE to $WASM_FILE..."
    # The command: wat2wasm test/wat/blue5.wat -o test/wasm/blue5.wasm
    if wat2wasm "$WAT_FILE" -o "$WASM_FILE"; then
        echo "✅ Compilation successful."

        # 2. Run the compiled .wasm file with wasm-interp
        echo "Interpreting and running exports..."
        # The command: wasm-interp test/wasm/blue5.wasm --run-all-exports
        if wasm-interp "$WASM_FILE" --run-all-exports; then
            echo "✅ Test **$FILE_NAME** completed successfully."
            ((SUCCESS_COUNT++))
        else
            echo "❌ Test **$FILE_NAME** failed during interpretation (wasm-interp returned an error)."
        fi

    else
        echo "❌ Compilation failed for **$FILE_NAME** (wat2wasm returned an error)."
    fi

done

# --- Summary ---
echo -e "\n=============================="
echo "📝 **Test Summary**"
echo "Tests Run: **$TOTAL_COUNT**"
echo "Tests Passed: **$SUCCESS_COUNT**"
echo "Tests Failed: **$((TOTAL_COUNT - SUCCESS_COUNT))**"
echo "=============================="