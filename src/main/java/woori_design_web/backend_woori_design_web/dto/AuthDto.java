package woori_design_web.backend_woori_design_web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AuthDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthRequestDto {
        @NotBlank
        private String loginId;

        @NotBlank
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponseDto {
        private String accessToken;
        private String refreshToken;
        private LocalDateTime expiration;
    }
}
