package woori_design_web.backend_woori_design_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import woori_design_web.backend_woori_design_web.entity.Token;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByLoginId(String loginId);

    void deleteByLoginId(String loginId);
}
