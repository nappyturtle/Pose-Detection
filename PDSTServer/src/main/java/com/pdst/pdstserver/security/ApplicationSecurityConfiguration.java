package com.pdst.pdstserver.security;

import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.repositories.AccountRepository;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.pdst.pdstserver.security.SecurityConstants.*;

@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ApplicationUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AccountRepository accountRepository;

    public ApplicationSecurityConfiguration(
            ApplicationUserDetailsService userDetailsService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AccountRepository accountRepository) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accountRepository = accountRepository;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.POST, CREATE_SUGGESTIONDETAIL).permitAll()
                .antMatchers(HttpMethod.PUT, EDIT_URL).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_VIDEO).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_CATEGORY).permitAll()
                .antMatchers(HttpMethod.GET, GET_ACCOUNT_BY_ID).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_VIDEO_TRAINER).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_VIDEO_TRENDING).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_COURSE_WITH_PRICE).permitAll()
                .antMatchers(HttpMethod.GET, "/account/accounts").hasAuthority("Admin")
                .antMatchers(HttpMethod.GET, "/account/staff/accounts").hasAuthority("Staff")
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTGeneration(authenticationManager(), accountRepository))
                .addFilter(new JWTVerification(authenticationManager()))
                // disable session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
