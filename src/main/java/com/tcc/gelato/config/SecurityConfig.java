package com.tcc.gelato.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final F_Jwt f_jwt;

    public SecurityConfig(UserDetailsService userDetailsService, F_Jwt f_jwt) {
        this.userDetailsService = userDetailsService;
        this.f_jwt = f_jwt;
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
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .addFilterBefore(f_jwt, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedPage("/catalogo"))
            .authenticationProvider(authenticationProvider())
            .build();
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

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, accessDeniedException) -> {
            if (request.getMethod().equals("POST")) {
                M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
                m_respostaTexto.setSucesso(false);
                m_respostaTexto.setMensagem("Você não tem autorização para ultilizar esse recurso.");

                String resposta = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(m_respostaTexto);
                response.setContentLength(resposta.getBytes(StandardCharsets.UTF_8).length);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                PrintWriter writer = response.getWriter();
                writer.print(resposta);
                writer.flush();
                return;
            }
            response.sendRedirect("/catalogo");
        };
    }
}
