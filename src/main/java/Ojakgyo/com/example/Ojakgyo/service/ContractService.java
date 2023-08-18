package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Contract;
import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.dto.ContractDetailResponse;
import Ojakgyo.com.example.Ojakgyo.dto.CreateContractRequest;
import Ojakgyo.com.example.Ojakgyo.dto.SignatureRequest;
import Ojakgyo.com.example.Ojakgyo.exception.ErrorCode;
import Ojakgyo.com.example.Ojakgyo.exception.NoSuchDataException;
import Ojakgyo.com.example.Ojakgyo.repository.ContractRepository;
import Ojakgyo.com.example.Ojakgyo.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final DealService dealService;
    private final DealRepository dealRepository;

    public void createContract(CreateContractRequest request) throws IOException {
        Deal deal = dealService.findById(request.getDealId());
        if(deal.getContract() != null){
            throw new NoSuchDataException(ErrorCode.DUPLICATED_CONTRACT);
        }
        Contract contract = Contract.builder()
                .repAndRes(request.getRepAndRes())
                .note(request.getNote())
                .build();
        deal.updateContract(contract);
        dealRepository.save(deal);
    }

    public Long saveSignature(SignatureRequest request) {
        Contract contract = contractRepository.findById(request.getContractId()).get();
        contract.setSignature(request.getIsSeller(), request.getSignature());
        return contractRepository.save(contract).getId();
    }

    public ContractDetailResponse findById(Long contractId) {
        Contract contract = contractRepository.findById(contractId).get();
        System.out.println("contract.getId() = " + contract.getId());
        ContractDetailResponse contractDetailResponse = ContractDetailResponse.builder()
                .repAndRes(contract.getRepAndRes())
                .note(contract.getNote())
                .buyerSignature(contract.getBuyerSignature())
                .sellerSignature(contract.getSellerSignature()).build();
        return contractDetailResponse;
    }
}
