package pl.lodz.p.aurora.configuration.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pl.lodz.p.aurora.users.domain.dto.UserAccountCredentialsDto;
import pl.lodz.p.aurora.users.domain.entity.User;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Security filter responsible for handling user authentication with JWT.
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private AuthenticationManager authenticationManager;
    private TokenConfigurationData configurationData;
    private final String filteredPath = "/login";
    private final HttpMethod filteredMethod = HttpMethod.POST;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super();
        this.authenticationManager = authenticationManager;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(filteredPath, filteredMethod.name()));
    }

    /**
     * Try to authenticate user with given credentials.
     *
     * @param request Received request
     * @param response Response to send
     * @return Authentication manager with authenticated user
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        initializeDependencies(request);

        try {
            UserAccountCredentialsDto accountCredentialsDto = new ObjectMapper()
                    .readValue(request.getInputStream(), UserAccountCredentialsDto.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            accountCredentialsDto.getUsername(),
                            accountCredentialsDto.getPassword()));

        } catch (IOException e) {
            logger.error("Authentication attempt has failed", e);
            response.setStatus(400);
        }

        return null;
    }

    /**
     * Initialize mandatory dependencies. These dependencies are managed by Spring and can't be initialized
     * any other way.
     *
     * @param request Received request
     */
    private void initializeDependencies(HttpServletRequest request) {
        configurationData = WebApplicationContextUtils.
                getWebApplicationContext(request.getServletContext()).
                getBean(TokenConfigurationData.class);
    }

    /**
     * Create JWT token and add it to response header on successful authentication.
     *
     * @param request Received request
     * @param response Response to send
     * @param filterChain Spring security filter chain
     * @param auth Authentication object
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain,
                                            Authentication auth) {
        initializeDependencies(request);
        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS512, configurationData.getTokenSecretKey())
                .compact();
        response.addHeader(configurationData.getTokenHeader(), configurationData.getTokenPrefix() + token);
        response.setContentType("application/json");
        response.addHeader("Access-Control-Expose-Headers", "Authorization");

        try {
            response.getOutputStream().write("{}".getBytes());

        } catch (IOException e) {
            logger.error("Failed to provide the default JSON body for the JWT token response", e);
        }
    }

    private Date getExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, configurationData.getTokenExpiresInHours());

        return calendar.getTime();
    }
}
