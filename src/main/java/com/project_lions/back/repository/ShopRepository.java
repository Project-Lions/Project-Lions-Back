package com.project_lions.back.repository;


import com.project_lions.back.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ShopRepository extends JpaRepository<Shop, Long>, ShopRepositoryCustom {
    Boolean existsByShopName(String shopName);
    Optional<Shop> findById(Long id);
    Optional<Shop> findByShopName(String shopName);

    @Query("SELECT s FROM Shop s JOIN s.memberLikeShop m WHERE m.id=:memberId")
    Optional<List<Shop>> findLikeShops(Long memberId);
}
