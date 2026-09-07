package com.example.stadiumtickets.config;

import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.model.Role;
import com.example.stadiumtickets.repository.AccountRepository;
import com.example.stadiumtickets.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/css/**", "/images/**", "/login", "/register").permitAll()
                .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/admin/accounts/**", "/admin/roles/**", "/admin/user-roles/**",
                        "/admin/banks/**", "/admin/gametypes/**", "/admin/employees/**", "/admin/cart/**")
                    .hasAuthority("Администратор")
                .requestMatchers("/admin/**")
                    .hasAnyAuthority("Администратор", "Менеджер")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")
            )
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = roleRepository.findByName("Администратор").orElseGet(() -> {
                Role r = new Role();
                r.setName("Администратор");
                r.setPermissions("all");
                return roleRepository.save(r);
            });
            System.out.println(">>> Администратор role id=" + adminRole.getId());

            Role managerRole = roleRepository.findByName("Менеджер").orElseGet(() -> {
                Role r = new Role();
                r.setName("Менеджер");
                r.setPermissions("events,tickets,orders,venues,sectors,seats");
                return roleRepository.save(r);
            });

            Role userRole = roleRepository.findByName("Пользователь").orElseGet(() -> {
                Role r = new Role();
                r.setName("Пользователь");
                r.setPermissions("browse,cart,myevents");
                return roleRepository.save(r);
            });

            if (accountRepository.findByUsername("admin").isEmpty()) {
                Account admin = new Account();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setEmail("admin@stadium.ru");
                admin.setFirstName("Администратор");
                admin.setLastName("Системный");
                admin.setPhoneNumber("0000000000");
                admin.setRole(adminRole);
                admin.setActive(true);
                admin.setCreatedAt(LocalDateTime.now());
                accountRepository.save(admin);
            }

        };
    }
}
