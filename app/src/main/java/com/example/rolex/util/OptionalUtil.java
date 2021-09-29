package com.example.rolex.util;

import com.google.common.base.Optional;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public final class OptionalUtil {

    private OptionalUtil() { }

    public static Optional<Integer> parseInteger(String value) {
        return parseInteger(value, 10);
    }

    public static Optional<Integer> parseInteger(String value, int radix) {
        try {
            return Optional.of(Integer.parseInt(value, radix));
        } catch (NumberFormatException ex) {
            return Optional.absent();
        }
    }

    public static Optional<Date> parseDate(String value, DateFormat format) {
        try {
            return Optional.fromNullable(format.parse(value));
        } catch (ParseException ex) {
            return Optional.absent();
        }
    }
}
