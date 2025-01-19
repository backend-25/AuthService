package com.example.auth_related.Service;
import com.example.auth_related.Exceptions.UserAlreadyPresent;
import com.example.auth_related.Exceptions.UserNotFound;
import com.example.auth_related.Exceptions.WrongPassword;
import com.example.auth_related.Models.User;
import com.example.auth_related.UserRepository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService
{
    private UserRepo userRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public AuthService(UserRepo userRepo,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
    }

    public boolean  SignUP(String email, String password) throws UserAlreadyPresent
    {
       if(userRepo.findByEmail(email).isPresent())
       {
           throw new UserAlreadyPresent("User with email"+email+"already presemt");
       }
       else{
           User user= new User();
           user.setEmail(email);

           user.setPassword(bCryptPasswordEncoder.encode(password)); //Need spring security as it is provided by spring
           userRepo.save(user);
           return true;
       }
    }


    public String Login(String email,String password) throws UserNotFound,WrongPassword
    {
        Optional<User> optionalUser=userRepo.findByEmail(email);
        if(optionalUser.isEmpty())
        {
            throw  new UserNotFound("User with email"+email+" not found");
        }
        String token="JWTTOKEN";
        boolean passwordStatus= bCryptPasswordEncoder.matches(password,optionalUser.get().getPassword());
        if(passwordStatus){
            return token;
        }
        throw new WrongPassword("password mismatch");


    }

}
