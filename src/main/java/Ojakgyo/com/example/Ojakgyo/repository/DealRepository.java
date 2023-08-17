package Ojakgyo.com.example.Ojakgyo.repository;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.dto.DealListInterface;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealRepository extends JpaRepository<Deal,Long> {
//    @Query(value = "SELECT d.deal_status, d.item, d.update_at FROM Deal d WHERE d.seller_id = :id OR d.buyer_id =:id ", nativeQuery = true)
    List<DealListInterface> findBySellerId(Long id);
    List<DealListInterface> findByBuyerId(Long id);
    Deal findDealById(long dealId);
}