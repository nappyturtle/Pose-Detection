package com.pdst.pdstserver.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.pdst.pdstserver.account.AccountDTO;
import com.pdst.pdstserver.account.Account;
import com.pdst.pdstserver.account.AccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import static com.pdst.pdstserver.security.SecurityConstants.*;


public class JWTGeneration extends UsernamePasswordAuthenticationFilter {

    public static String usernameTmp = "";
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;

    public JWTGeneration(AuthenticationManager authenticationManager, AccountRepository accountRepository) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        try {
            Account account = new ObjectMapper()
                    .readValue(request.getInputStream(), Account.class);
            usernameTmp = account.getUsername();


            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(account.getUsername(),
                                                            account.getPassword())
            );
        } catch (IOException e) {

            throw new RuntimeException(e);


        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        authResult.getAuthorities().forEach(authority -> System.out.println(authority.getAuthority()));
        String token = JWT.create()
                          .withSubject(((User) authResult.getPrincipal()).getUsername())
                          .withClaim(AUTHORITIES_KEY, authResult.getAuthorities()
                                                                .stream()
                                                                .map(GrantedAuthority::getAuthority)
                                                                .collect(Collectors.joining(",")))
                          .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                          .sign(Algorithm.HMAC512(JWT_SECRET.getBytes()));

        Account account = accountRepository.findByUsernameAndStatusEquals(usernameTmp,"active");

        if(account != null) {
            response.setHeader(HEADER_STRING, TOKEN_PREFIX + token);
            AccountDTO dto = new AccountDTO();
            dto.setId(account.getId());
            dto.setUsername(account.getUsername());
            dto.setRoleId(account.getRoleId());
            dto.setToken(TOKEN_PREFIX+token);
            dto.setMessage("Đăng nhập thành công");
            dto.setImgUrl(account.getImgUrl());

            String accResponse = new Gson().toJson(dto);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(accResponse);
        }else{
            AccountDTO dto = new AccountDTO();
            dto.setMessage("Tài khoản của bạn đã bị khoá");

            String accResponse = new Gson().toJson(dto);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(accResponse);
        }
    }
}
