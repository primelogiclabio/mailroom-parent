package io.primelogiclab.mailroom.smoke;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

class SmokeTest {

    /**
     * The Java 8 set is tested on JDK 8 only: the parent runs these tests on the JDK 8 toolchain with or without
     * -Dtest.jdk=8, and skips them with -Dtest.jdk=21 or 25.
     */
    @Test
    void runsOnJava8() {
        String expected = System.getProperty("expected.jdk", "");
        assumeTrue(expected.isEmpty() || expected.equals("8"), "the Java 8 set's tests run only on JDK 8");
        System.out.println("Tests running on Java " + System.getProperty("java.version"));
        assertThat(Smoke.javaFeatureVersion()).isEqualTo(8);
    }

    @Test
    void compiledForJava8() throws Exception {
        assertThat(System.getProperty("expected.release")).as("the java8-set file puts this module in the Java 8 set")
                .isEqualTo("8");
        try (java.io.DataInputStream in = new java.io.DataInputStream(
                Smoke.class.getResourceAsStream("Smoke.class"))) {
            in.readInt();
            in.readUnsignedShort();
            assertThat(in.readUnsignedShort()).as("class file major version").isEqualTo(52);
        }
    }
}
