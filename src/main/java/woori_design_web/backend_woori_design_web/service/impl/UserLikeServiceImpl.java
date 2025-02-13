package woori_design_web.backend_woori_design_web.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woori_design_web.backend_woori_design_web.dto.UserLikeDto;
import woori_design_web.backend_woori_design_web.entity.User;
import woori_design_web.backend_woori_design_web.entity.UserLike;
import woori_design_web.backend_woori_design_web.repository.UserLikeRepository;
import woori_design_web.backend_woori_design_web.service.UserLikeService;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserLikeServiceImpl implements UserLikeService {
    private final UserLikeRepository userLikeRepository;

    /** 좋아요 등록 */
    @Override
    @Transactional
    public UserLikeDto addLike(Long userId, Long postId) {
        validateInput(userId, postId);

        UserLike userLike = UserLike.builder()
                .user(User.builder().id(userId).build())
                .postId(postId)
                .createdAt(LocalDateTime.now())
                .build();

        UserLike savedLike = userLikeRepository.save(userLike);
        log.info("✅ 좋아요 등록 성공 - userId: {}, postId: {}", userId, postId);

        return new UserLikeDto(savedLike); // ✅ 엔티티 대신 DTO 반환
    }

    /** 좋아요 삭제 */
    @Override
    @Transactional
    public void removeLike(Long userId, Long postId) {
        validateInput(userId, postId);

        UserLike userLike = userLikeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> {
                    log.warn("❌ 좋아요 삭제 실패 - 해당 좋아요가 존재하지 않음 (userId: {}, postId: {})", userId, postId);
                    return new EntityNotFoundException("해당 좋아요가 존재하지 않습니다.");
                });

        userLikeRepository.delete(userLike);
        log.info("✅ 좋아요 삭제 성공 - userId: {}, postId: {}", userId, postId);
    }

    /** 특정 postId의 좋아요 개수 조회 */
    @Override
    @Transactional(readOnly = true)
    public Long getLikeCountByPostId(Long postId) {
        if (!userLikeRepository.existsByPostId(postId)) {
            log.error("❌ 좋아요 개수 조회 실패 - 존재하지 않는 postId: {}", postId);
            throw new EntityNotFoundException("해당 postId에 대한 좋아요 기록이 없습니다.");
        }

        Long likeCount = userLikeRepository.countLikesByPostId(postId);
        log.info("✅ 좋아요 개수 조회 성공 - postId: {}, 좋아요 개수: {}", postId, likeCount);

        return likeCount != null ? likeCount : 0L;
    }

    /** 공통 입력값 검증 메서드 */
    private void validateInput(Long userId, Long postId) {
        if (userId == null || postId == null) {
            log.error("❌ User ID 또는 Post ID는 null일 수 없습니다.");
            throw new IllegalArgumentException("❌ User ID 또는 Post ID는 null일 수 없습니다.");
        }
    }
}
