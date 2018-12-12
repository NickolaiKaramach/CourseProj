package by.bsuir.karamach.serviceworker;

import by.bsuir.karamach.serviceworker.entity.CustomerDetails;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class ServiceWorkerApplication extends WebSecurityConfigurerAdapter {
    private static final Algorithm algorithm = Algorithm.HMAC256("secret-key");
    private static final JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer("myauth10")
            .build();

    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/login**"),
            new AntPathRequestMatcher("/error**"),
            new AntPathRequestMatcher("/register/customer**"),
            new AntPathRequestMatcher("/search**"),
            new AntPathRequestMatcher("/activate")
    );

    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);
    private MyTokenProvider myAuthProvider = new MyTokenProvider();

    @Override
    protected void configure(AuthenticationManagerBuilder builder) {

        builder.authenticationProvider(this.myAuthProvider);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                // this entry point handles when you request a protected page and you are not yet
                // authenticated
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN), PROTECTED_URLS)
                .and()
                .authenticationProvider(this.myAuthProvider)
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(PROTECTED_URLS)
                .authenticated()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    protected MyTokenFilter restAuthenticationFilter() throws Exception {
        MyTokenFilter filter = new MyTokenFilter(PROTECTED_URLS);

        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    protected SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new RedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) {
                //no redirects in REST API
            }
        });
        return successHandler;
    }

    private static final class MyTokenFilter extends AbstractAuthenticationProcessingFilter {
        public MyTokenFilter(final RequestMatcher requiresAuth) {
            super(requiresAuth);
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
            String authHeader = httpServletRequest.getHeader("Authorization");
            if (authHeader == null) throw new BadCredentialsException("Missing Authentication Token");

            final Authentication auth = new UsernamePasswordAuthenticationToken(authHeader, authHeader);
            return getAuthenticationManager().authenticate(auth);
        }


    }

    private static final class MyTokenProvider extends AbstractUserDetailsAuthenticationProvider {
        @Override
        protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        }

        @Override
        protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
            Object tokenObject = authentication.getCredentials();

            if (tokenObject instanceof String) {
                //UserService.findByToken
                String token = (String) tokenObject;

                DecodedJWT decodedJwtToken;

                try {
                    decodedJwtToken = verifier.verify(token);

                } catch (JWTVerificationException e) {

                    System.out.println("ERROR");
                    //TODO: LOG !
                    throw new UsernameNotFoundException("Cannot retrieve data from token");

                }

                String email;
                String password;

                email = decodedJwtToken.getClaim("email").asString();
                password = decodedJwtToken.getClaim("password").asString();

                return new CustomerDetails(email, password);
            }

            throw new UsernameNotFoundException("Cannot find user with authentication token=" + tokenObject);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceWorkerApplication.class, args);
    }
}
