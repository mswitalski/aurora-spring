package pl.lodz.p.aurora.mco.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.lodz.p.aurora.mco.security.jwt.AuthenticationFilter;
import pl.lodz.p.aurora.mco.security.jwt.AuthorizationFilter;

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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // Profile
                .antMatchers(HttpMethod.GET, "/api/v1/profile").hasAnyRole("ADMIN", "UNIT_LEADER", "EMPLOYEE")
                .antMatchers(HttpMethod.PUT, "/api/v1/profile").hasAnyRole("ADMIN", "UNIT_LEADER", "EMPLOYEE")
                .antMatchers(HttpMethod.PUT, "/api/v1/profile/password").hasAnyRole("ADMIN", "UNIT_LEADER", "EMPLOYEE")

                // Users
                .antMatchers(HttpMethod.GET, "/api/v1/users/").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/users/paged/").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/users/all/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.POST, "/api/v1/users/search/").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/users/{id:[\\d]+}/evaluations/").hasAnyRole("EMPLOYEE", "UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/users/me/mentoring/").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.GET, "/api/v1/users/{id:[\\d]+}").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.POST, "/api/v1/users/").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.PUT, "/api/v1/users/{id:[\\d]+}/password").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.PUT, "/api/v1/users/{id:[\\d]+}").hasAnyRole("ADMIN", "UNIT_LEADER")
                .antMatchers(HttpMethod.PUT, "/api/v1/users/{id:[\\d]+}/duties/").hasRole("UNIT_LEADER")

                // Roles
                .antMatchers(HttpMethod.GET, "/api/v1/roles/").hasRole("ADMIN")

                // Duties
                .antMatchers(HttpMethod.GET, "/api/v1/duties/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.POST, "/api/v1/duties/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/duties/paged/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.POST, "/api/v1/duties/search/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/duties/{id:[\\d]+}").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.PUT, "/api/v1/duties/{id:[\\d]+}").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.DELETE, "/api/v1/duties/{id:[\\d]+}").hasRole("UNIT_LEADER")

                // Skills
                .antMatchers(HttpMethod.GET, "/api/v1/skills/").hasAnyRole("EMPLOYEE", "UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/skills/paged/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/skills/search/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.POST, "/api/v1/skills/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/skills/{id:[\\d]+}").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.DELETE, "/api/v1/skills/{id:[\\d]+}").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.PUT, "/api/v1/skills/{id:[\\d]+}").hasRole("UNIT_LEADER")

                // Evaluations
                .antMatchers(HttpMethod.POST, "/api/v1/evaluations/").hasAnyRole("EMPLOYEE", "UNIT_LEADER")
                .antMatchers(HttpMethod.DELETE, "/api/v1/evaluations/{id:[\\d]+}").hasAnyRole("EMPLOYEE", "UNIT_LEADER")
                .antMatchers(HttpMethod.PUT, "/api/v1/evaluations/{id:[\\d]+}").hasAnyRole("EMPLOYEE", "UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/evaluations/{id:[\\d]+}").hasAnyRole("EMPLOYEE", "UNIT_LEADER")

                // Mentors
                .antMatchers(HttpMethod.GET, "/api/v1/mentors/paged/").hasAnyRole("EMPLOYEE", "UNIT_LEADER")
                .antMatchers(HttpMethod.POST, "/api/v1/mentors/search/").hasAnyRole("EMPLOYEE", "UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/mentors/{id:[\\d]+}/feedback/").hasAnyRole("EMPLOYEE", "UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/mentors/{id:[\\d]+}").hasAnyRole("EMPLOYEE", "UNIT_LEADER")
                .antMatchers(HttpMethod.POST, "/api/v1/mentors/").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.DELETE, "/api/v1/mentors/{id:[\\d]+}").hasAnyRole("EMPLOYEE", "UNIT_LEADER")
                .antMatchers(HttpMethod.PUT, "/api/v1/mentors/{id:[\\d]+}").hasAnyRole("EMPLOYEE", "UNIT_LEADER")

                // Feedback
                .antMatchers(HttpMethod.POST, "/api/v1/feedback/").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.DELETE, "/api/v1/feedback/{id:[\\d]+}").hasRole("UNIT_LEADER")

                // Trainings
                .antMatchers(HttpMethod.GET, "/api/v1/trainings/finished/").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.GET, "/api/v1/trainings/planned/").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.GET, "/api/v1/trainings/paged/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.POST, "/api/v1/trainings/search/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/trainings/{id:[\\d]+}").hasAnyRole("EMPLOYEE", "UNIT_LEADER")
                .antMatchers(HttpMethod.DELETE, "/api/v1/trainings/{id:[\\d]+}").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.PUT, "/api/v1/trainings/{id:[\\d]+}").hasRole("UNIT_LEADER")

                // Tasks
                .antMatchers(HttpMethod.GET, "/api/v1/users/me/tasks/undone/").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.GET, "/api/v1/users/me/tasks/done/").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.GET, "/api/v1/users/me/tasks/statistics").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.GET, "/api/v1/users/{id:[\\d]+}/tasks/undone/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/users/{id:[\\d]+}/tasks/done/").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.GET, "/api/v1/users/{id:[\\d]+}/tasks/statistics").hasRole("UNIT_LEADER")
                .antMatchers(HttpMethod.POST, "/api/v1/tasks/").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.GET, "/api/v1/tasks/{id:[\\d]+}").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.PUT, "/api/v1/tasks/{id:[\\d]+}").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.DELETE, "/api/v1/tasks/{id:[\\d]+}").hasRole("EMPLOYEE")

                // The rest of the configuration
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().frameOptions().disable()
                .and().anonymous().disable()
                .httpBasic().disable()
                .cors().and()

                // Filters
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
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.DELETE);
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
