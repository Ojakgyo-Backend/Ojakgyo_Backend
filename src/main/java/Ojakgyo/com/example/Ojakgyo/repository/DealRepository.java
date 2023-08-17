package Ojakgyo.com.example.Ojakgyo.repository;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.domain.DealerDealListInterface;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealRepository extends JpaRepository<Deal,Long> {
//    @Query(value = "SELECT d.deal_status, d.item, d.update_at FROM Deal d WHERE d.seller_id = :id OR d.buyer_id =:id ", nativeQuery = true)
    List<DealerDealListInterface> findBySellerId(Long id);
    List<DealerDealListInterface> findByBuyerId(Long id);
    Deal findDealById(long dealId);
}