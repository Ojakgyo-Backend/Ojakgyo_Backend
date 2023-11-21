package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.BlockChain.RequestBlockChain;
import Ojakgyo.com.example.Ojakgyo.domain.Contract;
import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static Ojakgyo.com.example.Ojakgyo.domain.Utils.changeDateFormat;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final DealService dealService;
    private final DealRepository dealRepository;
    private final RequestBlockChain requestBlockChain;

    public Long saveBlock(BlockChainContract request) throws Exception {
        Deal deal = dealService.findById(request.getDealId());
        if(deal.getContract() != null){
            throw new NoSuchDataException(ErrorCode.DUPLICATED_CONTRACT);
        }

        Contract contract = Contract.builder()
                .repAndRes(request.getRepAndRes())
                .note(request.getNote())
                .build();

        requestBlockChain.requestSaveBlock(request);
        deal.updateContract(contract);
        dealRepository.save(deal);
        return deal.getContract().getId();
    }

    public void deleteContract(long dealId){
        if (isPresentContract(dealId)){
            contractRepository.deleteById(dealService.findById(dealId).getContract().getId());
        }
    }

    private boolean isPresentContract(long dealId) {
        Optional<Contract> contract = Optional.ofNullable(dealService.findById(dealId).getContract());
        return contract.isPresent();
    }

    public String saveSignature(SignatureRequest request) {
        Contract contract = contractRepository.findById(request.getContractId()).get();
        contract.setSignature(request.getIsSeller(), request.getSignature());

        final LocalDateTime current = LocalDateTime.now();
        contract.setSignatureCreatAt(request.getIsSeller(), current);

        // 내가 구매자라면 구매자가 아닌 판매자(상대방)의 서명이 저장되어 있는지 확인
        if (contract.isDealerSignatureSaved(!request.getIsSeller())) {
            Deal deal = contract.getDeal();
            deal.updateDealStatus(DealStatus.DEALING);
            dealRepository.save(deal);
        }

        contractRepository.save(contract);
        return changeDateFormat(current);
    }

    public ContractDetailResponse getContract(Long contractId) throws Exception {
        Contract contract = contractRepository.findById(contractId).get();
        Long dealId = contract.getDeal().getId();
        BlockChainContract blockChainContract = requestBlockChain.requestGetBlock(dealId);

        return ContractDetailResponse.builder()
                .repAndRes(blockChainContract.getRepAndRes())
                .note(blockChainContract.getNote())
                .buyerSignature(contract.getBuyerSignature())
                .sellerSignature(contract.getSellerSignature())
                .buyerSignatureCreatAt(changeDateFormat(contract.getBuyerSignatureCreateAt()))
                .sellerSignatureCreatAt(changeDateFormat(contract.getSellerSignatureCreateAt()))
                .build();
    }


    /**
     * 블록체인 연결 없이 db에만 컨트렉트 저장 (블록체인 연결 전 테스트 용)
     */
//    public Long createContract(BlockChainContract request) throws IOException {
//        Deal deal = dealService.findById(request.getDealId());
//        if(deal.getContract() != null){
//            throw new NoSuchDataException(ErrorCode.DUPLICATED_CONTRACT);
//        }
//
//        Contract contract = Contract.builder()
//                .repAndRes(request.getRepAndRes())
//                .note(request.getNote())
//                .build();
//
//        deal.updateContract(contract);
//        dealRepository.save(deal);
//        return deal.getContract().getId();
//    }

    /**
     * 블록체인 연결 없이 db에만 요청 컨트렉트 조회 (블록체인 연결 전 테스트 용)
     */
//    public ContractDetailResponse findById(Long contractId) {
//        Contract contract = contractRepository.findById(contractId).get();
//
//        return ContractDetailResponse.builder()
//                .repAndRes(contract.getRepAndRes())
//                .note(contract.getNote())
//                .buyerSignature(contract.getBuyerSignature())
//                .sellerSignature(contract.getSellerSignature())
//                .buyerSignatureCreatAt(changeDateFormat(contract.getBuyerSignatureCreateAt()))
//                .sellerSignatureCreatAt(changeDateFormat(contract.getSellerSignatureCreateAt()))
//                .build();
//    }

    // 변조비교
//    public ContractDetailResponse compareBlock(Long dealId , Long contractId) throws Exception {
//        Contract contract = contractRepository.findById(contractId).get();
//        BlockChainContract blockChainContract = blockChain.getContract(dealId);
//
//        /** 변조 비교 */
//        if(contract.getRepAndRes().equals(blockChainContract.getRepAndRes())){
//            throw new NoSuchDataException(ErrorCode.ALTERED_REP_RES);
//        } else if (contract.getNote().equals(blockChainContract.getNote())) {
//            throw new NoSuchDataException(ErrorCode.ALTERED_NOTE);
//        }
//
//        return ContractDetailResponse.builder()
//                .repAndRes(contract.getRepAndRes())
//                .note(contract.getNote())
//                .buyerSignature(contract.getBuyerSignature())
//                .sellerSignature(contract.getSellerSignature()).build();
//    }
}
