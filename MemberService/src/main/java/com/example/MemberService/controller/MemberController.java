package com.example.MemberService.controller;

import com.example.MemberService.dto.ModifyUserDto;
import com.example.MemberService.dto.RegisterUserDto;
import com.example.MemberService.entity.UserEntity;
import com.example.MemberService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/users")
public class MemberController {

    private final UserService userService;

    @PostMapping("/registration")
    public UserEntity registerUser(@RequestBody RegisterUserDto dto) {
       return userService.registerUser(dto.loginId, dto.userName);
    }

    @PostMapping("/{userId}/modify")
    public UserEntity modifyUser(@PathVariable(value = "userId") Long userId, @RequestBody ModifyUserDto dto) {
        return userService.modifyUser(userId, dto.userName);
    }


    @PostMapping("/{loginId}/login")
    public UserEntity login(@PathVariable(value = "loginId") String loginId) {
        return userService.getUserByLoginId(loginId);
    }


}
