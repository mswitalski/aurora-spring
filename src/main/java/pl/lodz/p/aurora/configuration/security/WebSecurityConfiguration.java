package pl.lodz.p.aurora.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.lodz.p.aurora.configuration.security.jwt.AuthenticationFilter;
import pl.lodz.p.aurora.configuration.security.jwt.AuthorizationFilter;

/**
 * Security configuration class for REST api interface.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuroraUserDetailsService userDetailsService;
    private final PasswordEncoderProvider passwordEncoderProvider;

    @Autowired
    public WebSecurityConfiguration(AuroraUserDetailsService userDetailsService, PasswordEncoderProvider passwordEncoderProvider) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoderProvider = passwordEncoderProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "api/v1/profile").hasAnyRole("ADMIN", "UNIT_LEADER", "EMPLOYEE")
                .antMatchers(HttpMethod.PUT, "api/v1/profile").hasAnyRole("ADMIN", "UNIT_LEADER", "EMPLOYEE")
                .antMatchers(HttpMethod.PUT, "api/v1/profile/password").hasAnyRole("ADMIN", "UNIT_LEADER", "EMPLOYEE")

                .antMatchers(HttpMethod.GET, "api/v1/users/").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "api/v1/users/paged/").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.POST, "api/v1/users/search/").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "api/v1/users/**").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.POST, "api/v1/users/").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.PUT, "api/v1/users/**/password").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "api/v1/users/**").hasAnyRole("ADMIN", "UNIT_LEADER")

                .antMatchers(HttpMethod.GET, "api/v1/roles/").hasAnyRole("ADMIN", "UNIT_LEADER")










                .antMatchers(HttpMethod.POST, "/api/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/admin/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/unitleader/**").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/unitleader/**").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.DELETE, "/api/unitleader/**").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.PUT, "/api/unitleader/**").hasRole("UNIT_LEADER")

                .antMatchers(HttpMethod.GET, "/api/users/").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "UNIT_LEADER", "EMPLOYEE")
                .antMatchers(HttpMethod.PUT, "/api/users/").hasAnyRole("ADMIN", "UNIT_LEADER", "EMPLOYEE")

                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().frameOptions().disable()
                .and().anonymous().disable()
                .httpBasic().disable();

        http.cors().and()
                .addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilter(new AuthorizationFilter(authenticationManager()));
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoderProvider.getEncoder());

        return authProvider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
