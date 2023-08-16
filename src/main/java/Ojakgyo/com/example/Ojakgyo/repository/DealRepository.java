package Ojakgyo.com.example.Ojakgyo.repository;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.dto.DealListInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DealRepository extends JpaRepository<Deal,Long> {
    @Query(value = "SELECT deal_status, item, update_at FROM deal WHERE seller_id = :id OR buyer_id =:id ", nativeQuery = true)
    List<DealListInterface> findDealListById(long id);
}
