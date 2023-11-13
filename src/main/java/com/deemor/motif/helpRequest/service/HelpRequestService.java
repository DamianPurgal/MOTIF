package com.deemor.motif.helpRequest.service;

import com.deemor.motif.alert.service.AlertService;
import com.deemor.motif.helpRequest.dto.HelpRequestAddDto;
import com.deemor.motif.helpRequest.dto.HelpRequestDto;
import com.deemor.motif.helpRequest.dto.HelpRequestEditDto;
import com.deemor.motif.helpRequest.dto.HelpRequestPage;
import com.deemor.motif.helpRequest.entity.HelpRequest;
import com.deemor.motif.helpRequest.entity.HelpRequestStatus;
import com.deemor.motif.helpRequest.exception.HelpRequestNotFoundException;
import com.deemor.motif.helpRequest.mapper.HelpRequestMapper;
import com.deemor.motif.helpRequest.repository.HelpRequestRepository;
import com.deemor.motif.user.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;
    private final HelpRequestMapper helpRequestMapper;
    private final AppUserService appUserService;
    private final AlertService alertService;

    public HelpRequestDto addHelpRequest(HelpRequestAddDto helpRequestAddDto) {
        HelpRequest helpRequest = helpRequestMapper.mapAddDtoToEntity(helpRequestAddDto);
        helpRequest.setRequester(appUserService.getLoggedUser());
        helpRequest.setDate(LocalDateTime.now());
        helpRequest.setStatus(HelpRequestStatus.NEW);

        return helpRequestMapper.mapEntityToDto(helpRequestRepository.save(helpRequest));
    }

    @Transactional
    public HelpRequestDto editHelpRequest(Long helpRequestId, HelpRequestEditDto helpRequestEditDto) {
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId).orElseThrow(HelpRequestNotFoundException::new);

        helpRequest.setResponse(helpRequestEditDto.getResponse());
        helpRequest.setStatus(helpRequestEditDto.getStatus());

        HelpRequestDto savedHelpRequest = helpRequestMapper.mapEntityToDto(helpRequestRepository.save(helpRequest));

        alertService.addBasicAlertToUser(
                "Zaktualizowano status zgłoszenia #" + savedHelpRequest.getId(),
                "Status zgłoszenia został zaktualizowany. Przejdź do zakładki 'Zgłoszenia' aby zobaczyć odpowiedź.",
                savedHelpRequest.getRequester()
        );

        return savedHelpRequest;
    }

    public HelpRequestPage getHelpRequestsOfUserPageable(Integer pageNumber, Integer itemsPerPage) {
        itemsPerPage = itemsPerPage > 10 ? 10 : itemsPerPage;

        Page<HelpRequest> page = helpRequestRepository.findAllByRequester(
                appUserService.getLoggedUser(),
                PageRequest.of(pageNumber, itemsPerPage, Sort.by("id").descending())
        );

        HelpRequestPage result = new HelpRequestPage();
        result.setRequests(helpRequestMapper.mapEntityToDto(page.getContent()));
        result.setTotalNumberOfPages(page.getTotalPages());
        result.setTotalNumberOfElements(Long.valueOf(page.getTotalElements()).intValue());

        return result;
    }

    public HelpRequestPage getHelpRequestsAdminPageable(Integer pageNumber, Integer itemsPerPage) {
        itemsPerPage = itemsPerPage > 10 ? 10 : itemsPerPage;

        Page<HelpRequest> page = helpRequestRepository.findAllByStatusIn(
                List.of(HelpRequestStatus.NEW, HelpRequestStatus.CLOSED, HelpRequestStatus.OPEN),
                PageRequest.of(pageNumber, itemsPerPage, Sort.by("id").descending())
        );

        HelpRequestPage result = new HelpRequestPage();
        result.setRequests(helpRequestMapper.mapEntityToDto(page.getContent()));
        result.setTotalNumberOfPages(page.getTotalPages());
        result.setTotalNumberOfElements(Long.valueOf(page.getTotalElements()).intValue());

        return result;
    }

}
