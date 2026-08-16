# Upgrade Plan: exhibition-registration (20260806131703)

- **Generated**: 2026-08-06 13:20:00
- **HEAD Branch**: main
- **HEAD Commit ID**: N/A

## Available Tools

**JDKs**
- JDK 21.0.11: /usr/lib/jvm/java-21-openjdk-amd64/bin (current project JDK, used by steps 1-3)

**Build Tools**
- Maven: /usr/share/maven/bin
- Maven Wrapper: not present

## Guidelines

> Note: You can add any specific guidelines or constraints for the upgrade process here if needed, bullet points are preferred.

## Options

- Working branch: appmod/java-upgrade-20260806131703
- Run tests before and after the upgrade: true

## Upgrade Goals

- Upgrade Java runtime target to latest LTS (Java 21)

## Technology Stack

| Technology/Dependency | Current | Min Compatible | Why Incompatible |
| --------------------- | ------- | -------------- | ---------------- | 
| Java | 21 | 21 | User requested target is Java 21 latest LTS |
| Maven | /usr/share/maven/bin | 3.9+ recommended | Build tool available and compatible with Java 21 |
| maven-compiler-plugin | 3.11.0 | 3.11.0 | Already sufficient for Java 21 |
| sqlite-jdbc | 3.45.1.0 | 3.45.1.0 | JDBC dependency does not block Java 21 |

## Derived Upgrades

- None required. The current project configuration already targets Java 21 and uses compatible build tooling.

## Impact Analysis

#### Dependency Changes

| File | Dependency | Current | Action | Target | Reason |
|------|------------|---------|--------|--------|--------|
| pom.xml | org.apache.maven.plugins:maven-surefire-plugin | (default Maven 3.6.3 managed, 2.12.4) | add | 3.1.2 | Ensure Maven can resolve and run the test phase with modern Surefire plugin support on Java 21 |

#### Source Code Changes

No source code changes are required solely for the Java 21 runtime target. The project uses only standard Java SE APIs and Swing, which are compatible with Java 21.

#### Configuration Changes

No configuration file changes are required.

#### CI/CD Changes

No CI/CD file updates are required at this time.

#### Risks & Warnings

- **Project already targets Java 21**: The main risk is assuming the current `pom.xml` and developer environment are fully aligned. **Mitigation**: verify with a clean Maven build and full test run on JDK 21.
- **Stale target artifacts**: Existing contents in `target/` may mask issues during build validation. **Mitigation**: use `mvn clean` for all verification commands.

## Upgrade Steps

- Step 1: Setup Environment
  - **Rationale**: Confirm Java 21 is available and the current build environment can run the target runtime.
  - **Changes to Make**: None in source files; verify available JDK and Maven.
  - **Verification**: `mvn -version` using JDK 21 and `java -version`.

- Step 2: Baseline Validation
  - **Rationale**: Validate the current project state and confirm the existing Java 21 runtime target is already satisfied.
  - **Changes to Make**: None.
  - **Verification**: `mvn clean test-compile -q && mvn clean test -q` on JDK 21.

- Step 3: Final Validation
  - **Rationale**: Confirm no upgrade changes are needed and verify the project compiles and all tests pass on Java 21.
  - **Changes to Make**: None.
  - **Verification**: `mvn clean test -q` on JDK 21.
