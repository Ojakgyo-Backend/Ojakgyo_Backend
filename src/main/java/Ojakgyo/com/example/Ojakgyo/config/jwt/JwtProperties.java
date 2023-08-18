package Ojakgyo.com.example.Ojakgyo.config.jwt;

public interface JwtProperties {
    // private key : JWT 생성 시 암호화 진행
    String SECRET = "yujeongChoegokk";
    // 액세스 토큰/리프레시 토큰의 만료 시간 : 10일 (1/1000초)
    int EXPIRATION_TIME = 864000000;

    String TOKEN_PREFIX = "Bearer ";
    //액세스 토큰/리프레시 토큰이 담길 헤더의 이름(Key)
    String HEADER_STRING = "Authorization";
}
