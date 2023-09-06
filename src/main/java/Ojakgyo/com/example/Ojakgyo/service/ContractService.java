package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.BlockChain.BlockChain;
import Ojakgyo.com.example.Ojakgyo.domain.Contract;
import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.dto.ContractDetailResponse;
import Ojakgyo.com.example.Ojakgyo.dto.BlockChainContract;
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
    private final BlockChain blockChain;

    public void createContract(BlockChainContract request) throws IOException {
        Deal deal = dealService.findById(request.getDealId());
        if(deal.getContract() != null){
            throw new NoSuchDataException(ErrorCode.DUPLICATED_CONTRACT);
        }
        Contract contract = Ojakgyo.com.example.Ojakgyo.domain.Contract.builder()
                .repAndRes(request.getRepAndRes())
                .note(request.getNote())
                .build();
        deal.updateContract(contract);
        dealRepository.save(deal);
    }

    public void saveBlock(BlockChainContract request) throws Exception {
        Deal deal = dealService.findById(request.getDealId());
        if(deal.getContract() != null){
            throw new NoSuchDataException(ErrorCode.DUPLICATED_CONTRACT);
        }
//        Contract contract = Ojakgyo.com.example.Ojakgyo.domain.Contract.builder()
//                .repAndRes(request.getRepAndRes())
//                .note(request.getNote())
//                .build();
//        deal.updateContract(contract);
//        dealRepository.save(deal);
        blockChain.createContract(request);

    }

    public Long saveSignature(SignatureRequest request) {
        Contract contract = contractRepository.findById(request.getContractId()).get();
        contract.setSignature(request.getIsSeller(), request.getSignature());
        return contractRepository.save(contract).getId();
    }

    public ContractDetailResponse findById(Long contractId) {
        Contract contract = contractRepository.findById(contractId).get();

        ContractDetailResponse contractDetailResponse = ContractDetailResponse.builder()
                .repAndRes(contract.getRepAndRes())
                .note(contract.getNote())
                .buyerSignature(contract.getBuyerSignature())
                .sellerSignature(contract.getSellerSignature()).build();
        return contractDetailResponse;
    }

    public ContractDetailResponse compareBlock(Long dealId , Long contractId) throws Exception {
        Contract contract = contractRepository.findById(contractId).get();
        BlockChainContract blockChainContract = blockChain.getContract(dealId);

        /** 변조 비교 */
        if(contract.getRepAndRes().equals(blockChainContract.getRepAndRes())){
            throw new NoSuchDataException(ErrorCode.ALTERED_REP_RES);
        } else if (contract.getNote().equals(blockChainContract.getNote())) {
            throw new NoSuchDataException(ErrorCode.ALTERED_NOTE);
        }

        ContractDetailResponse contractDetailResponse = ContractDetailResponse.builder()
                .repAndRes(contract.getRepAndRes())
                .note(contract.getNote())
                .buyerSignature(contract.getBuyerSignature())
                .sellerSignature(contract.getSellerSignature()).build();
        return contractDetailResponse;
    }
}
