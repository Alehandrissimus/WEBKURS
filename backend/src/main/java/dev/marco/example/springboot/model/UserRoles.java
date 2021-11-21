package dev.marco.example.springboot.model;

import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public enum UserRoles {
    ADMIN,
    USER,
    UNVERIFIED;

    public static UserRoles convertFromIntToRole(int number) {
        switch (number) {
            case 0:
                return ADMIN;
            case 1:
                return USER;
            default:
                return UNVERIFIED;
        }
    }

    public static List<SimpleGrantedAuthority> convertFromRoleToStr(UserRoles userRole) {
        String user = "ROLE_USER";
        String admin = "ROLE_ADMIN";
        String unverified = "ROLE_UNVERIFIED";
        switch (userRole) {
            case ADMIN:
                ArrayList<SimpleGrantedAuthority> roles = new ArrayList<>();
                roles.add(new SimpleGrantedAuthority(admin));
                roles.add(new SimpleGrantedAuthority(user));
                return roles;
            case USER:
                ArrayList<SimpleGrantedAuthority> roles1 = new ArrayList<>();
                roles1.add(new SimpleGrantedAuthority(user));
                return roles1;
            case UNVERIFIED:
                ArrayList<SimpleGrantedAuthority> roles2 = new ArrayList<>();
                roles2.add(new SimpleGrantedAuthority(unverified));
                return roles2;
            default:
                return Collections.emptyList();
        }
    }
}
