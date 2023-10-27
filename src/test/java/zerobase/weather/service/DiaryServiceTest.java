package zerobase.weather.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @InjectMocks
    private DiaryService diaryService;


    @Test
    @DisplayName("일기 수정 성공")
    void updateDiarySuccess() {
        //given
        Diary diary = Diary.builder()
                .weather("맑음")
                .date(LocalDate.now())
                .temperature(25.0)
                .icon("05d")
                .text("today's diary")
                .build();

        given(diaryRepository.getFirstByDate(any()))
                .willReturn(diary);

        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);

        //when
        diaryService.updateDiary(LocalDate.now(), "new Text");

        //then
        verify(diaryRepository, times(1)).save(captor.capture());
        assertEquals("new Text", captor.getValue().getText());
    }

    @Test
    void deleteDiarySuccess() {
        //given
        List<Diary> diaryList =
                Arrays.asList(
                        Diary.builder()
                                .text("1번 일기")
                                .date(LocalDate.now())
                                .icon("05d")
                                .weather("맑음")
                                .build(),
                        Diary.builder()
                                .text("2번 일기")
                                .date(LocalDate.now())
                                .icon("05d")
                                .weather("맑음")
                                .build()
                );
        //when
        diaryRepository.deleteAllByDate(LocalDate.now());

        //then
        verify(diaryRepository, times(1)).deleteAllByDate(LocalDate.now());
    }

}