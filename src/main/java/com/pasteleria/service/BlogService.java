package com.pasteleria.service;

import com.pasteleria.model.Blog;
import com.pasteleria.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Blog updateStatus(Long id, String status) {
        return blogRepository.findById(id)
                .map(blog -> {
                    blog.setStatus(status);
                    return blogRepository.save(blog);
                })
                .orElseThrow(() -> new RuntimeException("Blog no encontrado con ID: " + id));
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
