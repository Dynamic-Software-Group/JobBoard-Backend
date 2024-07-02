package dev.dynamic.jobboard.model;

import dev.dynamic.jobboard.model.enums.EmploymentType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Entity
@Table(name = "experiences")
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @Column
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type")
    private EmploymentType employmentType;

    @Column
    private Long startDate;

    @Column
    private Long endDate;

    @Column
    private String description;
}
