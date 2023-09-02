package Ojakgyo.com.example.Ojakgyo.repository;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.domain.DealerDealListInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DealRepository extends JpaRepository<Deal,Long> {
    List<DealerDealListInterface> findDealerDealListBySellerId(Long id);
    List<DealerDealListInterface> findDealerDealListByBuyerId(Long id);
    Deal findDealById(Long dealId);

    @Query(value = "SELECT * FROM deal d WHERE d.seller_id = :id OR d.buyer_id =:id ", nativeQuery = true)
    List<Deal> findAllByUserId(Long id);
}