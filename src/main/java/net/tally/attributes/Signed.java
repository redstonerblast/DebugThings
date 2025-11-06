package net.tally.attributes;

public record Signed<T>(Sign sign, T value) {
}