package Ojakgyo.com.example.Ojakgyo.repository;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal,Long> {
    Deal findByEmail(String email);
}
