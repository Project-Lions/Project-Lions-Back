package com.project_lions.back.repository;

import com.project_lions.back.domain.Review;
import com.project_lions.back.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByShop(Shop shop);

}
