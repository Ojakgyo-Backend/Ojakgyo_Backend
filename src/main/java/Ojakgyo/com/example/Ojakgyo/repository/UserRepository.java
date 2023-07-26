package Ojakgyo.com.example.Ojakgyo.repository;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByPhone(String phone);
}
