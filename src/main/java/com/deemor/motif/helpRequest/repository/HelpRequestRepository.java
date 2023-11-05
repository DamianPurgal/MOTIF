package com.deemor.motif.helpRequest.repository;

import com.deemor.motif.helpRequest.entity.HelpRequest;
import com.deemor.motif.helpRequest.entity.HelpRequestStatus;
import com.deemor.motif.user.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRequestRepository extends PagingAndSortingRepository<HelpRequest, Long> {

    Page<HelpRequest> findAllByRequester(AppUser requester, Pageable pageable);

    Page<HelpRequest> findAllByStatusIn(List<HelpRequestStatus> statuses, Pageable pageable);
}
