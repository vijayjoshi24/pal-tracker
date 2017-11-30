package io.pivotal.pal.tracker;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.StringUtils;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static String USER_ROLE = "USER";
    private static String SECURITY_FORCE_HTTPS = "SECURITY_FORCE_HTTPS";
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String forceHttps = System.getenv(SECURITY_FORCE_HTTPS);
        if(!StringUtils.isEmpty(forceHttps) && "true".equalsIgnoreCase(forceHttps)){
            httpSecurity.requiresChannel().anyRequest().requiresSecure();
        }
        httpSecurity.authorizeRequests().antMatchers("/**").hasRole(USER_ROLE).and().httpBasic().and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password").roles(USER_ROLE);
    }
}
