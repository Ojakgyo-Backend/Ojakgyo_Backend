package Ojakgyo.com.example.Ojakgyo.config.jwt;

import Ojakgyo.com.example.Ojakgyo.config.auth.PrincipalDetails;
import Ojakgyo.com.example.Ojakgyo.dto.LoginRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper mapper = new ObjectMapper();

    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        ObjectMapper om = new ObjectMapper();
        LoginRequest loginRequestDto = null;
        try {
            loginRequestDto = om.readValue(request.getInputStream(), LoginRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword());


        /**
         authenticate() 호출 -> AuthenticationManager(ProviderManager) -> DaoAuthenticationProvider가
         PrincipalDatailsService의 loadUserByUsername(토큰의 첫번째 파라메터(username) 호출
         PrincipalDatails 리턴-> 토큰의 두번째 파라메터(credential,password)과
         PrincipalDatails(DB값)의 getPassword()함수로 비교해서 동일하면
         인증 대상 객체인 UsernamePasswordAuthenticationToken에
         UserDetails 객체와 Authorities를 담아서 반환

        AuthenticationManager의 디폴트 서비스는 UserDetailsService 타입
        AuthenticationManager의 디폴트 암호화 방식은 BCryptPasswordEncoder -> 얘써야함 그래서 ㅇㅇ
        =>인증 프로바이더에게 알려줄 필요가 없음.
         **/
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);
        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("Authentication : "+principalDetailis.getUser().getEmail());
        return authentication;
    }

    /**
     * AccessToken 생성 메소드
     */
    private String createAccessToken(PrincipalDetails principalDetailis) {
        return JWT.create()  //JWT 토큰을 생성하는 빌더 반환
                .withSubject(principalDetailis.getUsername())   //JWT의 Subject 지정
                //토큰 만료 시간 설정 : 현재시간 + 만료 시간
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                //클레임 추가 : id와 username(email)
                .withClaim("id", principalDetailis.getUser().getId())
                .withClaim("username", principalDetailis.getUser().getEmail())
                //HMAC512 알고리즘 사용, JwtProperties클래스에 정의한 secret 키로 암호화
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    /** 인증됐다면,
     * AccessToken 생성 + response에 담아 보내기
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken = createAccessToken(principalDetailis);

        Long loginResponse = principalDetailis.getUser().getId();
        System.out.println(principalDetailis.getUser().getId());
        String result = mapper.writeValueAsString(loginResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    }
}