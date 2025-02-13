package woori_design_web.backend_woori_design_web.service;

import woori_design_web.backend_woori_design_web.dto.UserDto.UserRegisterRequest;
import woori_design_web.backend_woori_design_web.dto.UserDto.UserRegisterResponse;
import woori_design_web.backend_woori_design_web.dto.UserDto.UserFindByLoginRequest;
import woori_design_web.backend_woori_design_web.dto.UserDto.UserFindByLoginResponse;

/**
 * 회원 서비스
 * - 회원 가입, 회원 조회, 회원 삭제(soft delete)
 * @author 권민지
 * @since 2025.02.12
 */
public interface UserService {
    // 회원 가입
    UserRegisterResponse registerUser(UserRegisterRequest request);

    // 회원 조회
    UserFindByLoginResponse findUserByLogin(UserFindByLoginRequest request);

    // 회원 삭제 - soft delete
    void deleteUser(Long id);
}
