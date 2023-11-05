package com.deemor.motif.helpRequest.dto;

import com.deemor.motif.helpRequest.entity.HelpRequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class HelpRequestDto {
    private Long id;
    private String description;
    private String response;
    private LocalDateTime date;
    private String requester;
    private HelpRequestStatus status;
}
