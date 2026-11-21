package io.primelogiclab.mailroom.smoke;

/**
 * Reports which Java runtime the code is running on.
 *
 * @since 0.1.0
 */
public final class Smoke {

    private Smoke() {
    }

    /**
     * Returns the running JVM's feature version: 8 for Java 8 (whose specification version is "1.8"), otherwise the
     * specification version itself, such as 21 or 25.
     *
     * @return the Java feature version
     */
    public static int javaFeatureVersion() {
        String spec = System.getProperty("java.specification.version");
        return Integer.parseInt(spec.startsWith("1.") ? spec.substring(2) : spec);
    }
}
