package com.project_lions.back.repository;


import com.project_lions.back.domain.Shop;
import org.locationtech.jts.geom.Point;

import java.util.List;

public interface ShopRepositoryCustom {
    List<Shop> findShopsWithinRadius(Point location, double radius);
}
