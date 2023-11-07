package com.deemor.motif.alert.repository;

import com.deemor.motif.alert.entity.Alert;
import com.deemor.motif.user.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AlertRepository extends PagingAndSortingRepository<Alert, Long>, JpaRepository<Alert, Long> {

    List<Alert> findAllByUserAndSeen(AppUser user, boolean seen);
    Page<Alert> findAllByUser(AppUser user, Pageable pageable);
    Integer countAlertByUserAndSeen(AppUser user, boolean seen);

}
