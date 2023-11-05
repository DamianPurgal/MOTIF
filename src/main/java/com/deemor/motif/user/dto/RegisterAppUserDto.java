package com.deemor.motif.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAppUserDto {

    @NotBlank
    @Size(min = 4, max = 50,
            message = "Password must be between 4 and 50 characters")
    @Schema(description="username", example = "User1234")
    private String username;

    @NotBlank
    @Size(min = 5, max = 50,
            message = "Password must be between 5 and 50 characters")
    @Schema(description="Password", example = "haslo")
    private String password;

}
