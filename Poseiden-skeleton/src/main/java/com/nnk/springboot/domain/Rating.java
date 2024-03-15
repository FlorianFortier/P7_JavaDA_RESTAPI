package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

    @Column(name = "moodys_rating")
    private String moodysRating;

    @Column(name = "sandp_rating")
    private String sandPRating;

    @Column(name = "fitch_rating")
    private String fitchRating;

    @Column(name = "order_number")
    private Integer orderNumber;

    // Constructor with arguments
    public Rating(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }
}
