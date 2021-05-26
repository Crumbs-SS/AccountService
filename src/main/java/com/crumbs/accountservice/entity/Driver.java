package com.crumbs.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver implements Serializable {
        @Id
        @Column(name = "user_details_id", unique = true, nullable = false)
        private Integer id;

        @OneToOne
        @MapsId
        @JoinColumn(name = "user_details_id")
        @JsonBackReference
        @ToString.Exclude
        private UserDetails userDetails;

        @Column(nullable = false) @Size(max = 30)
        private String licenseId;
}

