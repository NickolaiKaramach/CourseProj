package by.bsuir.karamach.serviceworker;

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
import org.springframework.security.core.GrantedAuthority;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ServiceWorkerApplication extends WebSecurityConfigurerAdapter {
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
            Object token = authentication.getCredentials();
            if (token instanceof String) {
                //UserService.findByToken
                return new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return null;
                    }

                    @Override
                    public String getPassword() {
                        return (String) token;
                    }

                    @Override
                    public String getUsername() {
                        return (String) token;
                    }

                    @Override
                    public boolean isAccountNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return true;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                };
            }
            throw new UsernameNotFoundException("Cannot find user with authentication token=" + token);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceWorkerApplication.class, args);
    }
}
