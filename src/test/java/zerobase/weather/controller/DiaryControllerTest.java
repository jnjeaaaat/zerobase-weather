package zerobase.weather.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.repository.DiaryRepository;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {

    @MockBean
    private DiaryService diaryService;

    @MockBean
    private DiaryRepository diaryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("일기 저장 성공")
    void createDiarySuccess() throws Exception {
        //given
        given(diaryService.createDiary(any(), anyString()))
                .willReturn(DiaryDto.builder()
                        .weather("맑음")
                        .date(LocalDate.now())
                        .temperature(25.0)
                        .icon("05d")
                        .text("today's diary")
                        .build());

        //when
        //then
        mockMvc.perform(post("/create/diary?date=2023-10-27")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                "today's diary"
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weather").value("맑음"))
                .andExpect(jsonPath("$.date").value("2023-10-27"))
                .andExpect(jsonPath("$.temperature").value(25.0))
                .andExpect(jsonPath("$.icon").value("05d"))
                .andExpect(jsonPath("$.text").value("today's diary"))

                .andDo(print());
    }

    @Test
    @DisplayName("특정 날짜 일기 리스트 조회 성공")
    void readDiarySuccess() throws Exception {
        //given
        List<Diary> diaryList =
                Arrays.asList(
                        Diary.builder()
                                .text("1027 첫번째 일기")
                                .date(LocalDate.now())
                                .icon("05d")
                                .weather("맑음")
                                .build(),
                        Diary.builder()
                                .text("1027 두번째 일기")
                                .date(LocalDate.now())
                                .icon("05d")
                                .weather("맑음")
                                .build()
                );
        given(diaryService.readDiary(any()))
                .willReturn(diaryList);
        //when
        //then
        mockMvc.perform(get("/read/diary?date=2023-10-27"))
                .andExpect(jsonPath("$.[0].text").value("1027 첫번째 일기"))
                .andExpect(jsonPath("$.[0].icon").value("05d"))
                .andExpect(jsonPath("$.[0].weather").value("맑음"))
                .andExpect(jsonPath("$.[0].date").value("2023-10-27"))
                .andExpect(jsonPath("$.[1].text").value("1027 두번째 일기"))
                .andExpect(jsonPath("$.[1].icon").value("05d"))
                .andExpect(jsonPath("$.[1].weather").value("맑음"))
                .andExpect(jsonPath("$.[1].date").value("2023-10-27"))
                .andDo(print());
    }

    @Test
    @DisplayName("지정한 사이 날짜의 일기 리스트 조회")
    void readDiariesSuccess() throws Exception {
        //given
        List<Diary> diaryList =
                Arrays.asList(
                        Diary.builder()
                                .text("230501 일기")
                                .date(LocalDate.of(2023, 5, 1))
                                .icon("03d")
                                .weather("흐림")
                                .build(),
                        Diary.builder()
                                .text("230722 일기")
                                .date(LocalDate.of(2023, 7, 22))
                                .icon("05d")
                                .weather("맑음")
                                .build()
                );
        given(diaryService.readDiaries(
                LocalDate.of(2023, 4, 1),
                LocalDate.of(2023, 8, 1)))
                .willReturn(diaryList);
        //when
        //then
        mockMvc.perform(get("/read/diaries?startDate=2023-04-01&endDate=2023-08-01"))
                .andExpect(jsonPath("$[0].text").value("230501 일기"))
                .andExpect(jsonPath("$[0].icon").value("03d"))
                .andExpect(jsonPath("$[0].weather").value("흐림"))
                .andExpect(jsonPath("$[0].date").value("2023-05-01"))

                .andExpect(jsonPath("$[1].text").value("230722 일기"))
                .andExpect(jsonPath("$[1].icon").value("05d"))
                .andExpect(jsonPath("$[1].weather").value("맑음"))
                .andExpect(jsonPath("$[1].date").value("2023-07-22"))
                .andDo(print());
    }

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
        diaryService.updateDiary(LocalDate.of(2023, 10, 27), "new diary");

        //then
        verify(diaryRepository, times(1)).save(captor.capture());
        assertEquals("new diary", captor.getValue().getText());
//        assertEquals("new diary", diary.getText());
    }

}