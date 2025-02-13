package woori_design_web.backend_woori_design_web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import woori_design_web.backend_woori_design_web.service.UserLikeService;

@RequiredArgsConstructor
@RestController
public class UserLikeAPI {
    private final UserLikeService userLikeService;

    // 회원 좋아요 등록
    @PostMapping("/user-like/{login_id}/{post_id}")
    public void createUserLike(@PathVariable("login_id") String loginId,
                               @PathVariable("post_id") Long postId) {
    }

    // 회원 좋아요 조회
    @GetMapping("/user-like/{login_id}")
    public void getUserLike(@PathVariable("login_id") String loginId) {
    }

    // 회원 좋아요 삭제
    @PostMapping("/user-like/{login_id}/{post_id}")
    public void deleteUserLike(@PathVariable("login_id") String loginId,
                               @PathVariable("post_id") Long postId) {
    }
}
