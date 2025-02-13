package woori_design_web.backend_woori_design_web.service;

import woori_design_web.backend_woori_design_web.dto.AuthDto.AuthResponseDto;
import woori_design_web.backend_woori_design_web.dto.AuthDto.AuthRequestDto;

public interface AuthService {
    // 로그인
    AuthResponseDto login(AuthRequestDto requestDto);

    // 로그아웃
    void logout(String accessToken);
}
