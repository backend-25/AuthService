package com.example.auth_related.Controller;

import com.example.auth_related.Dtos.*;
import com.example.auth_related.Exceptions.UserAlreadyPresent;
import com.example.auth_related.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/Sign_up")
    public ResponseEntity<SignUpResponseDto> SignUp(@RequestBody SignUpRequestDto signUpRequestDto)
    {
        SignUpResponseDto responseDto = new SignUpResponseDto();
        try
        {
            if(authService.SignUP(signUpRequestDto.getEmail(),signUpRequestDto.getPassword()))
            {
                responseDto.setRequestStatus(RequestStatus.SUCCESS);
                responseDto.setSignupMsg("User has been Created with Email:"+signUpRequestDto.getEmail());
            }

        } catch (UserAlreadyPresent e)
        {
            responseDto.setRequestStatus(RequestStatus.FAILURE);
            responseDto.setSignupMsg("User is already exist with Email"+signUpRequestDto.getEmail());
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> Login(@RequestBody LoginRequestDto loginRequestDto)
    {
        String token=authService.Login(loginRequestDto.getEmail(),loginRequestDto.getPassword());

        LoginResponseDto loginResponseDto=new LoginResponseDto();
        loginResponseDto.setRequestStatus(RequestStatus.SUCCESS);
        MultiValueMap<String,String> header= new LinkedMultiValueMap<>();
        header.add("AUTH TOKEN",token);
        ResponseEntity<LoginResponseDto> responseEntity=new ResponseEntity(loginResponseDto,header,HttpStatus.OK);
        return responseEntity;


    }

}
