package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.domain.LockerStatus;
import Ojakgyo.com.example.Ojakgyo.dto.SearchLockerResponse;
import Ojakgyo.com.example.Ojakgyo.exception.ErrorCode;
import Ojakgyo.com.example.Ojakgyo.exception.NoSuchDataException;
import Ojakgyo.com.example.Ojakgyo.repository.LockerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LockerService {
    private final LockerRepository lockerRepository;

    public Locker createLocker(String address, String password) {
        Locker locker = Locker.builder()
                .lockerStatus(LockerStatus.NOT_IN_USE)
                .address(address)
                .password(password)
                .createLockerPwdAt(LocalDateTime.now()).build();
        return lockerRepository.save(locker);
    }

    public List<SearchLockerResponse> findAll() {
        List<Locker> lockerList = lockerRepository.findAll();
        List<SearchLockerResponse> lockerRes = new ArrayList<>();
        for (Locker locker : lockerList) {
            SearchLockerResponse searchLockerResponse = SearchLockerResponse.builder()
                    .lockerId(locker.getId())
                    .address(locker.getAddress()).build();
            lockerRes.add(searchLockerResponse);
        }
        return lockerRes;
    }

    public void saveLocker(Locker locker) {
        lockerRepository.save(locker);
    }

    // 락커 아이디로 락커 찾기
    public Locker findById(Long lockerId) {
        return lockerRepository.findById(lockerId).get();
    }

    public List<SearchLockerResponse> findByAddress(String address) {
        List<Locker> findLocker = lockerRepository.findByAddress(address);
        if (findLocker == null) {
            throw new NoSuchDataException(ErrorCode.LOCKER_NOT_EXIST);
        }

        List<SearchLockerResponse> searchLockers = new ArrayList<>();
        for (Locker locker : findLocker) {
            SearchLockerResponse searchLockeresponse = SearchLockerResponse.builder()
                    .lockerId(locker.getId())
                    .address(locker.getAddress())
                    .build();
            searchLockers.add(searchLockeresponse);
        }

        return searchLockers;
    }


}


