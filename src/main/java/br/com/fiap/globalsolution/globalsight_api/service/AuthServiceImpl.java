package br.com.fiap.globalsolution.globalsight_api.service;

import br.com.fiap.globalsolution.globalsight_api.dto.AuthRequestDto;
import br.com.fiap.globalsolution.globalsight_api.dto.AuthResponseDto;
import br.com.fiap.globalsolution.globalsight_api.dto.UserDetailsDto;
import br.com.fiap.globalsolution.globalsight_api.dto.UserRegistrationDto;
import br.com.fiap.globalsolution.globalsight_api.entity.Role;
import br.com.fiap.globalsolution.globalsight_api.entity.User;
import br.com.fiap.globalsolution.globalsight_api.repository.RoleRepository;
import br.com.fiap.globalsolution.globalsight_api.repository.UserRepository;
import br.com.fiap.globalsolution.globalsight_api.security.JwtTokenProvider; // Criaremos esta classe depois
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager; // Será injetado da configuração de segurança
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder; // Será injetado
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public UserDetailsDto registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new EntityExistsException("Username '" + registrationDto.getUsername() + "' já está em uso.");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEnabled(true);

        // Atribui o papel ROLE_USER por padrão
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER"))); // Cria se não existir
        user.setRoles(Collections.singletonList(userRole));

        User savedUser = userRepository.save(user);

        return new UserDetailsDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.isEnabled(),
                savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
        );
    }

    @Override
    public AuthResponseDto loginUser(AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.getUsername(),
                        authRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(authRequestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado após autenticação bem-sucedida: " + authRequestDto.getUsername()));

        UserDetailsDto userDetailsDto = new UserDetailsDto(
                user.getId(),
                user.getUsername(),
                user.isEnabled(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList())
        );

        return new AuthResponseDto(jwt, "Bearer", userDetailsDto);
    }
}