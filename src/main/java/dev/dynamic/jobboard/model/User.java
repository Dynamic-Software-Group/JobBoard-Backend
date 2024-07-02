package dev.dynamic.jobboard.model;

import dev.dynamic.jobboard.model.enums.Tags;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String avatar;

    @Column
    private String bio;

    @ElementCollection(targetClass = Tags.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_tags", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tags")
    private List<Tags> tags;

    @ManyToMany
    @JoinTable(
            name = "user_bookmarked_posts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<JobPost> bookmarkedPosts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_business",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "business_id")
    )
    private List<Business> businesses;
}
