package com.deemor.motif.user.mapper;

import com.deemor.motif.model.UserEditModelApi;
import com.deemor.motif.model.UserModelApi;
import com.deemor.motif.model.UserRegisterModelApi;
import com.deemor.motif.user.dto.AppUserDto;
import com.deemor.motif.user.dto.EditAppUserDto;
import com.deemor.motif.user.dto.RegisterAppUserDto;
import com.deemor.motif.user.entity.AppUser;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper()
public interface AppUserMapper {

    AppUser mapRegisterAppUserDtoToAppuser(RegisterAppUserDto registerAppUserDTO);

    AppUser mapAppUserDtoToAppUser(AppUserDto appUserDTO);

    AppUserDto mapAppUserToAppUserDTO(AppUser appUser);

    List<AppUserDto> mapAppUserToAppUserDTO(List<AppUser> appUser);

    List<UserModelApi> mapToModelApi(List<AppUserDto> appUserDtos);

    UserModelApi mapToModelApi(AppUserDto appUserDto);

    EditAppUserDto mapToEditUserDto(UserEditModelApi userEditModelApi);

    RegisterAppUserDto mapToRegisterUserDto(UserRegisterModelApi userRegisterModelApi);
}
