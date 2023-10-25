package zerobase.weather.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "dateWeather")
public class DateWeather {
    @Id
    private LocalDate date;

    private String weather;
    private String icon;
    private Double temperature;
}
