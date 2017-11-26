package pl.lodz.p.aurora.configuration.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pl.lodz.p.aurora.common.exception.InvalidResourceRequestedException;
import pl.lodz.p.aurora.users.domain.dto.TokenClaimsDto;
import pl.lodz.p.aurora.users.domain.entity.Role;
import pl.lodz.p.aurora.users.domain.entity.User;
import pl.lodz.p.aurora.users.service.common.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Security filter responsible for handling user authorization with JWT.
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserService userService;
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

        if (authentication == null) {
            response.setStatus(401);

        } else {
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (request.getMethod().equals(HttpMethod.GET.name())) {
                response.addHeader("Access-Control-Expose-Headers", "ETag");
            }

            filterChain.doFilter(request, response);
        }
    }

    /**
     * Initialize mandatory dependencies. These dependencies are managed by Spring and can't be initialized
     * any other way.
     *
     * @param request Received request
     */
    private void initializeDependencies(HttpServletRequest request) {
        userService = WebApplicationContextUtils.
                getWebApplicationContext(request.getServletContext()).
                getBean(UserService.class);
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
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws IOException {
        String token = request.getHeader(configurationData.getTokenHeader());

        if (token != null) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(configurationData.getTokenSecretKey())
                        .parseClaimsJws(token.replace(configurationData.getTokenPrefix(), ""))
                        .getBody();
                User storedUser = userService.findByUsername(claims.getSubject());
                TokenClaimsDto tokenClaimsDto = new TokenClaimsDto(
                        claims.get("enabled", Boolean.class),
                        processMisshapedRoles(claims.get("roles", ArrayList.class))
                );

                if (areTokenClaimsValid(storedUser, tokenClaimsDto)) {
                    return new UsernamePasswordAuthenticationToken(storedUser, null, storedUser.getAuthorities());

                } else {
                    logger.info("The subject in provided token does not exist");
                }

            } catch (ExpiredJwtException ex) {
                logger.info("User tried to authorize with expired JWT token", ex);
            } catch (InvalidResourceRequestedException ex) {
                logger.info("User tried to authorize with JWT token that points to non-existent user", ex);
            }
        }

        return null;
    }

    private Set<Role> processMisshapedRoles(ArrayList rolesFromToken) {
        Set<Role> properRoles = new HashSet<>();

        rolesFromToken.forEach(misshaped -> {
            String roleName = misshaped.toString().replace("{name=", "")
                    .replace("}", "");
            properRoles.add(new Role(roleName));
        });

        return properRoles;
    }

    private boolean areTokenClaimsValid(User storedUser, TokenClaimsDto tokenClaimsDto) {
        return storedUser != null &&
                storedUser.isEnabled() == tokenClaimsDto.isEnabled() &&
                storedUser.getRoles().containsAll(tokenClaimsDto.getRoles());
    }
}
