# mailroom-parent

Shared Maven parent POM for the mailroom email libraries: Java 8 target, test-JDK switch and release settings.

Every mailroom module inherits it: `mailroom-core`, `mailroom-http`, `mailroom-test`, `mailroom-tck` and the transport
modules such as `mailroom-smtp` and `mailroom-smtp2go`. It holds no code.

## What it sets

- **Java 8 bytecode** (`maven.compiler.release=8`), so one set of jars runs on Java 8, 21 and 25. The build itself runs
  on JDK 21.
- **Pinned plugin versions** and the **JUnit 5 and AssertJ 3** versions every repo uses.
- **`Automatic-Module-Name`** in each jar's manifest, taken from the module's `module.name` property. A module with
  Java sources must set it.
- **Enforcer rules:**
  - Maven 3.9 or newer and a JDK 21 or newer build.
  - Upper-bound dependencies, so a module can't quietly resolve an older `mailroom-core`.
  - Bans on library lines that need newer than Java 8: JUnit 6, Mockito 5, AssertJ 4 and Jackson 3 (`tools.jackson`).
  - Runtime dependencies must be Java 8 bytecode.
- **`-Dtest.jdk=8|21|25`**: runs Surefire and Failsafe on that JDK through Maven toolchains, while Maven stays on 21.
  On 21 it also turns on `-Djdk.tracePinnedThreads=full`.
- **`release` profile**: attaches sources and Javadoc jars.
- **Deploys** to `https://repo.primelogiclab.io`: releases to `releases`, snapshots to `snapshots`.

## Using it

Maven reads the parent before anything the parent declares, so a child needs the repository entries itself:

```xml
<parent>
    <groupId>io.primelogiclab.mailroom</groupId>
    <artifactId>mailroom-parent</artifactId>
    <version>0.1.0</version>
    <relativePath/>
</parent>

<properties>
    <module.name>io.primelogiclab.mailroom.core</module.name>
</properties>

<repositories>
    <repository>
        <id>primelogiclab-releases</id>
        <url>https://repo.primelogiclab.io/releases</url>
        <snapshots><enabled>false</enabled></snapshots>
    </repository>
    <repository>
        <id>primelogiclab-snapshots</id>
        <url>https://repo.primelogiclab.io/snapshots</url>
        <releases><enabled>false</enabled></releases>
        <snapshots><enabled>true</enabled></snapshots>
    </repository>
</repositories>
```

Both repositories can be read without signing in.

## Running the tests on another JDK

List your JDKs in `~/.m2/toolchains.xml`, with the versions written exactly as `8`, `21` and `25`:

```xml
<toolchains>
    <toolchain>
        <type>jdk</type>
        <provides><version>8</version></provides>
        <configuration><jdkHome>/path/to/jdk8</jdkHome></configuration>
    </toolchain>
    <!-- the same for 21 and 25 -->
</toolchains>
```

Then:

```
./mvnw verify -Dtest.jdk=8
```

Without `-Dtest.jdk`, the tests run on the JDK that runs Maven. In GitHub Actions, `actions/setup-java` with several
`java-version` lines writes the same `toolchains.xml` entries.

## Workflow templates

`templates/ci.yml` and `templates/release.yml` are the GitHub Actions workflows each mailroom repo copies into
`.github/workflows/`:

- **ci.yml** tests on JDK 8, 21 and 25 on every push and pull request. A `build-ok` job is the one required check. A
  push to `main` deploys a snapshot.
- **release.yml** runs on a tag `vX.Y.Z`. It checks that the tag matches the POM version without `-SNAPSHOT`, sets that
  version, and deploys with `-P release`.

Both need the `REPO_TOKEN_NAME` and `REPO_TOKEN_SECRET` repository secrets, a Reposilite access token that can write
to the repositories.

## Building this repo

```
./mvnw install
./mvnw -f src/it/smoke/pom.xml verify -Dtest.jdk=8
```

`src/it/smoke` is a small child project used to check the parent's settings. It is never deployed.

## Compatibility

| mailroom-parent | Build with | Tests run on | Produces |
| --- | --- | --- | --- |
| 0.1.x | Maven 3.9+, JDK 21+ | JDK 8, 21, 25 | Java 8 bytecode |

## License

[Apache License 2.0](LICENSE)
