package woori_design_web.backend_woori_design_web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class UserDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserRegisterRequest {
        private String loginId;
        private String password;
        private String name;
        private String email;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserRegisterResponse {
        private Long id;
        private String loginId;
        private String name;
        private String email;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserFindByLoginRequest {
        private String loginId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserFindByLoginResponse {
        private Long id;
        private String loginId;
        private String name;
        private String email;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<FetchedComment> comments;
        private List<FetchedUserLike> userLikes;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FetchedComment {
        private Long id;
        private String content;
        private Long postId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FetchedUserLike {
        private Long id;
        private Long postId;
        private LocalDateTime createdAt;
    }

}
