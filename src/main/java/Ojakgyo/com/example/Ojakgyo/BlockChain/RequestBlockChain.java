package Ojakgyo.com.example.Ojakgyo.BlockChain;

import Ojakgyo.com.example.Ojakgyo.dto.BlockChainContract;
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

    public BlockChainContract requestBlock(long dealId) {
        return restTemplate.exchange(createApiUri(dealId),
                        HttpMethod.GET,
                        new HttpEntity<>(createHttpHeaders()),
                        BlockChainContract.class)
                .getBody();
    }

    private String createApiUri(final long dealId) {
        return UriComponentsBuilder.fromUriString(BlockChainServer)
                .path("/test")
                .queryParam("dealId",dealId)
                .encode()
                .toUriString();
    }

    private HttpHeaders createHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        return headers;
    }
}
