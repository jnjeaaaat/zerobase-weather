package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

    @Value("${openweathermap.key}")
    private String apiKey;

    public void createDiary(LocalDate date, String text) {
        // openWeatherMap 에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();

        // 받아온 씨 json 파싱하기
        Map<String, Object> parsedWeather = parseWeather(weatherData);
        System.out.println("날씨 : " + parsedWeather.get("main"));
        System.out.println("온도 : " + parsedWeather.get("temp"));
        System.out.println("아이콘 : " + parsedWeather.get("icon"));

        // 파싱된 데이터 + 일기 값 우리 db에 넣기
        Diary diary = Diary.builder()
                .weather(parsedWeather.get("main").toString())
                .temperature((Double) parsedWeather.get("temp"))
                .icon(parsedWeather.get("icon").toString())
                .text(text)
                .date(date)
                .build();

        diaryRepository.save(diary);
    }

    private String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=ansan&appid=" + apiKey;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            return response.toString();

        } catch (Exception e) {
            return "failed to get response";
        }
    }

    // weather json parsing
    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
             jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));

        JSONArray weatherArrayData = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArrayData.get(0);
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }
}
