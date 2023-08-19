package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.RegisterDealRequest;
import Ojakgyo.com.example.Ojakgyo.dto.RegisterLockerRequest;
import Ojakgyo.com.example.Ojakgyo.service.LockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/locker")
public class LockerController {

    private final LockerService lockerService;

    @PostMapping
    public Object registerLocker(Authentication authentication, @RequestBody RegisterLockerRequest request) throws IOException {
        lockerService.createLocker(request.getAddress(),request.getPassword());
        return Map.of("result", "락커 생성 성공");
    }

}
