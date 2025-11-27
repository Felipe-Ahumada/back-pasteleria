package com.pasteleria.service;

import com.pasteleria.model.BlogLike;
import com.pasteleria.repository.BlogLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlogLikeService {

    @Autowired
    private BlogLikeRepository blogLikeRepository;

    public boolean toggleLike(Long blogId, Long userId) {
        Optional<BlogLike> existingLike = blogLikeRepository.findByBlogIdAndUserId(blogId, userId);
        if (existingLike.isPresent()) {
            blogLikeRepository.delete(existingLike.get());
            return false; // Like removed
        } else {
            BlogLike like = new BlogLike();
            like.setBlogId(blogId);
            like.setUserId(userId);
            blogLikeRepository.save(like);
            return true; // Like added
        }
    }

    public long countLikes(Long blogId) {
        return blogLikeRepository.countByBlogId(blogId);
    }

    public boolean hasLiked(Long blogId, Long userId) {
        return blogLikeRepository.existsByBlogIdAndUserId(blogId, userId);
    }
}
