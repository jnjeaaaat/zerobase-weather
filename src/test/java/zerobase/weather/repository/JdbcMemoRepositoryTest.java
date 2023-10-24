package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JdbcMemoRepositoryTest {

    @Autowired
    private JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest() {
        //given
        Memo newMemo = new Memo(2L, "insertMemoTest");

        //when
        jdbcMemoRepository.save(newMemo);

        //then
        Optional<Memo> result = jdbcMemoRepository.findById(2L);
        assertEquals("insertMemoTest", result.get().getText());
    }

    @Test
    void findAllMemoTest() {
        //given
        //when
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);

        //then
        assertNotNull(memoList);
    }
}