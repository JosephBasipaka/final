// package com.example.demo;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


// @Configuration
// @EnableWebSecurity
// public class BasicConfiguration {

//     @Autowired
//     private CorsConfig corsConfig;

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//         http
//             .csrf(csrf -> csrf.disable())
//             .cors(cors -> cors.configurationSource(corsConfig))
//             .authorizeHttpRequests((requests) -> requests
//             .requestMatchers("/api/*").permitAll()
//             .anyRequest().authenticated())
//             .logout(withDefaults())
//             .httpBasic(withDefaults());
//             //.formLogin(withDefaults())
    
//         return http.build;
//     }
    
// }
