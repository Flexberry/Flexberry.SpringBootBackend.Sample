package net.flexberry.flexberrySampleSpring.controller;

import io.swagger.v3.oas.annotations.Operation;
import net.flexberry.flexberrySampleSpring.model.Comment;
import net.flexberry.flexberrySampleSpring.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @Operation(summary = "Get comment by primary key")
    @GetMapping("/comments/{primarykey}")
    public Comment getComment(@PathVariable("primarykey") UUID primaryKey) {
        return service.getCommnet(primaryKey);
    }

    @Operation(summary = "Get comment by datetime range")
    @GetMapping("/commentsForPeriod")
    public List<Comment> getCommentsForPeriod(@RequestParam Date beginDate, @RequestParam Date endDate) {
        return service.getCommentsForPeriod(beginDate, endDate);
    }

    @Operation(summary = "Get all comments")
    @GetMapping("/comments")
    public List<Comment> getComments() {
        return service.getAllComments();
    }

    @Operation(summary = "Delete comment with primary key")
    @DeleteMapping("/comments/{primaryKey}")
    public void deleteComment(@PathVariable("primaryKey") UUID primaryKey) {
        service.deleteCommentByPrimaryKey(primaryKey);
    }

    @Operation(summary = "Post comment")
    @PostMapping("/comments")
    public Comment addComment(@RequestBody Comment comment) {
        return service.saveOrUpdateComment(comment);
    }

    @Operation(summary = "Update comment")
    @PutMapping("/comments")
    public Comment updateComment(@RequestBody Comment comment) {
        return service.saveOrUpdateComment(comment);
    }
}
