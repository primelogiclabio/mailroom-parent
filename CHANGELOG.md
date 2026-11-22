# Changelog

All notable changes to mailroom-parent are listed here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and versions follow [Semantic Versioning](https://semver.org/).

## [Unreleased]

## [0.2.0] - 2026-11-22

### Changed

- Two Java sets: modules compile for Java 21 by default, and for Java 8 when a `java8-set` file sits next to their
  `pom.xml`. Each set may depend only on its own mailroom artifacts (`*-java8` for the Java 8 set); the Java 8 bytecode
  check now applies to the Java 8 set only; with `-Dtest.jdk=8` the Java 21 set's tests are skipped. The smoke
  project has one module per set.
- The ci.yml template runs the release build (`-P release`, sources and Javadoc jars, tests skipped) on every push
  and pull request, and `build-ok` needs it, so a Javadoc error fails CI instead of the release after the tag.

## [0.1.0] - 2026-11-21

### Added

- Parent POM for the mailroom libraries: Java 8 bytecode (`--release 8`), pinned plugin versions, JUnit 5 and AssertJ 3
  versions, `Automatic-Module-Name` from `module.name`.
- Enforcer rules: Maven 3.9+, build JDK 21+, upper-bound dependencies, bans on Java 17-only lines (JUnit 6,
  Mockito 5, AssertJ 4, Jackson 3), Java 8 bytecode for runtime dependencies, `module.name` required for Java modules.
- `-Dtest.jdk=8|21|25` runs the tests on that JDK through Maven toolchains; on 21 with the virtual-thread pinning trace.
- `release` profile with sources and Javadoc jars; deploys to `repo.primelogiclab.io`.
- CI and release workflow templates for the mailroom repos.

[Unreleased]: https://github.com/primelogiclabio/mailroom-parent/compare/v0.2.0...HEAD
[0.2.0]: https://github.com/primelogiclabio/mailroom-parent/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/primelogiclabio/mailroom-parent/tree/v0.1.0
