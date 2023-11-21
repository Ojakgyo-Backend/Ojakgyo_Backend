package Ojakgyo.com.example.Ojakgyo.BlockChain;

import Ojakgyo.com.example.Ojakgyo.dto.BlockChainContract;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@RequiredArgsConstructor
@Service
public class RequestBlockChain {
    private final RestTemplate restTemplate = new RestTemplate();

    private final String BlockChainServer = "http://ec2-15-164-170-1.ap-northeast-2.compute.amazonaws.com:8081/api";

    public BlockChainContract requestSaveBlock(BlockChainContract request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        try {
            // 객체를 JSON 문자열로 변환
            String requestBody = new ObjectMapper().writeValueAsString(request);

            // 헤더와 바디 합치기
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            return restTemplate.exchange(createSaveBlockApiUri(),
                            HttpMethod.POST,
                            entity,
                            BlockChainContract.class)
                    .getBody();
        } catch (JsonProcessingException e) {
            // JSON 변환 오류 처리
            e.printStackTrace();
            return null; // 또는 예외 처리에 따라 다른 방식으로 처리
        }
    }

    private String createSaveBlockApiUri() {
        return UriComponentsBuilder.fromUriString(BlockChainServer)
                .path("/blockchain")
                .encode()
                .toUriString();
    }

    public BlockChainContract requestGetBlock(long dealId) {
        return restTemplate.exchange(createGetBlockApiUri(dealId),
                        HttpMethod.GET,
                        new HttpEntity<>(new HttpHeaders()),
                        BlockChainContract.class)
                .getBody();
    }

    private String createGetBlockApiUri(final long dealId) {
        return UriComponentsBuilder.fromUriString(BlockChainServer)
                .path("/blockchain")
                .queryParam("dealId",dealId)
                .encode()
                .toUriString();
    }

}
