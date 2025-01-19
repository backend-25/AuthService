package com.example.auth_related.UserRepository;

import com.example.auth_related.Models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepo extends JpaRepository<Session,Long>
{
    Session save(Session session);
}
