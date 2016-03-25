package za.co.no9.app.util;

import java.util.Optional;

public class Validation {
    public static StringValidation validate(String value, String name) {
        return new StringValidation(value, Optional.of(name));
    }

    public static <T> ObjectValidation<T> validate(T value) {
        return new ObjectValidation<T>(value);
    }

    public static class StringValidation extends Validation {
        private final String value;
        private final Optional<String> name;

        public StringValidation(String value, Optional<String> name) {
            this.value = value;
            this.name = name;
        }

        public String get() {
            return value;
        }

        public StringValidation notNull() {
            if (value == null) {
                throw new NullPointerException(name.orElse("field") + " may not be null.");
            }
            return this;
        }

        public StringValidation notEmpty() {
            return minimumLength(1);
        }

        public StringValidation minimumLength(int minimumLength) {
            if (value == null || value.length() < minimumLength) {
                throw new IllegalArgumentException(name.orElse("field") + " must contain at least " + minimumLength + " character" + (minimumLength > 1 ? "s" : ""));
            }
            return this;
        }
    }

    public static class ObjectValidation<T> extends Validation {
        private final T value;

        public ObjectValidation(T value) {
            this.value = value;
        }

        public ObjectValidation<T> notNull() {
            return this;
        }

        public T get() {
            return value;
        }
    }
}
