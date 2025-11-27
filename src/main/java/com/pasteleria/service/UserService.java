package com.pasteleria.service;

import com.pasteleria.dto.LoginRequest;
import com.pasteleria.dto.LoginResponse;
import com.pasteleria.dto.UserResponse;
import com.pasteleria.model.Role;
import com.pasteleria.model.User;
import com.pasteleria.repository.UserRepository;
import com.pasteleria.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest loginRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // Get user details
        User user = userRepository.findByCorreo(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getCorreo(), user.getTipoUsuario().name());

        return new LoginResponse(token, user.getCorreo(), user.getNombre() + " " + user.getApellidos(),
                user.getTipoUsuario().name());
    }

    public User registerUser(User user) {
        // Check if user already exists
        if (userRepository.existsByCorreo(user.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        if (userRepository.existsByRun(user.getRun())) {
            throw new RuntimeException("El RUN ya está registrado");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role if not provided
        if (user.getTipoUsuario() == null) {
            user.setTipoUsuario(Role.ROLE_CUSTOMER);
        }

        // Set active by default
        if (user.getActivo() == null) {
            user.setActivo(true);
        }

        return userRepository.save(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToUserResponse(user);
    }

    public UserResponse getUserByCorreo(String correo) {
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToUserResponse(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setNombre(userDetails.getNombre());
        user.setApellidos(userDetails.getApellidos());
        user.setFechaNacimiento(userDetails.getFechaNacimiento());
        user.setRegionId(userDetails.getRegionId());
        user.setRegionNombre(userDetails.getRegionNombre());
        user.setComuna(userDetails.getComuna());
        user.setDireccion(userDetails.getDireccion());
        user.setAvatarUrl(userDetails.getAvatarUrl());
        user.setCodigoDescuento(userDetails.getCodigoDescuento());

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setActivo(false);
        userRepository.save(user);
    }

    private UserResponse convertToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getRun(),
                user.getDv(),
                user.getNombre(),
                user.getApellidos(),
                user.getCorreo(),
                user.getFechaNacimiento(),
                user.getCodigoDescuento(),
                user.getTipoUsuario(),
                user.getRegionId(),
                user.getRegionNombre(),
                user.getComuna(),
                user.getDireccion(),
                user.getAvatarUrl(),
                user.getActivo());
    }
}
