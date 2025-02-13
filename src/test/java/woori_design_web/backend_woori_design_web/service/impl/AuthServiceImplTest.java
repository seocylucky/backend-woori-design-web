package woori_design_web.backend_woori_design_web.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;
import woori_design_web.backend_woori_design_web.dto.AuthDto.AuthRequestDto;
import woori_design_web.backend_woori_design_web.dto.AuthDto.AuthResponseDto;
import woori_design_web.backend_woori_design_web.entity.Token;
import woori_design_web.backend_woori_design_web.entity.User;
import woori_design_web.backend_woori_design_web.repository.TokenRepository;
import woori_design_web.backend_woori_design_web.repository.UserRepository;
import woori_design_web.backend_woori_design_web.security.JwtTokenProvider;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private String rawPassword = "password123";
    private String hashedPassword;

    @BeforeEach
    void setUp() {
        hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        testUser = User.builder()
                .loginId("testUser")
                .password(hashedPassword)
                .build();
    }

    @Test
    void login_Success() {
        AuthRequestDto requestDto = new AuthRequestDto("testUser", rawPassword);
        String accessToken = "mockAccessToken";
        String refreshToken = "mockRefreshToken";

        when(userRepository.findByLoginId(requestDto.getLoginId())).thenReturn(Optional.of(testUser));
        when(jwtTokenProvider.createAccessToken(testUser.getLoginId())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(testUser.getLoginId())).thenReturn(refreshToken);
        when(tokenRepository.findByLoginId(testUser.getLoginId())).thenReturn(Optional.empty());
        when(jwtTokenProvider.getExpiration(accessToken)).thenReturn(3600L);

        AuthResponseDto response = authService.login(requestDto);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertTrue(response.getExpiration().isAfter(LocalDateTime.now()));

        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void login_Fail_InvalidUser() {
        AuthRequestDto requestDto = new AuthRequestDto("invalidUser", rawPassword);
        when(userRepository.findByLoginId(requestDto.getLoginId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.login(requestDto));
        assertEquals("존재하지 않는 사용자입니다.", exception.getMessage());
    }

    @Test
    void login_Fail_InvalidPassword() {
        AuthRequestDto requestDto = new AuthRequestDto("testUser", "wrongPassword");
        when(userRepository.findByLoginId(requestDto.getLoginId())).thenReturn(Optional.of(testUser));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.login(requestDto));
        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    void logout_Success() {
        String accessToken = "mockAccessToken";
        String loginId = "testUser";

        when(jwtTokenProvider.getUserIdFromToken(accessToken)).thenReturn(loginId);

        authService.logout(accessToken);

        verify(tokenRepository, times(1)).deleteByLoginId(loginId);
        verify(jwtTokenProvider, times(1)).invalidateToken(accessToken);
    }
}
