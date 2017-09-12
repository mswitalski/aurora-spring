package pl.lodz.p.aurora.configuration.security.jwt;

import io.jsonwebtoken.Jwts;
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
 * TODO
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private JwtConfigurationData configurationData;

    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
        this.configurationData = new JwtConfigurationData();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        userRepository = WebApplicationContextUtils.
                getWebApplicationContext(request.getServletContext()).
                getBean(UserRepository.class);
        String header = request.getHeader(configurationData.getTokenHeader());

        if (header == null || !header.startsWith(configurationData.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

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
            }
        }

        return null;
    }
}
