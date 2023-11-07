package com.deemor.motif.alert.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertPage {
    private List<AlertDto> alerts;
    private Integer totalNumberOfPages;
    private Integer totalNumberOfElements;
}