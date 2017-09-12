package pl.lodz.p.aurora.configuration.security.jwt;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.domain.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Security filter responsible for handling user authorization with JWT.
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserRepository userRepository;
    private TokenConfigurationData configurationData;

    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
        this.configurationData = new TokenConfigurationData();
    }

    /**
     * Filter action and authorize user request with given JWT token.
     *
     * @param request Received request
     * @param response Response to send
     * @param filterChain Spring security filter chain
     * @throws IOException if an I/O error occurs during the processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        initializeDependencies(request);
        String header = request.getHeader(configurationData.getTokenHeader());

        if (header == null || !header.startsWith(configurationData.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    /**
     * Initialize mandatory dependencies. These dependencies are managed by Spring and can't be initialized
     * any other way.
     *
     * @param request Received request
     */
    private void initializeDependencies(HttpServletRequest request) {
        userRepository = WebApplicationContextUtils.
                getWebApplicationContext(request.getServletContext()).
                getBean(UserRepository.class);
        configurationData = WebApplicationContextUtils.
                getWebApplicationContext(request.getServletContext()).
                getBean(TokenConfigurationData.class);
    }

    /**
     * Authenticate user in the system.
     *
     * @param request Received request
     * @return Authenticated user
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(configurationData.getTokenHeader());

        if (token != null) {
            String subject = Jwts.parser()
                    .setSigningKey(configurationData.getTokenSecretKey())
                    .parseClaimsJws(token.replace(configurationData.getTokenPrefix(), ""))
                    .getBody()
                    .getSubject();
            User storedUser = userRepository.findDistinctByUsername(subject);

            if (storedUser != null) {
                return new UsernamePasswordAuthenticationToken(storedUser, null, storedUser.getAuthorities());

            } else {
                logger.warn("The subject in provided token does not exist");
            }
        }

        return null;
    }
}
