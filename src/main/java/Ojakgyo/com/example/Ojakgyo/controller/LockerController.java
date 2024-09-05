package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.CheckLockerRequest;
import Ojakgyo.com.example.Ojakgyo.dto.RegisterDealRequest;
import Ojakgyo.com.example.Ojakgyo.dto.RegisterLockerRequest;
import Ojakgyo.com.example.Ojakgyo.dto.SearchLockerResponse;
import Ojakgyo.com.example.Ojakgyo.exception.ErrorCode;
import Ojakgyo.com.example.Ojakgyo.exception.NoSuchDataException;
import Ojakgyo.com.example.Ojakgyo.service.LockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/locker")
public class LockerController {

    private final LockerService lockerService;

    @PostMapping(produces = "application/json; charset=UTF-8")
    public Object registerLocker(Authentication authentication, @RequestBody RegisterLockerRequest request) throws IOException {
        lockerService.createLocker(request.getAddress(),request.getPassword());
        return Map.of("result", "락커 생성 성공");
    }


    @PostMapping(value ="/check-password",produces = "application/json; charset=UTF-8")
    public boolean checkPassword(@RequestBody CheckLockerRequest request) throws IOException {
        return lockerService.checkPassword(request.getLockerId(),request.getPassword());
    }


    @GetMapping(value = "/password", produces = "application/json; charset=UTF-8")
    public String getPassword(@RequestParam Long lockerId) {
        Locker findLocker = lockerService.findById(lockerId);
        return findLocker.getPassword();
    }

    // 락커 id로 조회
    @GetMapping(value ="/search-locker", produces = "application/json; charset=UTF-8")
    public SearchLockerResponse searchLocker(Authentication auth, @RequestParam Long lockerId){
        try {
            Locker findLocker = lockerService.findById(lockerId);
            // 검색한 락커 아이디가 없을 경우 에러 처리
            if (findLocker == null) {
                throw new NoSuchDataException(ErrorCode.LOCKER_NOT_EXIST);
            }
            return SearchLockerResponse.builder()
                    .lockerId(lockerId)
                    .address(findLocker.getAddress())
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    // 락커 주소로 조회
    @GetMapping(value ="/search-locker-address", produces = "application/json; charset=UTF-8")
    public List<SearchLockerResponse> searchLockerAddress(Authentication auth, @RequestParam String address){
        try {
            return lockerService.findByAddress(address);
            // 검색한 락커 주소가 없을 경우 에러 처리
        } catch (Exception e) {
            throw e;
        }
    }
}
