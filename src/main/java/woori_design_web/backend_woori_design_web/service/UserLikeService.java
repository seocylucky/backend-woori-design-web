package woori_design_web.backend_woori_design_web.service;

import woori_design_web.backend_woori_design_web.dto.UserLikeDto;

public interface UserLikeService {
    // 좋아요 등록 (UserLike → UserLikeResponseDto)
    UserLikeDto addLike(Long userId, Long postId);

    // 좋아요 삭제 (반환값 없음)
    void removeLike(Long userId, Long postId);

    // 특정 postId의 좋아요 개수 조회
    Long getLikeCountByPostId(Long postId);
}
