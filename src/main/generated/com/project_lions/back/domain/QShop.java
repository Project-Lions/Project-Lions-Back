package com.project_lions.back.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShop is a Querydsl query type for Shop
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShop extends EntityPathBase<Shop> {

    private static final long serialVersionUID = -1232522589L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShop shop = new QShop("shop");

    public final TimePath<java.time.LocalTime> closeAt = createTime("closeAt", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final EnumPath<com.project_lions.back.domain.enums.Like> likeShop = createEnum("likeShop", com.project_lions.back.domain.enums.Like.class);

    public final ComparablePath<org.locationtech.jts.geom.Point> location = createComparable("location", org.locationtech.jts.geom.Point.class);

    public final ListPath<Member, QMember> memberLikeShop = this.<Member, QMember>createList("memberLikeShop", Member.class, QMember.class, PathInits.DIRECT2);

    public final TimePath<java.time.LocalTime> openAt = createTime("openAt", java.time.LocalTime.class);

    public final QMember owner;

    public final StringPath ownerName = createString("ownerName");

    public final StringPath ownerPhone = createString("ownerPhone");

    public final StringPath shopName = createString("shopName");

    public final SetPath<Tag, QTag> shopTags = this.<Tag, QTag>createSet("shopTags", Tag.class, QTag.class, PathInits.DIRECT2);

    public QShop(String variable) {
        this(Shop.class, forVariable(variable), INITS);
    }

    public QShop(Path<? extends Shop> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShop(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShop(PathMetadata metadata, PathInits inits) {
        this(Shop.class, metadata, inits);
    }

    public QShop(Class<? extends Shop> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new QMember(forProperty("owner")) : null;
    }

}

