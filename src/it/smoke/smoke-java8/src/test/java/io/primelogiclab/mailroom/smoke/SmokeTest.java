package io.primelogiclab.mailroom.smoke;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

class SmokeTest {

    /** With -Dtest.jdk=N the parent's test-jdk profile must run this test on JDK N, not on Maven's own JDK. */
    @Test
    void runsOnTheRequestedJdk() {
        String expected = System.getProperty("expected.jdk", "");
        assumeTrue(expected.matches("[0-9]+"), "run with -Dtest.jdk=<8|21|25> to check the JDK switch");
        System.out.println("Tests running on Java " + System.getProperty("java.version"));
        assertThat(Smoke.javaFeatureVersion()).isEqualTo(Integer.parseInt(expected));
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
