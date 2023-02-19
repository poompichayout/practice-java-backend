package com.poompich.training.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "m_address")
public class Address extends BaseEntity implements Serializable {
    @Column(length = 120)
    private String line1;

    @Column(length = 120)
    private String line2;

    @Column(length = 120)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "m_user_id", nullable = false)
    private User user;

}
