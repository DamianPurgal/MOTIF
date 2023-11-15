package com.deemor.motif.alert.service;

import com.deemor.motif.alert.dto.AlertDto;
import com.deemor.motif.alert.dto.AlertPage;
import com.deemor.motif.alert.dto.AlertStatistics;
import com.deemor.motif.alert.entity.Alert;
import com.deemor.motif.alert.mapper.AlertMapper;
import com.deemor.motif.alert.repository.AlertRepository;
import com.deemor.motif.notification.service.NotificationService;
import com.deemor.motif.user.entity.AppUser;
import com.deemor.motif.user.exception.UserUsernameNotFoundException;
import com.deemor.motif.user.repository.AppUserRepository;
import com.deemor.motif.user.service.AppUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;
    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;
    private final NotificationService notificationService;

    @Value("${application.alert.paging.max-items-per-page}")
    private Integer maxItemsPerPage;

    @Transactional
    public List<AlertDto> getAllUnreadAlertsOfUser() {
        List<Alert> alerts = alertRepository.findAllByUserAndSeen(appUserService.getLoggedUser(), false);
        alerts.forEach(alert -> alert.setSeen(true));

        return alertMapper.mapEntityToDto(alertRepository.saveAll(alerts));
    }

    public AlertPage getAllAlertsOfUserPageable(Integer pageNumber, Integer itemsPerPage) {
        itemsPerPage = itemsPerPage > maxItemsPerPage ? maxItemsPerPage : itemsPerPage;

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

        List<Alert> savedAlerts = alertRepository.saveAll(alerts);

        savedAlerts.forEach(savedAlert -> notificationService.sendNotificationToUser(savedAlert, savedAlert.getUser().getUsername()));

        return alertMapper.mapEntityToDto(savedAlerts);
    }

    @Transactional
    public AlertDto addAlertToLoggedUser(AlertDto alertDto) {
        Alert alert = alertMapper.mapDtoToAlert(alertDto);
        alert.setUser(appUserService.getLoggedUser());

        Alert savedAlert = alertRepository.save(alert);

        notificationService.sendNotificationToUser(savedAlert, alert.getUser().getUsername());

        return alertMapper.mapEntityToDto(savedAlert);
    }

    public AlertDto addBasicAlertToUser(String title, String description, String user) {
        return addBasicAlertToUser(title, description, appUserRepository.findByUsername(user).orElseThrow(UserUsernameNotFoundException::new));
    }

    public AlertDto addBasicAlertToUser(String title, String description, AppUser user) {
        Alert alert = Alert.getBasicAlertTemplate();
        alert.setTitle(title);
        alert.setDescription(description);
        alert.setUser(user);

        Alert savedAlert = alertRepository.save(alert);

        notificationService.sendNotificationToUser(savedAlert, user.getUsername());

        return alertMapper.mapEntityToDto(savedAlert);
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
