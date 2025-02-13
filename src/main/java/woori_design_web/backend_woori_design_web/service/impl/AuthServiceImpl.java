package woori_design_web.backend_woori_design_web.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import woori_design_web.backend_woori_design_web.dto.AuthDto.AuthRequestDto;
import woori_design_web.backend_woori_design_web.dto.AuthDto.AuthResponseDto;
import woori_design_web.backend_woori_design_web.entity.Token;
import woori_design_web.backend_woori_design_web.entity.User;
import woori_design_web.backend_woori_design_web.repository.TokenRepository;
import woori_design_web.backend_woori_design_web.repository.UserRepository;
import woori_design_web.backend_woori_design_web.security.JwtTokenProvider;
import woori_design_web.backend_woori_design_web.service.AuthService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    @Override
    public AuthResponseDto login(AuthRequestDto requestDto) {
        log.info("로그인 요청: {}", requestDto.getLoginId());

        User user = userRepository.findByLoginId(requestDto.getLoginId())
                .orElseThrow(() -> {
                    log.error("[로그인 실패] 존재하지 않는 사용자: {}", requestDto.getLoginId());
                    return new IllegalArgumentException("존재하지 않는 사용자입니다.");
                });

        if (!BCrypt.checkpw(requestDto.getPassword(), user.getPassword())) {
            log.error("[로그인 실패] 비밀번호 불일치: {}", requestDto.getLoginId());
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // Access Token & Refresh Token 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getLoginId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getLoginId());

        // 기존 Refresh Token 업데이트
        tokenRepository.findByLoginId(user.getLoginId())
                .ifPresentOrElse(
                        existingToken -> {
                            existingToken.updateRefreshToken(refreshToken);
                            tokenRepository.save(existingToken);
                        },
                        () -> tokenRepository.save(Token.builder()
                                .loginId(user.getLoginId())
                                .refreshToken(refreshToken)
                                .build())
                );

        long expirationSeconds = jwtTokenProvider.getExpiration(accessToken);
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expirationSeconds);
        return new AuthResponseDto(accessToken, refreshToken, expiresAt);
    }

    @Override
    public void logout(String accessToken) {
        log.info("로그아웃 요청: {}", accessToken);

        String loginId = jwtTokenProvider.getUserIdFromToken(accessToken);
        tokenRepository.deleteByLoginId(loginId);

        // 블랙리스트 등록
        jwtTokenProvider.invalidateToken(accessToken);
    }
}
