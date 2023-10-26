package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final DateWeatherService dateWeatherService;

    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DiaryDto createDiary(LocalDate date, String text) {
        logger.info("started to create diary");

        // 날씨 데이터 가져오기 (기존 DB 에서 가져오기)
        DateWeather dateWeather = dateWeatherService.getDateWeather(date);

        Diary diary = new Diary();
        diary.setDateWeather(dateWeather);
        diary.setDate(date);
        diary.setText(text);

        logger.info("end to create diary");

        // 파싱된 데이터 + 일기 값 우리 db에 넣기
        return DiaryDto.fromEntity(
                diaryRepository.save(diary)
        );

    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        logger.debug("read diary");
        return diaryRepository.findAllByDate(date);
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    @Transactional(readOnly = false)
    public void updateDiary(LocalDate date, String text) {
        Diary diary = diaryRepository.getFirstByDate(date);
        diary.setText(text);

        diaryRepository.save(diary);
    }

    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
