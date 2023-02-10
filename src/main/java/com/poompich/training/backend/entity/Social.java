package com.poompich.training.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "m_social")
public class Social extends BaseEntity {
    @Column(nullable = false, unique = true, length = 60)
    private String facebook;

    @Column(nullable = false, length = 120)
    private String line;

    @Column(nullable = false, length = 120)
    private String instagram;

    @Column(length = 120)
    private String tiktok;

    @OneToOne
    @JoinColumn(name = "m_user_id", nullable = false)
    private User user;
}
