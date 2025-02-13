package woori_design_web.backend_woori_design_web.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import woori_design_web.backend_woori_design_web.dto.UserLikeDto;
import woori_design_web.backend_woori_design_web.entity.User;
import woori_design_web.backend_woori_design_web.entity.UserLike;
import woori_design_web.backend_woori_design_web.repository.UserLikeRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLikeServiceImplTest {

    @Mock
    private UserLikeRepository userLikeRepository;

    @InjectMocks
    private UserLikeServiceImpl userLikeService;

    private User user;
    private UserLike userLike;
    private UserLikeDto userLikeDto;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).build();
        userLike = UserLike.builder()
                .postId(100L)
                .createdAt(LocalDateTime.now())
                .build();
        userLikeDto = new UserLikeDto(userLike);
    }

    /** ✅ 좋아요 등록 테스트 (DTO 반환 확인) */
    @Test
    void testAddLike() {
        when(userLikeRepository.save(any(UserLike.class))).thenReturn(userLike);

        UserLikeDto savedLikeDto = userLikeService.addLike(1L, 100L);

        assertNotNull(savedLikeDto);
        assertEquals(100L, savedLikeDto.getPostId());

        verify(userLikeRepository, times(1)).save(any(UserLike.class));
    }

    /** ✅ 좋아요 삭제 테스트 */
    @Test
    void testRemoveLike() {
        when(userLikeRepository.findByUserIdAndPostId(1L, 100L)).thenReturn(Optional.of(userLike));
        doNothing().when(userLikeRepository).delete(userLike);

        assertDoesNotThrow(() -> userLikeService.removeLike(1L, 100L));
        verify(userLikeRepository, times(1)).delete(userLike);
    }

    /** ✅ 특정 postId의 좋아요 개수 조회 테스트 */
    @Test
    void testGetLikeCountByPostId() {
        when(userLikeRepository.existsByPostId(100L)).thenReturn(true);
        when(userLikeRepository.countLikesByPostId(100L)).thenReturn(3L);

        Long likeCount = userLikeService.getLikeCountByPostId(100L);

        assertNotNull(likeCount);
        assertEquals(3L, likeCount);
    }

    /** ✅ 존재하지 않는 postId로 좋아요 개수 조회 시 예외 발생 테스트 */
    @Test
    void testGetLikeCountByPostId_NotFound() {
        when(userLikeRepository.existsByPostId(999L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userLikeService.getLikeCountByPostId(999L);
        });

        assertEquals("해당 postId에 대한 좋아요 기록이 없습니다.", exception.getMessage());
    }

    /** ✅ 유효하지 않은 User ID로 좋아요 등록 시 예외 발생 테스트 */
    @Test
    void testAddLikeWithInvalidUser() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userLikeService.addLike(null, 100L);
        });

        assertEquals("❌ User ID 또는 Post ID는 null일 수 없습니다.", exception.getMessage());
    }

    /** ✅ 존재하지 않는 좋아요 삭제 시 예외 발생 테스트 */
    @Test
    void testRemoveLikeWithInvalidPost() {
        when(userLikeRepository.findByUserIdAndPostId(1L, 999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userLikeService.removeLike(1L, 999L);
        });

        assertEquals("해당 좋아요가 존재하지 않습니다.", exception.getMessage());
    }
}
