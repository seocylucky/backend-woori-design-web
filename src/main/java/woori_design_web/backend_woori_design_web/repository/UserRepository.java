package woori_design_web.backend_woori_design_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import woori_design_web.backend_woori_design_web.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 로그인 아이디로 회원 존재 여부 확인
    boolean existsByLoginId(String loginId);

    // 이메일로 회원 존재 여부 확인
    boolean existsByEmail(String email);

    // 로그인 아이디로 회원 조회
    Optional<User> findByLoginId(String loginId);
}


