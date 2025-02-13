package woori_design_web.backend_woori_design_web.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woori_design_web.backend_woori_design_web.entity.Comment;
import woori_design_web.backend_woori_design_web.repository.CommentRepository;
import woori_design_web.backend_woori_design_web.service.CommentService;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    /**
     * 댓글 등록 */
    @Override
    @Transactional
    public Long registerComment(Comment comment) {
        LocalDateTime now = LocalDateTime.now();
        Comment newComment = Comment.builder()
                .user(comment.getUser())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .createdAt(now)
                .updatedAt(now)
                .build();
        Comment savedComment = commentRepository.save(newComment);
        log.info("Comment registered: {}", savedComment.getId());
        return savedComment.getId();
    }

    /**
     * 댓글 조회 */
    @Override
    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        try {
            return commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));
        } catch (RuntimeException e) {
            log.error("조회할 수 없습니다. 댓글 ID: {}", commentId, e);
            throw e;
        }
    }


    /**
     * 댓글 개수 조회 */
    @Override
    @Transactional(readOnly = true)
    public long getCommentCount() {
        long count = commentRepository.count();
        log.info("Total comment count: {}", count);
        return count;
    }
}
