package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final JsonManager jsonManager;

    @Transactional
    public void createDiary(LocalDate date, String text) {
        // openWeatherMap 에서 날씨 데이터 가져오기
        String weatherData = jsonManager.getWeatherString();

        // 받아온 씨 json 파싱하기
        Map<String, Object> parsedWeather = jsonManager.parseWeather(weatherData);

        // 파싱된 데이터 + 일기 값 우리 db에 넣기
        diaryRepository.save(Diary.builder()
                .weather(parsedWeather.get("main").toString())
                .temperature((Double) parsedWeather.get("temp"))
                .icon(parsedWeather.get("icon").toString())
                .text(text)
                .date(date)
                .build());
    }


    @Transactional
    public List<Diary> readDiary(LocalDate date) {
        return diaryRepository.findAllByDate(date);
    }

    @Transactional
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    @Transactional
    public void updateDiary(LocalDate date, String text) {
        Diary diary = diaryRepository.getFirstByDate(date);
        diary.setText(text);

        diaryRepository.save(diary);
    }

    @Transactional
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
