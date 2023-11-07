package com.deemor.motif.alert.controller;

import com.deemor.motif.alert.mapper.AlertMapper;
import com.deemor.motif.alert.service.AlertService;
import com.deemor.motif.api.AlertApi;
import com.deemor.motif.model.AlertModelApi;
import com.deemor.motif.model.AlertPageModelApi;
import com.deemor.motif.model.AlertStatisticsModelApi;
import com.deemor.motif.model.RequestPageableModelApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AlertController implements AlertApi {

    private final AlertService alertService;
    private final AlertMapper alertMapper;

    @Override
    public ResponseEntity<AlertPageModelApi> getAlertsOfUserPageable(RequestPageableModelApi requestDto) {
        return ResponseEntity.ok().body(
                alertMapper.mapDtoToModelApi(alertService.getAllAlertsOfUserPageable(requestDto.getPage(), requestDto.getSize()))
        );
    }

    @Override
    public ResponseEntity<List<AlertModelApi>> getAllUnreadAlertsOfUser() {
        return ResponseEntity.ok().body(
                alertMapper.mapDtoToModelApi(alertService.getAllUnreadAlertsOfUser())
        );
    }

    @Override
    public ResponseEntity<AlertStatisticsModelApi> getAlertStatisticsOfUser() {
        return ResponseEntity.ok().body(
                alertMapper.mapDtoToModelApi(alertService.getAlertStatisticsOfUser())
        );
    }
}
