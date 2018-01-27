package pl.lodz.p.aurora.mco.security.jwt;

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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pl.lodz.p.aurora.msh.exception.InvalidResourceRequestedException;
import pl.lodz.p.aurora.mus.web.dto.TokenClaimsDto;
import pl.lodz.p.aurora.mus.domain.entity.Role;
import pl.lodz.p.aurora.mus.domain.entity.User;
import pl.lodz.p.aurora.mus.service.common.UserService;

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
     * @param request     Received request
     * @param response    Response to send
     * @param filterChain Spring security filter chain
     * @throws IOException      if an I/O error occurs during the processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        initializeDependencies(request);
        String tokenHeader = request.getHeader(configurationData.getTokenHeader());

        if (tokenHeader == null || !tokenHeader.startsWith(configurationData.getTokenPrefix())) {
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
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        userService = context.getBean(UserService.class);
        configurationData = context.getBean(TokenConfigurationData.class);
    }

    /**
     * Authenticate user in the system.
     *
     * @param req Received request
     * @return Authenticated user
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) throws IOException {
        String tokenHeader = req.getHeader(configurationData.getTokenHeader());

        if (tokenHeader != null) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(configurationData.getTokenSecretKey())
                        .parseClaimsJws(tokenHeader.replace(configurationData.getTokenPrefix(), ""))
                        .getBody();
                User storedUser = userService.findByUsername(claims.getSubject());
                TokenClaimsDto dto = new TokenClaimsDto(
                        claims.get("enabled", Boolean.class),
                        processMisshapedRoles(claims.get("roles", ArrayList.class))
                );

                if (areTokenClaimsValid(storedUser, dto)) {
                    return new UsernamePasswordAuthenticationToken(storedUser, null, storedUser.getAuthorities());

                } else {
                    logger.info("Provided token is not valid - subject does not exist or his data has changed");
                }

            } catch (ExpiredJwtException ex) {
                logger.info("User tried to authorize with expired JWT token", ex);

            } catch (InvalidResourceRequestedException ex) {
                logger.info("User tried to authorize with JWT token that points to non-existent user", ex);
            }
        }

        return null;
    }

    private Set<Role> processMisshapedRoles(ArrayList roles) {
        Set<Role> properRoles = new HashSet<>();

        roles.forEach(r -> {
            String roleName = r.toString().replace("{name=", "").replace("}", "");
            properRoles.add(new Role(roleName));
        });

        return properRoles;
    }

    private boolean areTokenClaimsValid(User user, TokenClaimsDto dto) {
        return user != null && user.isEnabled() && dto.getRoles().equals(user.getRoles());
    }
}
