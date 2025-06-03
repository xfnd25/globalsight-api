package br.com.fiap.globalsolution.globalsight_api.service;

import br.com.fiap.globalsolution.globalsight_api.dto.AuthRequestDto;
import br.com.fiap.globalsolution.globalsight_api.dto.AuthResponseDto;
import br.com.fiap.globalsolution.globalsight_api.dto.UserRegistrationDto;
import br.com.fiap.globalsolution.globalsight_api.dto.UserDetailsDto;

public interface AuthService {
    UserDetailsDto registerUser(UserRegistrationDto registrationDto);
    AuthResponseDto loginUser(AuthRequestDto authRequestDto);
}