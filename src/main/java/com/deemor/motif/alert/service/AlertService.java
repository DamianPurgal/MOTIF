package com.deemor.motif.alert.service;

import com.deemor.motif.alert.dto.AlertDto;
import com.deemor.motif.alert.dto.AlertPage;
import com.deemor.motif.alert.dto.AlertStatistics;
import com.deemor.motif.alert.entity.Alert;
import com.deemor.motif.alert.entity.AlertPriority;
import com.deemor.motif.alert.entity.AlertStyle;
import com.deemor.motif.alert.entity.AlertType;
import com.deemor.motif.alert.mapper.AlertMapper;
import com.deemor.motif.alert.repository.AlertRepository;
import com.deemor.motif.user.entity.AppUser;
import com.deemor.motif.user.repository.AppUserRepository;
import com.deemor.motif.user.service.AppUserService;
import com.deemor.motif.websocket.WebsocketPath;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;
    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public List<AlertDto> getAllUnreadAlertsOfUser() {
        List<Alert> alerts = alertRepository.findAllByUserAndSeen(appUserService.getLoggedUser(), false);
        alerts.forEach(alert -> alert.setSeen(true));

        return alertMapper.mapEntityToDto(alertRepository.saveAll(alerts));
    }

    public AlertPage getAllAlertsOfUserPageable(Integer pageNumber, Integer itemsPerPage) {
        itemsPerPage = itemsPerPage > 10 ? 10 : itemsPerPage;

        Page<Alert> page = alertRepository.findAllByUser(
                appUserService.getLoggedUser(),
                PageRequest.of(pageNumber, itemsPerPage, Sort.by("priority", "id").descending())
        );

        AlertPage result = new AlertPage();
        result.setAlerts(alertMapper.mapEntityToDto(page.getContent()));
        result.setTotalNumberOfPages(page.getTotalPages());
        result.setTotalNumberOfElements(Long.valueOf(page.getTotalElements()).intValue());

        return result;
    }

    @Transactional
    public List<AlertDto> addAlertToAllUsers(AlertDto alertDto) {
        List<AppUser> users = appUserRepository.findAll();
        Alert alert = alertMapper.mapDtoToAlert(alertDto);
        List<Alert> alerts = new ArrayList<>();

        users.forEach(user -> alerts.add(
                alert.toBuilder().user(user).build())
        );

        List<AlertDto> savedAlerts = alertMapper.mapEntityToDto(alertRepository.saveAll(alerts));

        savedAlerts.forEach(
                savedAlert -> simpMessagingTemplate.convertAndSendToUser(
                        savedAlert.getUser(),
                        WebsocketPath.NEW_ALERT_SPECIFIC_USER.getPath(),
                        alertMapper.mapDtoToModelApi(savedAlert)
                )
        );

        return savedAlerts;
    }

    @Transactional
    public AlertDto addAlertToLoggedUser(AlertDto alertDto) {
        Alert alert = alertMapper.mapDtoToAlert(alertDto);
        alert.setUser(appUserService.getLoggedUser());

        AlertDto savedAlert = alertMapper.mapEntityToDto(alertRepository.save(alert));

        simpMessagingTemplate.convertAndSendToUser(
                savedAlert.getUser(),
                WebsocketPath.NEW_ALERT_SPECIFIC_USER.getPath(),
                alertMapper.mapDtoToModelApi(savedAlert)
        );

        return savedAlert;
    }

    public AlertStatistics getAlertStatisticsOfUser() {
        return AlertStatistics.builder()
                .unreadAlerts(alertRepository.countAlertByUserAndSeen(appUserService.getLoggedUser(), false))
                .build();
    }


    //Method for testing
//    @Scheduled(fixedDelay = 2000)
//    public void addAlertToUser() {
//        try {
//            AppUser appUser = appUserRepository.findByUsername("admin").orElseThrow(Exception::new);
//
//            Alert alert = Alert.builder()
//                    .style(AlertStyle.BASIC)
//                    .title("Title")
//                    .type(AlertType.BASIC)
//                    .description("Description")
//                    .seen(false)
//                    .date(LocalDateTime.now())
//                    .priority(AlertPriority.BASIC.getPriority())
//                    .user(appUser)
//                    .build();
//
//            simpMessagingTemplate.convertAndSendToUser(
//                    appUser.getUsername(),
//                    WebsocketPath.NEW_ALERT_SPECIFIC_USER.getPath(),
//                    alertMapper.mapDtoToModelApi(alertMapper.mapEntityToDto(alert))
//            );
//
//            log.info("Alert send to specific User: " + appUser.getUsername());
//
//        } catch (Exception ignored) {};
//    }

}
