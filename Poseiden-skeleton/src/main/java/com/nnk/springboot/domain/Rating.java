package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "rating")
@Getter
@Setter
@RequiredArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer  id;

    @Column(name = "moodys_rating", nullable = false)
    @NotNull
    private String moodysRating;

    @Column(name = "sandp_rating", nullable = false)
    @NotNull
    private String sandPRating;

    @Column(name = "fitch_rating", nullable = false)
    @NotNull
    private String fitchRating;

    @Column(name = "order_number", nullable = false)
    @NotNull
    private Integer orderNumber;

    public Rating(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }
}
