package zerobase.weather.dto;

import lombok.*;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryDto {
    private Long id;
    private String weather;
    private String icon;
    private Double temperature;
    private String text;
    private LocalDate date;

    public static DiaryDto fromEntity(Diary diary) {
        return DiaryDto.builder()
                .id(diary.getId())
                .weather(diary.getWeather())
                .icon(diary.getIcon())
                .temperature(diary.getTemperature())
                .text(diary.getText())
                .date(diary.getDate())
                .build();
    }
}
