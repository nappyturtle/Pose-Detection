package com.pdst.pdstserver.security;

import com.pdst.pdstserver.repository.AccountRepository;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**html")
                .antMatchers("/**png")
                .antMatchers("/**jpg")
                .antMatchers("/**css")
                .antMatchers("/**js")
                .antMatchers("/resources/**")
                .antMatchers("/bower_components/**")
                .antMatchers("/dist/**")
                .antMatchers("/plugins/**")
                .antMatchers("/management/**")
                .antMatchers("/management/course/**")
                .antMatchers("/management/video/**")
                .antMatchers("/management/suggestion/**")
                .antMatchers("/management/account/**")
                .antMatchers("/management/suggestiondetail/**")
                .antMatchers("/images/**")
                .antMatchers("/images/**jpg")
                .antMatchers("/favicon.*")
                .antMatchers("/js/**")
                .antMatchers("/js/**js")
                .antMatchers("/css/**css")
                .antMatchers("/**txt")
                .antMatchers("/account/details/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.POST, CREATE_SUGGESTIONDETAIL).permitAll()
                //.antMatchers(HttpMethod.PUT, EDIT_URL).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_VIDEO).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_CATEGORY).permitAll()
                .antMatchers(HttpMethod.GET, GET_ACCOUNT_BY_ID).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_VIDEO_RELATED).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_VIDEO_TRENDING).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_COURSE_WITH_PRICE).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_FREE_VIDEO_BY_ACCOUNTID).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_COURSES_WITH_PRICE_BY_ACCOUNTID).permitAll()
                .antMatchers(HttpMethod.GET, GET_TRAINER_INFO).permitAll()
                .antMatchers(HttpMethod.GET, GET_UNBOUGHT_COURSES).permitAll()
                .antMatchers(HttpMethod.GET, SEARCH_VIDEO_ORDERED_BY_DATE).permitAll()
                .antMatchers(HttpMethod.GET, SEARCH_VIDEO_ORDERED_BY_VIEW).permitAll()
                .antMatchers(HttpMethod.GET, SEARCH_COURSE).permitAll()
                .antMatchers(HttpMethod.GET, "/account/accounts").hasAuthority("Admin")
                .antMatchers(HttpMethod.GET, "/account/staff/accounts").hasAuthority("Staff")
                .antMatchers("/", "/public/webadmin/**").permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_ACCOUNT_BY_ROLE).permitAll()
                .antMatchers(HttpMethod.POST, UPDATE_ACCOUNT).permitAll()
                .antMatchers(HttpMethod.GET, GET_DATAFORDASHBOARD).permitAll()
                .antMatchers(HttpMethod.POST, CREATE_NEW_ACCOUNT).permitAll()
                .antMatchers(HttpMethod.POST, CREATE_DATASET).permitAll()
                .antMatchers(HttpMethod.GET, GET_DATASET).permitAll()
                //.antMatchers(HttpMethod.GET, GET_ALL_ACCOUNT_BY_ROLE).hasAnyAuthority("Staff")
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTGeneration(authenticationManager(), accountRepository))
                .addFilter(new JWTVerification(authenticationManager()))
                // disable session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
