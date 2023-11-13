package com.deemor.motif.notification.service;

import com.deemor.motif.alert.entity.Alert;
import com.deemor.motif.alert.mapper.AlertMapper;
import com.deemor.motif.websocket.WebsocketPath;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AlertMapper alertMapper;

    public void sendNotificationToUser(Alert alert, String username) {
        simpMessagingTemplate.convertAndSendToUser(
                username,
                WebsocketPath.NEW_ALERT_SPECIFIC_USER.getPath(),
                alertMapper.mapDtoToModelApi(alertMapper.mapEntityToDto(alert))
        );
    }

}
