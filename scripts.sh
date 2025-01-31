#!/bin/bash

# Extract TODO lines from git diff
TODOS=$(git diff HEAD^ HEAD | egrep -i "(//|#)\s*todo" | sed 's/.*todo.*://gi')

# Check if there are any TODOs
if [ -z "$TODOS" ]; then
  echo "No TODOs found in git diff."
  exit 0
fi

# Loop through each TODO line and create an issue
echo "$TODOS" | while IFS= read -r TODO; do
  # Trim leading/trailing whitespace
  TODO=$(echo "$TODO" | xargs)

  # Check if an issue with the same title already exists
  EXISTING_ISSUE=$(gh issue list --search "in:title $TODO" --json title --jq '.[].title')

  if [ -z "$EXISTING_ISSUE" ]; then
    # Create an issue using the GitHub CLI
    echo "Creating issue for TODO: $TODO"
    gh issue create --title "$TODO" --body "This issue was automatically created from a TODO in the code."
  else
    echo "Issue already exists for TODO: $TODO"
  fi
done
