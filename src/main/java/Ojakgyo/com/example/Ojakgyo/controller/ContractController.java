package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.dto.ContractDetailResponse;
import Ojakgyo.com.example.Ojakgyo.dto.BlockChainContract;
import Ojakgyo.com.example.Ojakgyo.dto.SignatureRequest;
import Ojakgyo.com.example.Ojakgyo.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contract")
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public Long createContract(Authentication authentication,@RequestBody BlockChainContract request) throws IOException {
        Long contractId = contractService.createContract(request);
        return contractId;
    }

    /** 사용자 서명 받기 */
    @PostMapping("/signature")
    public String saveSignature(Authentication authentication, @RequestBody SignatureRequest request){
         String SignatureCreatAt = contractService.saveSignature(request);
         return SignatureCreatAt;
    }

    @GetMapping(value ="/details", produces = "application/json; charset=UTF-8")
    public ContractDetailResponse getDetails(Authentication authentication, @RequestParam Long contractId){
        ContractDetailResponse contractDetailResponse = contractService.findById(contractId);
        return contractDetailResponse;
    }

    @GetMapping(value = "/block-test", produces = "application/json; charset=UTF-8")
    public BlockChainContract testBlock(@RequestParam Long dealId) throws Exception {
        return contractService.testBlock(dealId);
    }

    /**
     * 아래는 api 불러오기 성공하면 수정
     */
//    @PostMapping(value ="/blockchain", produces = "application/json; charset=UTF-8")
//    public Object saveBlock(Authentication authentication,@RequestBody BlockChainContract request) throws Exception {
//        contractService.saveBlock(request);
//        return Map.of("result", "계약서 생성 성공");
//    }
//
//
//    @GetMapping(value ="/blockchain/details", produces = "application/json; charset=UTF-8")
//    public ContractDetailResponse compareContract(Authentication authentication, @RequestParam Long dealId, Long contractId) throws Exception {
//        ContractDetailResponse contractDetailResponse = contractService.compareBlock(dealId,contractId);
//        return contractDetailResponse;
//    }
}
