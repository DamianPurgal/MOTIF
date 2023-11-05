package com.deemor.motif.helpRequest.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HelpRequestPage {
    private List<HelpRequestDto> requests;
    private Integer totalNumberOfPages;
    private Integer totalNumberOfElements;
}