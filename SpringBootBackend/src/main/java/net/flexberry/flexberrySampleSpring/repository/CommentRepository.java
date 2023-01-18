package net.flexberry.flexberrySampleSpring.repository;

import net.flexberry.flexberrySampleSpring.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Date;
import java.util.List;

@Repository
public interface CommentRepository extends RevisionRepository<Comment, UUID, Integer>, JpaRepository<Comment, UUID> {

    /* Создает запрос - select сomment ... where x.commentDate >= начальная дата and x.commentDate <= конечная дата.
    Запрос формируется по имени метода.
     */
    List<Comment> findByCommentDateGreaterThanEqualAndCommentDateLessThanEqual(Date beginDate, Date endDate);
}
