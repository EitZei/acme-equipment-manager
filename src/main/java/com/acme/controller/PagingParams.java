package com.acme.controller;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Optional;
import java.util.OptionalInt;

@Introspected
public class PagingParams {
    @PositiveOrZero
    private OptionalInt offset;

    @Positive
    private OptionalInt limit;

    public OptionalInt getLimit() {
        return limit;
    }

    public void setLimit(OptionalInt limit) {
        this.limit = limit;
    }

    public OptionalInt getOffset() {
        return offset;
    }

    public void setOffset(OptionalInt offset) {
        this.offset = offset;
    }
}
