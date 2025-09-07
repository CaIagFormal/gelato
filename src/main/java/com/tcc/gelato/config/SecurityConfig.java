package com.tcc.gelato.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    //private final F_Jwt f_jwt;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        //this.f_jwt = f_jwt;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Qualquer outro request é permitido a usuários autenticados

        return http
            .csrf(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request -> request
                    //.requestMatchers(HttpMethod.POST,SecurityParams.publico_post)
                    //.permitAll()
                    .requestMatchers(HttpMethod.GET,SecurityParams.publico_get)
                    .permitAll()

                    .requestMatchers(HttpMethod.POST,SecurityParams.visitante_post)
                    .anonymous()
                    .requestMatchers(HttpMethod.GET,SecurityParams.visitante_get)
                    .anonymous()

                    .requestMatchers(HttpMethod.POST,SecurityParams.cadastrados_post)
                    .authenticated()
                    .requestMatchers(HttpMethod.GET,SecurityParams.cadastrados_get)
                    .authenticated()

                    .requestMatchers(HttpMethod.POST,SecurityParams.vendedor_post)
                    .hasAuthority("VENDEDOR")
                    .requestMatchers(HttpMethod.GET,SecurityParams.vendedor_get)
                    .hasAuthority("VENDEDOR")

                    .requestMatchers(HttpMethod.POST,SecurityParams.cliente_post)
                    .hasAuthority("CLIENTE")
                    .requestMatchers(HttpMethod.GET,SecurityParams.cliente_get)
                    .hasAuthority("CLIENTE")

                    .anyRequest()
                    .denyAll())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
            //.addFilterBefore(f_jwt, UsernamePasswordAuthenticationFilter.class) // não é mais utilizado
            .exceptionHandling(customizer -> customizer
                    .accessDeniedHandler(accessDeniedHandler())
                    .authenticationEntryPoint(authenticationEntryPoint())
            )
            .authenticationProvider(authenticationProvider())
            .build();
    }

    /**
     * Redirecionamento para usuários já autenticados em caso de erro em status HTML
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/");
        return accessDeniedHandler;
    }

    /**
     * Redirecionamento para usuários não autenticados em caso de erro em status HTML
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            if (response.isCommitted()) {
                return;
            }
            request.getRequestDispatcher("/").forward(request, response);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
