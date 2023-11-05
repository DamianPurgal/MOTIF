package com.deemor.motif.user.controller;

import com.deemor.motif.api.UserApi;
import com.deemor.motif.model.UserEditModelApi;
import com.deemor.motif.model.UserModelApi;
import com.deemor.motif.model.UserRegisterModelApi;
import com.deemor.motif.user.mapper.AppUserMapper;
import com.deemor.motif.user.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AppUserController implements UserApi {

    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> deleteUser(String username) {
        appUserService.deleteUserByUsername(username);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<UserModelApi> editUser(UserEditModelApi userEditModelApi) {
        return ResponseEntity.ok().body(
                appUserMapper.mapToModelApi(appUserService.updateUserByUsername(
                        appUserMapper.mapToEditUserDto(userEditModelApi)
                        )
                )
        );
    }

    @Override
    public ResponseEntity<List<UserModelApi>> getAllUsers() {
        return ResponseEntity.ok().body(
                appUserMapper.mapToModelApi(appUserService.getAllUsers())
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<UserModelApi> registerUser(UserRegisterModelApi userRegisterModelApi) {
        return ResponseEntity.ok().body(
                appUserMapper.mapToModelApi(
                        appUserService.registerUser(appUserMapper.mapToRegisterUserDto(userRegisterModelApi))
                )
        );
    }

}
