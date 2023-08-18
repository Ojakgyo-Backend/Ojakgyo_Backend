package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.domain.Contract;
import Ojakgyo.com.example.Ojakgyo.dto.ContractDetailResponse;
import Ojakgyo.com.example.Ojakgyo.dto.CreateContractRequest;
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
    public Object createContract(Authentication authentication,@RequestBody CreateContractRequest request) throws IOException {
        contractService.createContract(request);
        return Map.of("result", "계약서 생성 성공");
    }

    /** 사용자 서명 받기 */
    @PostMapping("/signature")
    public Long saveSignature(Authentication authentication, @RequestBody SignatureRequest request){
         Long contractId = contractService.saveSignature(request);
         return contractId;
    }

    @GetMapping("/details")
    public ContractDetailResponse getDetails(Authentication authentication, @RequestParam Long contractId){
        ContractDetailResponse contractDetailResponse = contractService.findById(contractId);
        return contractDetailResponse;
    }

}
