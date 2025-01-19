package com.example.auth_related.Service;
import com.example.auth_related.Exceptions.UserAlreadyPresent;
import com.example.auth_related.Exceptions.UserNotFound;
import com.example.auth_related.Exceptions.WrongPassword;
import com.example.auth_related.Models.Session;
import com.example.auth_related.Models.SessionStatus;
import com.example.auth_related.Models.User;
import com.example.auth_related.UserRepository.SessionRepo;
import com.example.auth_related.UserRepository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;

@Service
public class AuthService
{
    // Generate a secret key for the HS256 algorithm
    private SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private UserRepo userRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SessionRepo sessionRepo;


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

        String token=generateJWT(optionalUser.get().getId(),new ArrayList<>(),optionalUser.get().getEmail());

        Session session=new Session();
        session.setToken(token);
        session.setUser(optionalUser.get());
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + 3 * 60 * 1000); // 3 minutes in milliseconds
        session.setExpiringAt(expirationTime);
        session.setSessionStatus(SessionStatus.ACTIVE); // Set the session status to ACTIVE by default
        sessionRepo.save(session);


        boolean passwordStatus= bCryptPasswordEncoder.matches(password,optionalUser.get().getPassword());
        if(passwordStatus){
            return token;
        }
        throw new WrongPassword("password mismatch");


    }

    public String generateJWT(Long id, List<String> roles, String email)
    {
        // Set expiration time (30 minutes from now)
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + 3 * 60 * 1000); // 3 minutes in milliseconds

        Map<String, Object> DataInJwt = new HashMap<>();
        DataInJwt.put("ID",id);
        DataInJwt.put("roles",roles);
        DataInJwt.put("email",email);

        String JWTtoken= Jwts.builder()
                .addClaims(DataInJwt)
                .setExpiration(expirationTime)
                .setIssuedAt(now)
                .signWith(key)
                .compact();
        return JWTtoken;

    }
    public boolean validateToken(String token)
    {
        // Parse the token to check its validity
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

}
