package pl.lodz.p.aurora.mco.security.jwt;

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
import pl.lodz.p.aurora.mus.web.dto.UserAccountCredentialsDto;
import pl.lodz.p.aurora.mus.domain.entity.User;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Security filter responsible for handling user authentication with JWT.
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final String filteredPath = "/login";
    private final HttpMethod filteredMethod = HttpMethod.POST;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private AuthenticationManager authManager;
    private TokenConfigurationData configurationData;

    public AuthenticationFilter(AuthenticationManager authManager) {
        super();
        this.authManager = authManager;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(filteredPath, filteredMethod.name()));
    }

    /**
     * Try to authenticate user with given credentials.
     *
     * @param req Received request
     * @param res Response to send
     * @return Authentication manager with authenticated user
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        initializeDependencies(req);

        try {
            UserAccountCredentialsDto dto = new ObjectMapper()
                    .readValue(req.getInputStream(), UserAccountCredentialsDto.class);

            return authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        } catch (IOException e) {
            logger.error("Authentication attempt has failed", e);
            res.setStatus(400);
        }

        return null;
    }

    /**
     * Initialize mandatory dependencies. These dependencies are managed by Spring and can't be initialized
     * any other way.
     *
     * @param req Received request
     */
    private void initializeDependencies(HttpServletRequest req) {
        configurationData = WebApplicationContextUtils.
                getWebApplicationContext(req.getServletContext()).
                getBean(TokenConfigurationData.class);
    }

    /**
     * Create JWT token and add it to response header on successful authentication.
     *
     * @param req         Received request
     * @param res         Response to send
     * @param filterChain Spring security filter chain
     * @param auth        Authentication object
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain filterChain,
                                            Authentication auth) {
        initializeDependencies(req);
        User authenticatedUser = (User) auth.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authenticatedUser.getRoles());
        claims.put("enabled", authenticatedUser.isEnabled());

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(authenticatedUser.getUsername())
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS512, configurationData.getTokenSecretKey())
                .compact();
        res.addHeader(configurationData.getTokenHeader(), configurationData.getTokenPrefix() + token);
        res.setContentType("application/json");
        res.addHeader("Access-Control-Expose-Headers", "Authorization");

        try {
            res.getOutputStream().write("{}".getBytes());

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
