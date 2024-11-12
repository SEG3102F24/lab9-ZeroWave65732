package seg3x02.tempconverterapi.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class ApiSecurityConfig {
    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration):
            AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http.cors(Customizer.withDefaults()).csrf(({ csrf -> csrf.disable() }))
            .oauth2ResourceServer {
                obj: OAuth2ResourceServerConfigurer<HttpSecurity?> -> obj.jwt(Customizer.withDefaults())}
                .exceptionHandling{ ex: ExceptionHandlingConfigurer<HttpSecurity?> ->
                    ex.authenticationEntryPoint(BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(BearerTokenAccessDeniedHandler())
            }.sessionManagement{ session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests{auth ->
                auth
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers(HttpMethod.GET,"/tempconverterapi/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST,"/tempconvertapi/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/tempconverterapi/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/tempconverterapi/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/books-api/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
        return http.build()
    }

}