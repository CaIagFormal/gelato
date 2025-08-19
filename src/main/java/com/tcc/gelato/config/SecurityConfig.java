package com.tcc.gelato.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] publico = new String[]{
                "/", "/catalogo", "/produto", // Get
                "/js/funcionalidades.js",   // JS
                "/js/catalogo_cliente.js",
                "/css/index.css",           //CSS
                "/css/catalogo_cliente.css",
                "/css/produto_cliente.css",
                "/favicon.ico"};
        String[] visitante = new String[]{
                "/cadastrar", "/fazer_login", // Post
                "/cadastro", "/login",  // Get
                "/js/formularios.js", // JS
                "/js/cadastro_senha.js",
                "/css/index.css" //CSS
        };
        String[] vendedor = new String[]{
                "/inspecionar_saldo", "/inspecionar_transacoes", "/alterar_saldo", "/esvaziar_saldo", // Post
                "/adicionar_estoque",
                "/gerir_saldo", // Get
                "/js/gerir_saldo.js", // JS
                "/js/produto_vndedor.js",
                "/css/gerir_saldo.css" // CSS
        };
        String[] cliente = new String[]{
                "/adicionar_carrinho", "/remover_do_carrinho","/definir_horario_retirada_ticket", // Post
                "/carrinho", // Get
                "/js/carrinho.js", // JS
                "/js/produto_cliente.js",
                "/css/carrinho_cliente.css", // CSS
        };
        // Qualquer outro request é permitido a usuários autenticados

        return http.csrf(AbstractHttpConfigurer::disable)
                .logout((logout) -> logout.logoutSuccessHandler((request, response, authentication) -> {
                    M_RespostaTexto m_respostaTexto = new M_RespostaTexto();

                    m_respostaTexto.setMensagem("Saiu da conta com sucesso, tem de se cadastrar novamente para acessar recursos exclusivos.");
                    m_respostaTexto.setSucesso(true);

                    String json = new ObjectMapper().writeValueAsString(m_respostaTexto);
                    response.setIntHeader("Content-Length",json.getBytes().length);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(json);
                    response.getWriter().close();
                }))
            .authorizeHttpRequests(request -> request
                    .requestMatchers(publico)
                    .permitAll()

                    .requestMatchers(visitante)
                    .anonymous()

                    .requestMatchers(vendedor)
                    .hasAuthority("VENDEDOR")

                    .requestMatchers(cliente)
                    .hasAuthority("CLIENTE")

                    .anyRequest()
                    .authenticated())
            //.formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
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
}
