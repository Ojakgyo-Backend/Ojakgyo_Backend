package Ojakgyo.com.example.Ojakgyo.config;

import Ojakgyo.com.example.Ojakgyo.config.jwt.JwtAuthenticationFilter;
import Ojakgyo.com.example.Ojakgyo.config.jwt.JwtAuthorizationFilter;
import Ojakgyo.com.example.Ojakgyo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CorsConfig corsConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilter(corsConfig.corsFilter())
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                // 필터 추가
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                // 권한 관리
                .authorizeRequests()
                .antMatchers("/user/**")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/deal/**")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/contract/**")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/deal-details/**")
                .access("hasRole('ROLE_USER')")
                .anyRequest().permitAll();	//다른 요청은 권한 허용
    }
}

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         return http.httpBasic().and()
//                .addFilter(corsConfig.corsFilter())
//                .csrf().disable()
//                 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                 .and()
//                .authorizeRequests()
//                .antMatchers("/user/**").authenticated()
////                .access("hasRole('ROLE_USER')")
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/login")     //로그인할 페이지
//                .defaultSuccessUrl("/home")   //로그인 성공시 이동할 URL
//                .failureUrl("/login")    //로그인 실패 시 이동할 URL
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
//                 .and().build();
//    }
//}