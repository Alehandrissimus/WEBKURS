package dev.marco.example.springboot.security;

import dev.marco.example.springboot.exception.DAOLogicException;
import dev.marco.example.springboot.exception.UserDoesNotExistException;
import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailService implements UserDetailsService {

    private static final Logger log = Logger.getLogger(JwtUserDetailService.class);

    private final UserService userService;

    @Autowired
    public JwtUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userService.getUserByEmail(email);
            log.debug("loadUserByUsername email: " + user.getEmail());
            if (user == null) {
                log.error("JwtUserDetailsService user == null error");
                throw new UsernameNotFoundException("User with email: " + email + " not found");
            }

            JwtUser jwtUser = JwtUserFactory.create(user);
            log.info("IN loadUserByUsername - user with email:" + jwtUser.getUsername() + " successfully loaded");
            return jwtUser;
        } catch (UserDoesNotExistException e) {
            log.error("JwtUserDetailsService UserDoesNotExistException " + e.getCause(), e);
            return null;
        } catch (DAOLogicException e) {
            log.error("JwtUserDetailsService DAOLogicException " + e.getCause(), e);
            return null;
        }
    }
}