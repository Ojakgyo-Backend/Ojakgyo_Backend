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
    public Long createContract(Authentication authentication, @RequestBody BlockChainContract request) throws Exception {
        Long contractId = contractService.saveBlock(request);
        return contractId;
    }

    /**
     * 사용자 서명 받기
     */
    @PostMapping("/signature")
    public String saveSignature(Authentication authentication, @RequestBody SignatureRequest request) {
        String SignatureCreatAt = contractService.saveSignature(request);
        return SignatureCreatAt;
    }

    @GetMapping(value = "/details", produces = "application/json; charset=UTF-8")
    public ContractDetailResponse getDetails(Authentication authentication, @RequestParam Long contractId) throws Exception {
        ContractDetailResponse contractDetailResponse = contractService.getContract(contractId);
        return contractDetailResponse;
    }
}