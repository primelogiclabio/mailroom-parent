package io.primelogiclab.mailroom.smoke;

/**
 * Reports which Java runtime the code is running on, using Java 21 language features to prove the module compiles for
 * Java 21.
 *
 * @since 0.2.0
 */
public final class Smoke {

    /**
     * A Java runtime, by feature version.
     *
     * @param feature the feature version, such as 21 or 25
     * @since 0.2.0
     */
    public record Runtime(int feature) {
    }

    private Smoke() {
    }

    /**
     * Returns the running JVM's feature version.
     *
     * @return the Java feature version, such as 21 or 25
     * @since 0.2.0
     */
    public static int javaFeatureVersion() {
        Object runtime = new Runtime(java.lang.Runtime.version().feature());
        return switch (runtime) {
            case Runtime(int feature) -> feature;
            default -> throw new IllegalStateException("not a runtime");
        };
    }
}
