package com.example.projectsns.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingFields {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Column
    private LocalDateTime deletedAt;

    @PrePersist
    void createdAt() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void updatedAt() {
        modifiedAt = LocalDateTime.now();
    }
}
