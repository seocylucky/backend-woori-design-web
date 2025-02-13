package woori_design_web.backend_woori_design_web.service.impl;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import woori_design_web.backend_woori_design_web.entity.Comment;
import woori_design_web.backend_woori_design_web.entity.User;
import woori_design_web.backend_woori_design_web.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this).close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Logger logger = (Logger) LoggerFactory.getLogger(CommentServiceImpl.class);
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    void testAllCommentServiceFunctions() {
        // 1. 댓글 등록
        User alice = User.builder().id(1L).name("Alice").build();
        Comment newComment = Comment.builder()
                .user(alice)
                .postId(1L)
                .content("First comment")
                .build();

        Comment savedComment = Comment.builder()
                .id(1L)
                .user(alice)
                .postId(1L)
                .content("First comment")
                .createdAt(LocalDateTime.of(2025, 2, 12, 10, 0))
                .updatedAt(LocalDateTime.of(2025, 2, 12, 10, 0))
                .build();

        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        Long commentId = commentService.registerComment(newComment);
        assertEquals(1L, commentId);
        System.out.println("댓글 등록 완료: ID = " + commentId);

        // 2. 댓글 조회
        when(commentRepository.findById(1L)).thenReturn(Optional.of(savedComment));
        Comment retrievedComment = commentService.getComment(1L);
        assertNotNull(retrievedComment);
        assertEquals("First comment", retrievedComment.getContent());
        System.out.println("댓글 조회 완료: 내용 = " + retrievedComment.getContent());

        // 3. 댓글 개수 조회
        when(commentRepository.count()).thenReturn(1L);
        long commentCount = commentService.getCommentCount();
        assertEquals(1L, commentCount);
        System.out.println("댓글 개수 조회 완료: 개수 = " + commentCount);

        // 4. 존재하지 않는 댓글 조회 (에러 케이스)
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> commentService.getComment(999L));
        System.out.println("존재하지 않는 댓글 조회 시도 (예외 발생 예상)");

        // 로그 출력
        System.out.println("\n--- 로그 메시지 ---");
        listAppender.list.forEach(event -> System.out.println(event.getFormattedMessage()));
    }
}
