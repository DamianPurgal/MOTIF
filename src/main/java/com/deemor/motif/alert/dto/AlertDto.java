package com.deemor.motif.alert.dto;

import com.deemor.motif.alert.entity.AlertStyle;
import com.deemor.motif.alert.entity.AlertType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AlertDto {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime date;
    private Long priority;
    private AlertType type;
    private AlertStyle style;
    private boolean seen;
    private String user;

}
