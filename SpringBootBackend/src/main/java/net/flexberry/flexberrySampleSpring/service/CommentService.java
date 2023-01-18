package net.flexberry.flexberrySampleSpring.service;

import net.flexberry.flexberrySampleSpring.model.Comment;
import net.flexberry.flexberrySampleSpring.model.Customer;
import net.flexberry.flexberrySampleSpring.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository repository;

    public Comment getCommnet(UUID primarykey) {
        return repository.findById(primarykey).orElse(null);
    }

    public Comment saveOrUpdateComment(Comment comment) {
        return repository.save(comment);
    }

    public void deleteCommentByPrimaryKey(UUID primarykey) {
        repository.deleteById(primarykey);
    }

    // Возвращает комментарии в заданном диапазоне.
    public List<Comment> getCommentsForPeriod(Date beginDate, Date endDate) {
        return repository.findByCommentDateGreaterThanEqualAndCommentDateLessThanEqual(beginDate, endDate);
    }

    public List<Comment> getAllComments() {
        List<Comment> comments = repository.findAll();

        return comments;
    }
}
