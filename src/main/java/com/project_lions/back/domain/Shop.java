package com.project_lions.back.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project_lions.back.domain.enums.Like;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String shopName; //업체명

    @Column(nullable = false)
    private String ownerPhone; //연락처

    @Column(nullable = false)
    private String ownerName; //대표명

    private String description; //소개글

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openAt; //오픈시간

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeAt;//마감시간

    @Column(columnDefinition = "POINT SRID 4326")
    @JsonIgnore
    private Point location;  //주소

    @Column(nullable = false)
    private String image;

    @ManyToMany
    @Builder.Default
    private Set<Tag> shopTags=new HashSet<>();  //카테고리

    @ManyToOne
    @JoinColumn(name = "owner", nullable = true)
    private Member owner;  // 소품샵 주인

    @ManyToMany(mappedBy = "likeshops", cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @Column(nullable = false)
    @JsonIgnore
    private List<Member> memberLikeShop = new ArrayList<>();// 찜한 소품샵

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Like likeShop;  //찜 여부

    @PreRemove
    private void preRemove() {
        for (Member member : memberLikeShop) {
            member.getLikeshops().remove(this); // Member의 찜 목록에서 제거
        }
        memberLikeShop.clear();
    }

}
