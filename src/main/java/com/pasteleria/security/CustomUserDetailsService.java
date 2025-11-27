package com.pasteleria.security;

import com.pasteleria.model.User;
import com.pasteleria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        if (!user.getActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + correo);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getCorreo(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getTipoUsuario().name())));
    }
}
