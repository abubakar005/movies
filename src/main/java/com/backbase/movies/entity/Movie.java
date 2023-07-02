package com.backbase.movies.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "Movies")
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imdId;

    @Column(unique = true)
    private String title;

    @Column
    private String released;

    @Column
    private String genre;

    @Column
    private String actors;

    @Column
    private String awards;

    @Column(columnDefinition = "DECIMAL(3, 1)")
    private BigDecimal rating;

    @Column
    private Long votes;

    @Column
    private Long boxOffice;

    @Column
    private Boolean oscarWon;
}
