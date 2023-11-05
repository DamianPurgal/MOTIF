package com.deemor.motif.user.service;

import com.deemor.motif.security.UserRole;
import com.deemor.motif.user.dto.AppUserDto;
import com.deemor.motif.user.dto.EditAppUserDto;
import com.deemor.motif.user.dto.RegisterAppUserDto;
import com.deemor.motif.user.entity.AppUser;
import com.deemor.motif.user.exception.UserPasswordsDoesntMatchException;
import com.deemor.motif.user.exception.UserUsernameNotAvaliableException;
import com.deemor.motif.user.mapper.AppUserMapper;
import com.deemor.motif.user.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.deemor.motif.security.util.AuthUtil.getLoggedUserUsername;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {


    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppUserMapper appUserMapper;

    @Transactional
    public AppUserDto registerUser(RegisterAppUserDto user) {
        user.setUsername(user.getUsername().toLowerCase());
        if (isExistingUser(user.getUsername())) {
            throw new UserUsernameNotAvaliableException();
        }

        AppUser userToRegister = appUserMapper.mapRegisterAppUserDtoToAppuser(user);

        userToRegister.setPassword(passwordEncoder.encode(user.getPassword()));
        userToRegister.setUserRole(UserRole.BASIC_USER);
        userToRegister.setLocked(false);
        userToRegister.setEnabled(true);

        AppUser registeredUser = appUserRepository.save(userToRegister);

        return appUserMapper.mapAppUserToAppUserDTO(
                registeredUser
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username.toLowerCase())
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User %s not found", username))
                );
    }

    public AppUserDto getUserByUsername(String username) {
        return appUserMapper.mapAppUserToAppUserDTO(
                appUserRepository.findByUsername(username.toLowerCase())
                        .orElseThrow(
                                () -> new UsernameNotFoundException(String.format("User %s not found", username))
                        )
        );
    }

    public List<AppUserDto> getAllUsers() {
        return appUserMapper.mapAppUserToAppUserDTO(appUserRepository.findAll());
    }

    @Transactional
    public AppUserDto updateUserByUsername(EditAppUserDto editAppUserDTO) {
        AppUser user = appUserRepository.findByUsername(editAppUserDTO.getUsername().toLowerCase())
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User %s not found", editAppUserDTO.getUsername()))
                );
        if(!passwordEncoder.matches(editAppUserDTO.getOldPassword(), user.getPassword())){
            throw new UserPasswordsDoesntMatchException();
        }

        if (isExistingUser(editAppUserDTO.getUsername().toLowerCase())) {
            throw new UserUsernameNotAvaliableException();
        }

        user.setUsername(editAppUserDTO.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(editAppUserDTO.getNewPassword()));

        return appUserMapper.mapAppUserToAppUserDTO(appUserRepository.save(user));
    }


    @Transactional
    public void deleteUserByUsername(String username) {
        AppUser user = appUserRepository.findByUsername(username.toLowerCase())
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User %s not found", username))
                );

        appUserRepository.delete(user);
    }

    public AppUser getLoggedUser() {
        return appUserRepository.findByUsername(getLoggedUserUsername().toLowerCase())
                .orElseThrow(
                        () -> new UsernameNotFoundException("Logged user not found. Authentication error")
                );
    }

    private boolean isExistingUser(String username){
        return appUserRepository.findByUsername(username.toLowerCase()).isPresent();
    }

}
