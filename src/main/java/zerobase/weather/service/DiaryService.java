package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

    @Value("${openweathermap.key}")
    private String apiKey;

    public DiaryDto createDiary(LocalDate date, String text) {
        return null;
    }

    private String getWeatherString() {
        "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;
    }
}
