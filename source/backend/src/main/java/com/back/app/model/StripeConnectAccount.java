package com.back.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "stripe_connect_account")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StripeConnectAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stripe_connect_account_id")
    private Integer id;

    @Column(name = "account_id", unique = true, nullable = false)
    private Integer accountId;

    @Column(name = "stripe_account_id", unique = true, nullable = false)
    private String stripeAccountId;

    @Column(name = "account_status")
    private String accountStatus; // "pending", "active", "restricted", "disabled"

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
