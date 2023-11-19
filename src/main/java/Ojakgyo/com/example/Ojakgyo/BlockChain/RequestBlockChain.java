package Ojakgyo.com.example.Ojakgyo.BlockChain;

import Ojakgyo.com.example.Ojakgyo.dto.BlockChainContract;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class RequestBlockChain {
    private final RestTemplate restTemplate;

    private final String BlockChainServerTest = "ec2-15-164-170-1.ap-northeast-2.compute.amazonaws.com:8081/api/test";

    public BlockChainContract requestBlock(Long dealId) {
        final HttpHeaders headers = new HttpHeaders(); // 헤더에 key들을 담아준다.
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(BlockChainServerTest, HttpMethod.GET, entity, BlockChainContract.class, dealId)
                .getBody();
    }
}
