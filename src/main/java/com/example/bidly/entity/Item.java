package com.example.bidly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;
    private String name;
    private String description;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "approved")
    private boolean isApproved;

    @Column(name = "winnerid")
    private int winner;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser owner;

    @Column(name = "image", columnDefinition = "BYTEA")
    private byte[] image;
    private Double startingBid;

    @Column(name = "end_time")
    private LocalDateTime endTime;

}

