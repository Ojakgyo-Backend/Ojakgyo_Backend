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

    public Long createContract(BlockChainContract request) throws IOException {
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

    public ContractDetailResponse findById(Long contractId) {
        Contract contract = contractRepository.findById(contractId).get();

        return ContractDetailResponse.builder()
                .repAndRes(contract.getRepAndRes())
                .note(contract.getNote())
                .buyerSignature(contract.getBuyerSignature())
                .sellerSignature(contract.getSellerSignature())
                .buyerSignatureCreatAt(changeDateFormat(contract.getBuyerSignatureCreateAt()))
                .sellerSignatureCreatAt(changeDateFormat(contract.getSellerSignatureCreateAt()))
                .build();
    }

    public BlockChainContract testBlock(long dealId) throws Exception {
        return requestBlockChain.requestBlock(dealId);
    }

    /**
     * 아래는 api 불러오기 성공하면 수정
     */
//    public void saveBlock(BlockChainContract request) throws Exception {
//        Deal deal = dealService.findById(request.getDealId());
//        if(deal.getContract() != null){
//            throw new NoSuchDataException(ErrorCode.DUPLICATED_CONTRACT);
//        }
////        Contract contract = Ojakgyo.com.example.Ojakgyo.domain.Contract.builder()
////                .repAndRes(request.getRepAndRes())
////                .note(request.getNote())
////                .build();
////        deal.updateContract(contract);
////        dealRepository.save(deal);
//        blockChain.createContract(request);
//
//    }
//
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
