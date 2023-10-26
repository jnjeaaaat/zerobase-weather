package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.repository.DateWeatherRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DateWeatherService {
    private final DateWeatherRepository dateWeatherRepository;
    private final JsonManager jsonManager;

    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class);

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveDateWeather() {
        dateWeatherRepository.save(getWeatherFromApi());
        logger.info("오늘 날씨 데이터 잘 가져옴.");
    }

    private DateWeather getWeatherFromApi() {
        // openWeatherMap 에서 날씨 데이터 가져오기
        String weatherData = jsonManager.getWeatherString();

        // 받아온 씨 json 파싱하기
        Map<String, Object> parsedWeather = jsonManager.parseWeather(weatherData);

        return DateWeather.builder()
                .date(LocalDate.now())
                .weather(parsedWeather.get("main").toString())
                .temperature((Double) parsedWeather.get("temp"))
                .icon(parsedWeather.get("icon").toString())
                .build();
    }

    public DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> dateWeatherList = dateWeatherRepository.findAllByDate(date);
        if (dateWeatherList.size() == 0) {
            System.out.println("저장된 날씨 없음");
            return getWeatherFromApi();
        } else {
            System.out.println("기존 날씨 사용");
            return dateWeatherList.get(0);
        }

    }
}
