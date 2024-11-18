package com.project_lions.back.repository;

import com.project_lions.back.domain.QShop;
import com.project_lions.back.domain.Shop;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.locationtech.jts.geom.Point;

import java.util.List;

public class ShopRepositoryImpl implements ShopRepositoryCustom {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Shop> findShopsWithinRadius(Point center, double radius) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QShop shop = QShop.shop;

        BooleanExpression withinRadius = com.querydsl.core.types.dsl.Expressions.booleanTemplate(
                "ST_Distance_Sphere({0}, {1}) <= {2}",
                shop.location, center, radius
        );

        return queryFactory.selectFrom(shop)
                .where(withinRadius)
                .fetch();
    }
}
