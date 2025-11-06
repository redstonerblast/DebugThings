package net.tally.attributes;

public enum Sign {
    POSITIVE,
    NEGATIVE;

    public <T> Signed<T> wrap(T value) {
        return new Signed<>(this, value);
    }
}
