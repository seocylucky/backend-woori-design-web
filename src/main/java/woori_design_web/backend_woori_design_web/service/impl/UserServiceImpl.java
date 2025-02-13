package woori_design_web.backend_woori_design_web.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woori_design_web.backend_woori_design_web.dto.UserDto.UserRegisterRequest;
import woori_design_web.backend_woori_design_web.dto.UserDto.UserRegisterResponse;
import woori_design_web.backend_woori_design_web.dto.UserDto.UserFindByLoginResponse;
import woori_design_web.backend_woori_design_web.dto.UserDto.UserFindByLoginRequest;
import woori_design_web.backend_woori_design_web.dto.UserDto.FetchedComment;
import woori_design_web.backend_woori_design_web.dto.UserDto.FetchedUserLike;
import woori_design_web.backend_woori_design_web.entity.User;
import woori_design_web.backend_woori_design_web.enums.UserStatus;
import woori_design_web.backend_woori_design_web.repository.UserRepository;
import woori_design_web.backend_woori_design_web.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 회원 서비스
 * - 회원 가입, 회원 조회, 회원 삭제(soft delete)
 * @author 권민지
 * @since 2025.02.12
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        log.info("[회원 가입 요청] loginId: {}", request.getLoginId());

        // 로그인 아이디로 회원 존재 여부 확인
        if (userRepository.existsByLoginId(request.getLoginId())) {
            log.error("[회원가입 실패] 동일 아이디 존재: {}", request.getLoginId());
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        // 이메일로 회원 존재 여부 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("[회원가입 실패] 동일 이메일 존재: {}", request.getEmail());
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        User user = User.builder()
                .loginId(request.getLoginId())
                .password(hashedPassword)
                .name(request.getName())
                .email(request.getEmail())
                .status(UserStatus.ACTIVE)
                .build();

        User saveUser = userRepository.save(user);

        log.info("[회원 가입 성공] userId: {}", saveUser.getId());

        return UserRegisterResponse.builder()
                .id(saveUser.getId())
                .loginId(saveUser.getLoginId())
                .name(saveUser.getName())
                .email(saveUser.getEmail())
                .build();
    }

    // 회원 조회
    @Transactional(readOnly = true)
    @Override
    public UserFindByLoginResponse findUserByLogin(UserFindByLoginRequest request) {
        log.info("[회원 조회 요청] loginId: {}", request.getLoginId());

        User user = userRepository.findByLoginId(request.getLoginId())
                        .orElseThrow(() -> {
                            log.error("[회원 조회 실패] 존재하지 않는 회원: {}", request.getLoginId());
                            return new IllegalArgumentException("존재하지 않는 회원입니다.");
                        });

        log.info("[회원 조회 성공] userId: {}", user.getId());

        // user와 연관된 comments, userLikes 조회 (없다면 빈 리스트 반환)
        List<FetchedComment> fetchedComment = user.getComments().stream()
                .map(comment -> FetchedComment.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .postId(comment.getPostId())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build())
                .toList();

        List<FetchedUserLike> fetchedUserLikes = user.getUserLikes().stream()
                .map(userLike -> FetchedUserLike.builder()
                        .id(userLike.getId())
                        .postId(userLike.getPostId())
                        .createdAt(userLike.getCreatedAt())
                        .build())
                .toList();

        return UserFindByLoginResponse.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .comments(fetchedComment)
                .userLikes(fetchedUserLikes)
                .build();
    }

    // 회원 삭제 - soft delete
    @Transactional
    @Override
    public void deleteUser(Long userId) {
        log.info("[회원 삭제 요청] userId: {}", userId);

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> {
                            log.error("[회원 삭제 실패] 존재하지 않는 회원: {}", userId);
                            return new IllegalArgumentException("존재하지 않는 회원입니다.");
                        });

        User deletedUser = user.toBuilder()
                        .status(UserStatus.DELETED)
                        .build();

        userRepository.save(deletedUser);

        log.info("[회원 삭제 성공] userId: {}", userId);
    }
}
