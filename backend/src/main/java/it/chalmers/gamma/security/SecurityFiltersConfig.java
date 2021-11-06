package it.chalmers.gamma.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
public class SecurityFiltersConfig {

    private final LoginCustomizer loginCustomizer;

    public SecurityFiltersConfig(LoginCustomizer loginCustomizer) {
        this.loginCustomizer = loginCustomizer;
    }

    /**
     * This SecurityFilterChain setups the security for the endpoints that is used for OAuth 2.1.
     */
    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer<>();

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http
                // This SecurityFilterChain shall match the endpoints that spring-authorization-server
                // has for OAuth 2.1 such as /oauth2/authorize or /oauth2/token.
                .requestMatcher(endpointsMatcher)
                // All access has to be authenticated.
                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                // Ignore csrf with all OAuth2.1 actions.
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                // Apply specific OAuth 2.1 settings, which is specified above, such as custom consent page.
                .apply(authorizationServerConfigurer)
                .and()
                // If a client has to authorize, but isn't authenticated, then the user is directed to the login page.
                .formLogin(loginCustomizer);

        return http.build();
    }

    /**
     * Sets up the security for the api that is used by the frontend.
     */
    @Bean
    SecurityFilterChain internalSecurityFilterChain(HttpSecurity http, CsrfTokenRepository csrfTokenRepository) throws Exception {
        http
                //Either /internal/**, /login or /logout
                .regexMatcher("^\\/internal.+|\\/login|\\/logout")
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/login").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository))
                .cors(Customizer.withDefaults())
                .formLogin(loginCustomizer)
                .logout(new LogoutCustomizer())
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        return http.build();
    }

    //TODO: Check for the ApiKeyType and make sure that the URI specified there.

    /**
     * Complemented with ApiUsersMeSecurityConfig
     */
    @Bean
    SecurityFilterChain externalSecurityFilterChain(HttpSecurity http,
                                                    ApiKeyAuthenticationFilter apiKeyAuthenticationFilter) throws Exception {
        http
                //Matches on everything except /users/me, since they are going to use Bearer authentication
                .regexMatcher("^\\/external\\/(?!.*(users\\/me)).+")
                .addFilterBefore(apiKeyAuthenticationFilter, BasicAuthenticationFilter.class)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //Since only backends will call the /external
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    /**
     * This is separated to ensure that /users/me is the only endpoint that can be accessed with a Bearer token.
     */
    @Bean
    SecurityFilterChain externalUsersMeSecurityFilterChain(HttpSecurity http,
                                                           ApiKeyAuthenticationFilter apiKeyAuthenticationFilter) throws Exception {
        http
                //Matches everything that has /users/me, since they are going to use Bearer authentication
                .regexMatcher("^\\/external.*\\b\\/users\\/me\\b.*")
                .addFilterBefore(apiKeyAuthenticationFilter, BasicAuthenticationFilter.class)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //Since only backends will call the /external
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2Resource -> oauth2Resource.jwt());
        return http.build();
    }

    //TODO: Add a FilterSecurityChain for LegacyApiController

}
