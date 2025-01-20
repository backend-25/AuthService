package com.example.auth_related.Security;

import com.example.auth_related.Models.User;
import com.example.auth_related.UserRepository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
    private UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
       Optional<User> optionalUser =userRepo.findByEmail(username);
        if(optionalUser.isEmpty())
        {
            throw new UsernameNotFoundException("User Not found");
        }
        else
        {
            return new CustomUserDetails(optionalUser.get());
        }
    }
}
