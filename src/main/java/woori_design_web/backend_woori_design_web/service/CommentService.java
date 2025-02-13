package woori_design_web.backend_woori_design_web.service;

import woori_design_web.backend_woori_design_web.entity.Comment;

/**
 * 댓글 서비스
 * @author 조윤주
 * @since 2025.02.12
 */
public interface CommentService {
    /**
     * 댓글 등록
     * @param comment 등록할 댓글 정보
     * @return 등록된 댓글의 ID
     */
    Long registerComment(Comment comment);

    /**
     * 댓글 조회
     * @param commentId 조회할 댓글의 ID
     * @return 조회된 댓글 정보
     */
    Comment getComment(Long commentId);

    /**
     * 댓글 개수 조회
     * @return 전체 댓글 개수
     */
    long getCommentCount();

}
