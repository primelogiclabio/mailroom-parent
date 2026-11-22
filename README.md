# mailroom-parent

Shared Maven parent POM for the mailroom email libraries: the Java 21 and Java 8 sets, test-JDK switch and release settings.

Every mailroom module inherits it: `mailroom-core`, `mailroom-http`, `mailroom-test`, `mailroom-tck` and the transport
modules such as `mailroom-smtp` and `mailroom-smtp2go`. It holds no code.

## What it sets

- **Two Java sets** (see below): Java 21 bytecode by default, Java 8 bytecode for modules marked as the Java 8 set.
  The build itself runs on JDK 21.
- **Pinned plugin versions** and the **JUnit 5 and AssertJ 3** versions every repo uses.
- **`Automatic-Module-Name`** in each jar's manifest, taken from the module's `module.name` property. A module with
  Java sources must set it.
- **Enforcer rules:**
  - Maven 3.9 or newer and a JDK 21 or newer build.
  - Upper-bound dependencies, so a module can't quietly resolve an older `mailroom-core`.
  - Bans on library lines that need Java 17+: JUnit 6, Mockito 5, AssertJ 4 and Jackson 3 (`tools.jackson`). Both
    sets keep them while they share their API and tests.
  - A module depends only on mailroom artifacts of its own set.
  - In the Java 8 set, runtime dependencies must be Java 8 bytecode.
- **`-Dtest.jdk=8|21|25`**: runs Surefire and Failsafe on that JDK through Maven toolchains, while Maven stays on 21.
  On 21 it also turns on `-Djdk.tracePinnedThreads=full`. Each set is tested only on its own JDKs: on 8 the Java 21
  set's tests are skipped, and on 21 and 25 the Java 8 set's.
- **`release` profile**: attaches sources and Javadoc jars.
- **Deploys** to `https://repo.primelogiclab.io`: releases to `releases`, snapshots to `snapshots`.

## Two Java sets

Each mailroom module comes twice, with the same version and packages: an unsuffixed artifact compiled for Java 21
(`mailroom-core`) and a `-java8` twin compiled for Java 8 (`mailroom-core-java8`). A module joins the Java 8 set by
having a file named `java8-set` next to its `pom.xml`; its content doesn't matter (a comment line saying why is
enough). Maven profiles can't read a module's own properties but can check for files, which is why it's a file.

| | Java 21 set (no `java8-set` file) | Java 8 set (`java8-set` file) |
| --- | --- | --- |
| `maven.compiler.release` | 21 | 8 |
| Tests run on | Maven's JDK, or `-Dtest.jdk=21` / `25`; skipped with `-Dtest.jdk=8` | JDK 8 only (its toolchain, with or without `-Dtest.jdk=8`); skipped with `-Dtest.jdk=21` / `25` |
| mailroom dependencies | unsuffixed only; `*-java8` is banned | `*-java8` only |
| Bytecode check on runtime dependencies | none needed: a JDK 21 build can't read newer class files | Java 8 |

The property `mailroom.java` (21 or 8) holds the module's set, for plugins that need it.

## Using it

Maven reads the parent before anything the parent declares, so a child needs the repository entries itself:

```xml
<parent>
    <groupId>io.primelogiclab.mailroom</groupId>
    <artifactId>mailroom-parent</artifactId>
    <version>0.2.0</version>
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

- **ci.yml** tests on JDK 8, 21 and 25 on every push and pull request, and runs the release build (`-P release`,
  with the sources and Javadoc jars) so a Javadoc error shows before a tag. A `build-ok` job, which needs both, is the
  one required check. A push to `main` deploys a snapshot.
- **release.yml** runs on a tag `vX.Y.Z`. It checks that the tag matches the POM version without `-SNAPSHOT`, sets that
  version, and deploys with `-P release`.
- The snapshot deploy and the release run the tests again before deploying, so they set up JDK 8 next to 21:
  from 0.3.0 the Java 8 set's tests need its toolchain.

Both need the `REPO_TOKEN_NAME` and `REPO_TOKEN_SECRET` repository secrets, a Reposilite access token that can write
to the repositories.

## Building this repo

```
./mvnw install
./mvnw -f src/it/smoke/pom.xml verify -Dtest.jdk=8
```

`src/it/smoke` is a small child project used to check the parent's settings, with one module per Java set. On
JDK 8 its Java 21 module skips its tests, and on 21 and 25 its Java 8 module does. It is never deployed.

## Compatibility

| mailroom-parent | Build with | Tests run on | Produces |
| --- | --- | --- | --- |
| 0.1.x | Maven 3.9+, JDK 21+ | JDK 8, 21, 25 | Java 8 bytecode |
| 0.2.x | Maven 3.9+, JDK 21+ | Java 21 set: JDK 21, 25; Java 8 set: JDK 8, 21, 25 | Java 21 bytecode, or Java 8 for the Java 8 set |
| 0.3.x | Maven 3.9+, JDK 21+, a JDK 8 toolchain | Java 21 set: JDK 21, 25; Java 8 set: JDK 8 | Java 21 bytecode, or Java 8 for the Java 8 set |

## License

[Apache License 2.0](LICENSE)
