package com.azurealstn.alog.domain.hashtag;

import com.azurealstn.alog.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hashtag")
@Entity
public class HashTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Builder
    public HashTag(String name) {
        this.name = name;
    }
}
