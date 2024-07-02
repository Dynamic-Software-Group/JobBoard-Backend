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

    @ElementCollection(targetClass = Tags.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_tags", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tags")
    private List<Tags> tags;

    @OneToMany(mappedBy = "bookmarked", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<JobPost> bookmarkedPosts;

}
