package zerobase.weather.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @Operation(summary = "새로운 일기 작성")
    @PostMapping("/create/diary")
    public DiaryDto createDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text) {
        return diaryService.createDiary(date, text);
    }

    @Operation(summary = "특정 날짜에 작성된 일기 리스트 조회")
    @GetMapping("/read/diary")
    public List<Diary> readDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        if (date.isAfter(LocalDate.ofYearDay(3050, 1))) {
//            throw new InvalidDate();
//        }
        return diaryService.readDiary(date);
    }

    @Operation(summary = "지정한 두개의 날짜 사이에 작성된 일기 리스트 조회", description = "안녕하세요")
    @GetMapping("/read/diaries")
    public List<Diary> readDiaries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "조회할 기간의 첫번째날", example = "2020-01-01")
                    LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "조회할 기간의 마지막날", example = "2020-02-02")
                    LocalDate endDate) {

        return diaryService.readDiaries(startDate, endDate);
    }

    @Operation(summary = "지정한 날짜의 일기중 첫번째 일기 내용 수정")
    @PutMapping("/update/diary")
    public void updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                     @RequestBody String text) {

        diaryService.updateDiary(date, text);
    }

    @Operation(summary = "지정한 날짜의 모든 일기 삭제")
    @DeleteMapping("/delete/diary")
    public void deleteDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        diaryService.deleteDiary(date);
    }

}
