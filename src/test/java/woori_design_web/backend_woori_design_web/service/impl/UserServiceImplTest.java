package woori_design_web.backend_woori_design_web.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import woori_design_web.backend_woori_design_web.dto.UserDto.UserRegisterRequest;
import woori_design_web.backend_woori_design_web.dto.UserDto.UserRegisterResponse;
import woori_design_web.backend_woori_design_web.dto.UserDto.UserFindByLoginResponse;
import woori_design_web.backend_woori_design_web.dto.UserDto.UserFindByLoginRequest;
import woori_design_web.backend_woori_design_web.entity.User;
import woori_design_web.backend_woori_design_web.enums.UserStatus;
import woori_design_web.backend_woori_design_web.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterRequest request;

    @BeforeEach
    void setUp() {
        request = UserRegisterRequest.builder()
                .loginId("testUser")
                .password("password123")
                .name("Test User")
                .email("test@example.com")
                .build();
    }

    @Test
    void sign_up_success() {
        // given
        when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return user.toBuilder()
                    .id(1L)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .status(UserStatus.ACTIVE)
                    .build();
        });

        // when
        UserRegisterResponse response = userService.registerUser(request);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("testUser", response.getLoginId());
        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());

        verify(userRepository, times(1)).existsByLoginId(request.getLoginId());
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void sign_up_fail_not_exist_user() {
        // given
        when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(true);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(request));

        assertEquals("이미 존재하는 회원입니다.", exception.getMessage());
        verify(userRepository, times(1)).existsByLoginId(request.getLoginId());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void sign_up_fail_not_exist_email() {
        // given
        when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(request));

        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
        verify(userRepository, times(1)).existsByLoginId(request.getLoginId());
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser() {
        // Mock 설정
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return user.toBuilder().id(1L).build();
        });

        when(userRepository.findById(1L)).thenReturn(Optional.of(
                User.builder()
                        .id(1L)
                        .loginId("testUser")
                        .status(UserStatus.ACTIVE)
                        .build()
        ));

        // given
        UserRegisterResponse response = userService.registerUser(request);
        Long userId = response.getId();

        // when
        userService.deleteUser(userId);

        // then - user 상태 변경 확인
        verify(userRepository).save(argThat(user -> user.getStatus() == UserStatus.DELETED));
    }

    @Test
    void findUserByLogin_success() {
        // given
        User user = User.builder()
                .id(1L)
                .loginId("testUser")
                .name("Test User")
                .email("test@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .comments(List.of())
                .userLikes(List.of())
                .build();

        when(userRepository.findByLoginId("testUser")).thenReturn(Optional.of(user));

        // when
        UserFindByLoginResponse response = userService.findUserByLogin(new UserFindByLoginRequest("testUser"));

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("testUser", response.getLoginId());
        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void findUserByLogin_fail_not_found() {
        // given
        when(userRepository.findByLoginId("unknownUser")).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.findUserByLogin(new UserFindByLoginRequest("unknownUser")));

        assertEquals("존재하지 않는 회원입니다.", exception.getMessage());
    }

}
