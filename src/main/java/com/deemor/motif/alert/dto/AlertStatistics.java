package com.deemor.motif.alert.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlertStatistics {
    private Integer unreadAlerts;
}
