package code.with.vanilson.produtor;

/**
 * ProducerProperty
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-17
 */
@SuppressWarnings("unused")
record ProducerProperty(String key, String value) {
    public ProducerProperty {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Value cannot be null or empty");
        }
    }
}