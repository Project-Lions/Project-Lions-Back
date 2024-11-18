package com.project_lions.back.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 782150407L;

    public static final QMember member = new QMember("member1");

    public final com.project_lions.back.domain.common.QBaseEntity _super = new com.project_lions.back.domain.common.QBaseEntity(this);

    public final StringPath address = createString("address");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created_at = _super.created_at;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Shop, QShop> likeshops = this.<Shop, QShop>createList("likeshops", Shop.class, QShop.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath refreshToken = createString("refreshToken");

    public final EnumPath<com.project_lions.back.domain.enums.Role> role = createEnum("role", com.project_lions.back.domain.enums.Role.class);

    public final ListPath<Shop, QShop> shops = this.<Shop, QShop>createList("shops", Shop.class, QShop.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated_at = _super.updated_at;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

