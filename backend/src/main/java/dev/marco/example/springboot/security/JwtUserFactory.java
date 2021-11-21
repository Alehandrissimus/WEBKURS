package dev.marco.example.springboot.security;

import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.model.UserRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId().longValue(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getUserRole()),
                user.isActive()
        );
    }

    private static List<SimpleGrantedAuthority> mapToGrantedAuthorities(UserRoles userRole) {
        return UserRoles.convertFromRoleToStr(userRole);
    }
}