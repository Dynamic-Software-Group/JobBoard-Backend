package dev.dynamic.jobboard.model;

import dev.dynamic.jobboard.model.enums.Socials;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Entity
@Table(name = "businesses")
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String avatar;

    @Column
    private String businessEmail;

    @OneToOne(mappedBy = "business")
    private User owner;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<JobPost> posts;

    @Column
    private String website;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "business_socials", joinColumns = @JoinColumn(name = "business_id"))
    @MapKeyColumn(name = "social_platform")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "social_link")
    private Map<Socials, String> socials;

    @Column
    private String businessType;

}
