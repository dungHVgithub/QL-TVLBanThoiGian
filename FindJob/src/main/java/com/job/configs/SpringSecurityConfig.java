package com.job.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.job.filters.JwtFilter;
import jakarta.servlet.Filter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.job.controllers",
    "com.job.repositories",
    "com.job.services",
    "com.job.filters"
})
public class SpringSecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    /* --------------------------------------------------
     * Password encoder
     * -------------------------------------------------- */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* --------------------------------------------------
     * DaoAuthenticationProvider wiring UserDetailsService
     * -------------------------------------------------- */
    @Bean
    public AuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    /* --------------------------------------------------
     * AuthenticationManager (build from provider)
     * -------------------------------------------------- */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(daoAuthProvider());
        return builder.build();
    }

    /* --------------------------------------------------
     * HandlerMappingIntrospector (multipart / mvc infra)
     * -------------------------------------------------- */
    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    /* --------------------------------------------------
     * Multipart resolver
     * -------------------------------------------------- */
    @Bean
    @Order(0)
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    /* --------------------------------------------------
     * CORS config (DEV: mở mọi origin)
     *  - Dùng addAllowedOriginPattern("*") để hỗ trợ allowCredentials=true
     *  - Nếu PROD: hãy đọc danh sách origin từ ENV & giới hạn lại.
     * -------------------------------------------------- */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Chấp nhận tất cả origin (mọi IP/domain/port) – DEV ONLY
        config.addAllowedOriginPattern("*");

        // Cho phép cookie / Authorization header
        config.setAllowCredentials(true);

        // Header & Method
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // Header browser được phép đọc
        config.addExposedHeader("Authorization");
        config.addExposedHeader("Content-Type");

        // Cache preflight 1h (tùy chọn)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // pattern bên trong contextPath (/FindJob) -> dùng /** để khỏi lẫn
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /* --------------------------------------------------
     * Security filter chain
     * -------------------------------------------------- */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // bật cors() dùng bean ở trên
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // tắt CSRF (REST API / JWT)
            .csrf(csrf -> csrf.disable())
            // auth rules
            .authorizeHttpRequests(req -> req
                // CHO PHÉP preflight (OPTIONS) mọi url
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Trang chủ yêu cầu auth? (giữ theo code bạn – chỉnh nếu muốn)
                .requestMatchers("/").authenticated()

                // Ví dụ rule cũ của bạn
                .requestMatchers(HttpMethod.GET, "/job_postings").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/job_postings/**").hasAnyRole("ADMIN")

                // Một số API public/secure
                .requestMatchers(HttpMethod.GET, "/api/users/check_email_exists").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/user/update").authenticated()
                .requestMatchers("/api/notifications").authenticated()
                .requestMatchers("/api/follows").authenticated()

                // Mặc định mở hết /api/** (dev)
                .requestMatchers("/api/**").permitAll()

                .anyRequest().permitAll()
            )
            // form login (vẫn enable cho phần web MVC)
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/login").permitAll());

        // Thêm JWT filter trước UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /* --------------------------------------------------
     * Cloudinary (TODO: chuyển sang ENV biến môi trường)
     * -------------------------------------------------- */
    @Bean
    public Cloudinary cloudinary() {
        // TODO: replace hard-coded secrets
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dxymsdvsz",
                "api_key", "216696381565245",
                "api_secret", "hsazyGO01a_702eZ9inRx8skrWA",
                "secure", true));
    }
}

