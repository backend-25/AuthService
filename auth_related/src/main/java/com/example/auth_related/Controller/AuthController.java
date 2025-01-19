package com.example.auth_related.Controller;

import com.example.auth_related.Dtos.*;
import com.example.auth_related.Exceptions.UserAlreadyPresent;
import com.example.auth_related.Exceptions.UserNotFound;
import com.example.auth_related.Exceptions.WrongPassword;
import com.example.auth_related.Service.AuthService;
import jakarta.persistence.GeneratedValue;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

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
            responseDto.setSignupMsg(String.valueOf(e));
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> Login(@RequestBody LoginRequestDto loginRequestDto)
    {
        LoginResponseDto loginResponseDto=new LoginResponseDto();
        String token= null;
        try {
            token = authService.Login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
        }
        catch (Exception e)
        {
            loginResponseDto.setRequestStatus(RequestStatus.FAILURE);
            loginResponseDto.setLoginMessage(String.valueOf(e));
            return new ResponseEntity<>(loginResponseDto,HttpStatus.NOT_FOUND);
        }

        loginResponseDto.setRequestStatus(RequestStatus.SUCCESS);
        loginResponseDto.setLoginMessage("Login Successfully");
        MultiValueMap<String,String> header= new LinkedMultiValueMap<>();
        header.add("AUTH TOKEN",token);
        ResponseEntity<LoginResponseDto> responseEntity=new ResponseEntity(loginResponseDto,header,HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/validate")
    public boolean Validate(@RequestParam("token") String token)
    {
        return authService.validateToken(token);
    }

}
