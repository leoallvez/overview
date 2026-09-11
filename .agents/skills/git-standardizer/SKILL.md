---
name: git-standardizer
description: Standardizes commit messages following the Conventional Commits pattern observed in the project's history. Use it when you are ready to commit changes.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - git
  - commit
  - conventional-commits
  - standard
  - documentation
---

## Core Workflow

- [ ] Step 1: Analyze the changes made in the current session (files modified, added, or deleted).
- [ ] Step 2: Identify the primary type of change based on the project's standards:
    - `feat`: A new feature (e.g., a new screen or use case).
    - `fix`: A bug fix.
    - `docs`: Documentation only changes (e.g., AGENTS.md, skill files).
    - `style`: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc).
    - `refactor`: A code change that neither fixes a bug nor adds a feature.
    - `test`: Adding missing tests or correcting existing tests.
    - `chore`: Changes to the build process or auxiliary tools and libraries (e.g., dependency updates, version bumps).
- [ ] Step 3: Determine if a scope is needed (optional but recommended for multi-module projects). Common scopes in this project: `(data)`, `(presentation)`, `(app)`, `(domain)`, `(all)`, `(gradle)`.
- [ ] Step 4: Write a concise description in lowercase, starting with a verb in the imperative or present tense (e.g., "add", "update", "fix").
- [ ] Step 5: Format the message as `<type>(<scope>): <description>` or `<type>: <description>`.
- [ ] Step 6: Present the proposed commit message to the user for approval.

## Guidelines

- **Conciseness:** Keep the subject line short (usually under 50-72 characters).
- **Lower Case:** Use lowercase for the type, scope, and description.
- **Consistency:** Follow the existing project patterns (e.g., `chore(all): setting X.Y.Z version`).
- **Accuracy:** The type must accurately reflect the nature of the change.

## Examples from Project History

- `feat(presentation): add skeleton screen for person details`
- `refactor(data): turning some methods internal`
- `fix(proguard): resolve build errors during R8 minification`
- `chore: update dependencies`
- `docs: creating project skills`
- `test(data): improve unit tests coverage`

## Mandatory Rules

- **Conventional Format:** You MUST use the `<type>(<scope>): <description>` pattern.
- **Lowercase:** The entire message (type, scope, description) SHOULD be in lowercase.
- **Approval:** You MUST ask for user approval before committing if you have a tool for it, or just propose the message in the chat.
- **No Punctuation:** Do not end the subject line with a period.
