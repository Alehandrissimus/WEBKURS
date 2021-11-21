package dev.marco.example.springboot.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final String USER = "USER";
    private final String ADMIN = "ADMIN";
    private final String GET_USERNAME_QUERY = "SELECT email, passwd, isActive from USR where email = ?";
    private final String GET_AUTHORITY_QUERY = "SELECT U.email, R.usr_role FROM USR U, USR_ROLES R where U.email = ? AND U.USR_ROLE = R.ID_USR_ROLE";

    private final JwtTokenProvider jwtTokenProvider;
    private final DataSource dataSource;

    @Autowired
    public WebSecurityConfigurer(JwtTokenProvider jwtTokenProvider, DataSource dataSource) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.dataSource = dataSource;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(GET_USERNAME_QUERY)
                .authoritiesByUsernameQuery(GET_AUTHORITY_QUERY);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers("/user/**").hasRole(USER)
                    .antMatchers("/users").hasRole(USER)
                    .antMatchers("/quiz/**").hasRole(USER)
                    .antMatchers("/announcement/**").hasRole(USER)
                    .antMatchers("/updatePassword").hasRole(USER)
                    .antMatchers("/dashboard/**").hasRole(USER)
                    .antMatchers("/toAdmin").hasRole(ADMIN)
                    .antMatchers("/admin/**").hasRole(ADMIN)
                    .antMatchers("/confirm").permitAll()
                    .antMatchers("/auth/**").permitAll()
                    .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
