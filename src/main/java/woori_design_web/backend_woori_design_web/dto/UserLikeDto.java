package woori_design_web.backend_woori_design_web.dto;

import woori_design_web.backend_woori_design_web.entity.UserLike;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserLikeDto {
    private final Long postId;
    private final LocalDateTime likedAt;

    public UserLikeDto(UserLike userLike) {
        this.postId = userLike.getPostId();
        this.likedAt = userLike.getCreatedAt();
    }
}
