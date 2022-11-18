package com.azurealstn.alog.domain.hashtag;

import com.azurealstn.alog.domain.BaseTimeEntity;
import com.azurealstn.alog.domain.posts.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "posts_hashtag_map")
@Entity
public class PostsHashTagMap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posts_hashtag_map_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private HashTag hashTag;

    @Builder
    public PostsHashTagMap(Posts posts, HashTag hashTag) {
        this.posts = posts;
        this.hashTag = hashTag;
    }
}
