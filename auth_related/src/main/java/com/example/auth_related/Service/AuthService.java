package com.example.auth_related.Service;
import com.example.auth_related.Exceptions.UserAlreadyPresent;
import com.example.auth_related.Models.User;
import com.example.auth_related.UserRepository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService
{
    private UserRepo userRepo;


    public AuthService(UserRepo userRepo) {
        this.userRepo = userRepo;
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

           user.setPassword(password);
           userRepo.save(user);
           return true;
       }
    }


    public String Login(String email,String password)
    {

        return null;
    }

}
