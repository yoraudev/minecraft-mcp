# Contributing

Thank you for your interest in contributing.

## Before You Start

1. Read `README.md`, `LICENSE`, `SECURITY.md`, and this file.
2. Discuss major changes in an issue before implementation.
3. Keep pull requests focused on one topic.

## Local Setup

1. Install Java 21 and Gradle.
2. Build locally with:

```bash
gradle build
```

## Coding Rules

1. Do not use comment lines in code.
2. Avoid unnecessary code duplication.

## Commit Rules

1. Use Conventional Commits format: `<type>(<scope>): <subject>`.
2. Allowed types: `feat`, `fix`, `chore`, `docs`, `refactor`, `test`, `ci`, `build`, `perf`, `style`, `revert`.
3. Write all commit messages in English.
4. Write subjects in imperative mood and do not end with a period.
5. Keep the subject line at 72 characters or fewer when possible.
6. Split unrelated changes into separate commits.

## Pull Request Rules

1. Keep each pull request scoped to one logical change.
2. Ensure build and relevant checks pass before requesting review.
3. Explain what changed, why it changed, and how it was tested.
4. Link related issues when available.
5. Mark breaking changes clearly.

## Review and Merge

1. Address review feedback with additional commits.
2. Keep discussion respectful and technical.
3. Maintainers decide final merge timing and strategy.
