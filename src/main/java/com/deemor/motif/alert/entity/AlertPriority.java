package com.deemor.motif.alert.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlertPriority {
    BASIC(100L),
    IMPORTANT(200L),
    SUPER_IMPORTANT(300L);

    private final Long priority;
}
