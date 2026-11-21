# Changelog

All notable changes to mailroom-parent are listed here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and versions follow [Semantic Versioning](https://semver.org/).

## [Unreleased]

## [0.1.0] - 2026-11-21

### Added

- Parent POM for the mailroom libraries: Java 8 bytecode (`--release 8`), pinned plugin versions, JUnit 5 and AssertJ 3
  versions, `Automatic-Module-Name` from `module.name`.
- Enforcer rules: Maven 3.9+, build JDK 21+, upper-bound dependencies, bans on Java 17-only lines (JUnit 6,
  Mockito 5, AssertJ 4, Jackson 3), Java 8 bytecode for runtime dependencies, `module.name` required for Java modules.
- `-Dtest.jdk=8|21|25` runs the tests on that JDK through Maven toolchains; on 21 with the virtual-thread pinning trace.
- `release` profile with sources and Javadoc jars; deploys to `repo.primelogiclab.io`.
- CI and release workflow templates for the mailroom repos.

[Unreleased]: https://github.com/primelogiclabio/mailroom-parent/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/primelogiclabio/mailroom-parent/tree/v0.1.0
