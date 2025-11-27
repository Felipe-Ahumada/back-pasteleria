package com.pasteleria.service;

import com.pasteleria.model.Comentario;
import com.pasteleria.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    public List<Comentario> getAllComentarios() {
        return comentarioRepository.findAll();
    }

    public List<Comentario> getComentariosByBlogId(String blogId) {
        return comentarioRepository.findByBlogId(blogId);
    }

    public Comentario createComentario(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    public void deleteComentario(Long id) {
        comentarioRepository.deleteById(id);
    }
}
