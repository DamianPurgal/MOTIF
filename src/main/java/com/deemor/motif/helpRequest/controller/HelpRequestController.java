package com.deemor.motif.helpRequest.controller;

import com.deemor.motif.api.HelpRequestApi;
import com.deemor.motif.helpRequest.mapper.HelpRequestMapper;
import com.deemor.motif.helpRequest.service.HelpRequestService;
import com.deemor.motif.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class HelpRequestController implements HelpRequestApi {

    private final HelpRequestService helpRequestService;
    private final HelpRequestMapper helpRequestMapper;

    @Override
    public ResponseEntity<HelpRequestModelApi> addHelpRequest(HelpRequestAddModelApi helpRequestAddModelApi) {
        return ResponseEntity.ok().body(
            helpRequestMapper.mapDtoToModelApi(
                    helpRequestService.addHelpRequest(helpRequestMapper.mapAddModelApiToDto(helpRequestAddModelApi))
            )
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<HelpRequestModelApi> editHelpRequest(Long helpRequestId, HelpRequestEditModelApi helpRequestEditModelApi) {
        return ResponseEntity.ok().body(
                helpRequestMapper.mapDtoToModelApi(
                        helpRequestService.editHelpRequest(helpRequestId, helpRequestMapper.mapEditModelApiToDto(helpRequestEditModelApi))
                )
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<HelpRequestPageModelApi> getHelpRequestsAdminPageable(RequestPageableModelApi requestDto) {
        return ResponseEntity.ok().body(
                helpRequestMapper.mapDtoPageToModelApiPage(
                        helpRequestService.getHelpRequestsAdminPageable(requestDto.getPage(), requestDto.getSize())
                )
        );
    }

    @Override
    public ResponseEntity<HelpRequestPageModelApi> getHelpRequestsPageable(RequestPageableModelApi requestDto) {
        return ResponseEntity.ok().body(
                helpRequestMapper.mapDtoPageToModelApiPage(
                        helpRequestService.getHelpRequestsOfUserPageable(requestDto.getPage(), requestDto.getSize())
                )
        );
    }

}
