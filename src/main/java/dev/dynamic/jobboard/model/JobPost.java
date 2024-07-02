package dev.dynamic.jobboard.model;

import dev.dynamic.jobboard.model.enums.EmploymentType;
import dev.dynamic.jobboard.model.enums.LocationType;
import dev.dynamic.jobboard.model.enums.Tags;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "job_posts")
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type")
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type")
    private LocationType locationType;

    @Column
    private String location;

    @Column
    private String currency;

    @Column
    private int maxSalary;

    @Column
    private int minSalary;

    @ElementCollection(targetClass = Tags.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tags")
    private List<Tags> tags;

    @Column
    private int applicants;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToMany(mappedBy = "bookmarkedPosts")
    private List<User> bookmarkedBy;
}
