package com.project_lions.back.domain;

import com.project_lions.back.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Table(name = "REVIEW")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {

    @Getter
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @NotNull
    @ManyToOne
    @JoinColumn(name = "MEMBER")
    private Member member;

    @Getter
    @NotNull
    @ManyToOne
    @JoinColumn(name = "SHOP")
    private Shop shop;

    @NotNull
    @Column(length = 200)
    private String title;
    private String body;

    @Column(nullable = false)
    private String image;

    public void updateTitle(@NotNull String title) {
        this.title = title;
    }
    public void updateBody(@NotNull String body) {
        this.body = body;
    }
    public void updateImage(@NotNull String image) {this.image = image;}
}
