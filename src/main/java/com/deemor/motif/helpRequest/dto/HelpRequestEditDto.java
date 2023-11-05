package com.deemor.motif.helpRequest.dto;

import com.deemor.motif.helpRequest.entity.HelpRequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HelpRequestEditDto {
    private String response;
    private HelpRequestStatus status;
}
