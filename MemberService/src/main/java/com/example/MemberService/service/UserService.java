package com.example.MemberService.service;

import com.example.MemberService.entity.UserEntity;
import com.example.MemberService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity registerUser(String loginId, String userName) {
        return userRepository.save(
                UserEntity.builder()
                        .loginId(loginId)
                        .userName(userName).build());
    }

    public UserEntity modifyUser(Long userId, String userName) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        user.setUserName(userName);
        return userRepository.save(user);
    }

    public UserEntity getUserByLoginId(String loginId) {

        return userRepository.findByLoginId(loginId).orElseThrow();
    }

}
