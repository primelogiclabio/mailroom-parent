package io.primelogiclab.mailroom.smoke;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.DataInputStream;
import org.junit.jupiter.api.Test;

class SmokeTest {

    /** With -Dtest.jdk=N the parent's test-jdk profile must run this test on JDK N, not on Maven's own JDK. */
    @Test
    void runsOnTheRequestedJdk() {
        var expected = System.getProperty("expected.jdk", "");
        assumeTrue(expected.matches("[0-9]+"), "run with -Dtest.jdk=<21|25> to check the JDK switch");
        System.out.println("Tests running on Java " + System.getProperty("java.version"));
        assertThat(Smoke.javaFeatureVersion()).isEqualTo(Integer.parseInt(expected));
    }

    /** The Java 21 set compiles for Java 21: class file major version 65. */
    @Test
    void compiledForItsSet() throws Exception {
        assertThat(System.getProperty("expected.release")).isEqualTo("21");
        try (var in = new DataInputStream(Smoke.class.getResourceAsStream("Smoke.class"))) {
            in.readInt();
            in.readUnsignedShort();
            assertThat(in.readUnsignedShort()).as("class file major version").isEqualTo(65);
        }
    }
}
