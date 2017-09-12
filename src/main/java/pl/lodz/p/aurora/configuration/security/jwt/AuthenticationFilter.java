package pl.lodz.p.aurora.configuration.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pl.lodz.p.aurora.configuration.security.jwt.TokenConfigurationData;
import pl.lodz.p.aurora.users.domain.dto.UserAccountCredentialsDto;
import pl.lodz.p.aurora.users.domain.entity.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Marek on 9/10/2017.
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private TokenConfigurationData configurationData;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super();
        this.authenticationManager = authenticationManager;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        initializeDependencies(request);

        try {
            UserAccountCredentialsDto accountCredentialsDto = new ObjectMapper()
                    .readValue(request.getInputStream(), UserAccountCredentialsDto.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            accountCredentialsDto.getUsername(),
                            accountCredentialsDto.getPassword()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeDependencies(HttpServletRequest request) {
        configurationData = WebApplicationContextUtils.
                getWebApplicationContext(request.getServletContext()).
                getBean(TokenConfigurationData.class);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain,
                                            Authentication auth) throws IOException, ServletException {
        initializeDependencies(request);
        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS512, configurationData.getTokenSecretKey())
                .compact();
        response.addHeader(configurationData.getTokenHeader(), configurationData.getTokenPrefix() + token);
    }

    private Date getExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, configurationData.getTokenExpiresInHours());

        return calendar.getTime();
    }
}
