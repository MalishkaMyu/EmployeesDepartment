package com.samsolutions.employeesdep.config;

import com.samsolutions.employeesdep.model.services.EmployeeAuthenticationService;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
//@EnableWebSecurity
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    private MyPasswordEncoder encoder;

    @Autowired
    private EmployeeAuthenticationService empAuthenticationService;

    /**
     * sessionAuthenticationStrategy: Defines the session authentication strategy. We don´t save a AuthSession. We are completely stateless in this case
     *
     * @return RegisterSessionAuthenticationStrategy
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * Registers the KeycloakAuthenticationProvider with the authentication manager.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();

        // Note the role mapping is done using the SimpleAuthorityMapper. By default,
        // Spring Security adds a prefix ‘ROLE_’ to any authority, but Keycloak’s roles
        // do not.
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());

        auth.authenticationProvider(keycloakAuthenticationProvider);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/employees/**").authenticated()
                .antMatchers("/api/admin/**").hasRole("admin")
                .antMatchers("/api/users/**").hasRole("admin")
                /*            .and()
                            .formLogin().defaultSuccessUrl("/")
                            .and()
                            .logout().logoutSuccessUrl("/")*/
                .and()
                .exceptionHandling().accessDeniedPage("/forbidden");
    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService());
    }*/

    /*// In-Memory Authentication
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        UserDetails user = User.withUsername("superadmin")
                .password(encoder.encode("1"))
                .roles("ADMIN")
                .build();
        userDetailsManager.createUser(user);
        return userDetailsManager;
    }*/

    // DaoAuthenticationProvider
    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(encoder);
        authenticationProvider.setUserDetailsService(empAuthenticationService);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(daoAuthenticationProvider())
                .build();
    }
}
