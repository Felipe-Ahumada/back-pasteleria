package com.pasteleria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blog_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "blogId", "userId" })
})
public class BlogLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long blogId;

    @Column(nullable = false)
    private Long userId;
}
