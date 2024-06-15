package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.entity.*;
import com.diplom.web_service_attendance.enumPackage.ParityWeekEnum;
import com.diplom.web_service_attendance.enumPackage.WeekdayEnum;
import com.diplom.web_service_attendance.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class FillSheduleService {

    private final LessonRepository lessonRepository;
    private final DisciplineRepository disciplineRepository;
    private final TeacherRepository teacherRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final TypeLessonRepository typeLessonRepository;
    private final WeekdayRepository weekdayRepository;
    private final NumberLessonRepository numberLessonRepository;
    private final ParityOfWeekRepository parityOfWeekRepository;
    private final ActualLessonRepository actualLessonRepository;

    LocalDate startDate = LocalDate.of(2024, 2, 5);
    LocalDate endDate = LocalDate.of(2024, 7, 19);

    public String downloadFilePdf() {
        String url = "https://www.sibsiu.ru/files/raspisanie/uc/%D0%A0%D0%B0%D1%81%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5%20%D0%B7%D0%B0%D0%BD%D1%8F%D1%82%D0%B8%D0%B9/4%20%D0%9A%D1%83%D1%80%D1%81%20%D0%92%D0%B5%D1%81%D0%B5%D0%BD%D0%BD%D0%B8%D0%B9%20%D1%81%D0%B5%D0%BC%D0%B5%D1%81%D1%82%D1%80%202023-2024.pdf";
        String file = "C:\\Users\\anton\\IdeaProjects\\web_service_attendance\\doc.pdf";
        int CONNECT_TIMEOUT = 10000; // Timeout для соединения
        int READ_TIMEOUT = 10000; // Timeout для чтения
//        try {
//            copyURLToFile(
//                    new URL(url),
//                    new File(file),
//                    CONNECT_TIMEOUT,
//                    READ_TIMEOUT);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return file;
    }

    public String convertPdfToXlsx(String pathPdf){
        // реализация подключения к API
        return "doc(last version).xlsx";
    }

    public String importDb(String pathXlsx){
        
        // Чтение файла Excel
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(
                    new File("doc(last version).xlsx"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // считает листы
        for (int i = 1; i < workbook.getNumberOfSheets(); i+=2) {

            Sheet sheet = workbook.getSheetAt(i);
            StudyGroup studyGroup = null;

            // считает столбцы
            for (int j = 2; j <= 4; j++) {

                Row row = sheet.getRow(0);
                Cell cell = row.getCell(j);
                if (cell != null) {

                    if (cell.getStringCellValue().isEmpty()){
                        continue;
                    }

                    String groupShortName = cell.getStringCellValue()
                            .split("\n")[0]
                            .substring(2);

                    studyGroup = studyGroupRepository
                            .findByShortNameLikeIgnoreCase(groupShortName).orElse(null);
                    if (studyGroup == null) {
                        int year = Integer.parseInt(groupShortName.substring(groupShortName.length() - 2));
                        studyGroup = StudyGroup.builder()
                                .shortName(groupShortName)
                                .yearAdmission(LocalDate.of(2000+year, 1, 1))
                                .build();
                        studyGroupRepository.save(studyGroup);
                    }
                } else {
                    System.out.println("Ячейка C1 пуста или не существует в " + sheet.getSheetName());
                }

                // считает строки
                int countNumberLesson = 0;
                WeekdayEnum weekdayEnum = null;
                ParityWeekEnum parityWeekEnum;
                for (int k = 1; k < sheet.getLastRowNum(); k++) {

                    String cellWeekday = sheet.getRow(k).getCell(0).getStringCellValue();
                    if (!cellWeekday.isEmpty()){
                        countNumberLesson = 0;
                        weekdayEnum = WeekdayEnum.valueOf(cellWeekday.toUpperCase());
                    }

                    // счет пар
                    if (k % 2 != 0){
                        countNumberLesson++;
                        parityWeekEnum = ParityWeekEnum.НЕЧЕТНАЯ;
                    } else
                        parityWeekEnum = ParityWeekEnum.ЧЕТНАЯ;

                    String nowCell = sheet.getRow(k).getCell(j).getStringCellValue();

                    if (!nowCell.isEmpty()){
                        String[] disciplinInfo = disciplinInfoToArray(nowCell);

                        Lesson lesson = Lesson.builder()
                                .discipline(existsDisciplinNameAndCreate(disciplinInfo[0]))
                                .teacherList(existsTeacherAndCreate(Arrays
                                        .copyOfRange(disciplinInfo, 1, disciplinInfo.length)))
                                .studyGroupList(List.of(studyGroup))
                                .typeLesson(typeLessonRepository.findByName("Практика")
                                        .orElseThrow(() ->
                                                new EntityNotFoundException("Не найден тип пары name = Практика")))
                                .numberLesson(numberLessonRepository
                                        .findByNameIgnoreCase(String.valueOf(countNumberLesson))
                                        .orElseThrow(() -> new EntityNotFoundException("Не найден номер пары " +
                                                "name при создании Lesson")))
                                .weekday(weekdayRepository.findByNameIgnoreCase(weekdayEnum.name())
                                                .orElseThrow(() -> new EntityNotFoundException("Не найден день" +
                                                        " недели name при создании Lesson")))
                                .parityOfWeek(parityOfWeekRepository
                                        .findByNameIgnoreCase(parityWeekEnum.name())
                                        .orElseThrow(() -> new EntityNotFoundException("Не найдена четность недели" +
                                                " при создании Lesson")))
                                .build();
                        if (!lessonRepository.exists(Example.of(lesson))) {
                            lessonRepository.save(lesson);
                        }
                    }
                    // если пара одна строка, то сработает
                    if (!(cell != null && isCellMergedWithAdjacentCells(sheet, k, 1))) {
                        k++;
                    }
                }
            }
        }

        try {
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "База данных успешно обновлена";
    }

    // работает отлично но только до нечетной недели

    public String createActualLesson() {
        final List<StudyGroup> studyGroupList = studyGroupRepository.findAll();
        final List<Weekday> weekdayList = weekdayRepository.findAll();
        final List<ParityOfWeek> parityOfWeekList = parityOfWeekRepository.findAll();
        LocalDate countDate = startDate;

        for (StudyGroup studyGroup : studyGroupList) {
            countDate = startDate;

            while (countDate.isBefore(endDate)) {

                for (ParityOfWeek parity : parityOfWeekList) {
                    for (Weekday weekday : weekdayList) {
                        // Skip weekends (Saturday and Sunday)
                        if (countDate.getDayOfWeek() == DayOfWeek.SATURDAY || countDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            countDate = countDate.plusDays(1);
                            continue;
                        }

                        List<Lesson> countLessonList = lessonRepository
                                .findLessonsByStudyGroupListAndWeekdayAndParityOfWeekOrderByNumberLesson(
                                        studyGroup,
                                        weekday,
                                        parity
                                ).orElse(null);

                        if (countLessonList == null || countLessonList.isEmpty()) {
                            // Skip if there are no lessons for the current weekday
                            countDate = countDate.plusDays(1);
                            continue;
                        }

                        for (Lesson lesson : countLessonList) {
                            ActualLesson actualLesson = ActualLesson.builder()
                                    .lesson(lesson)
                                    .date(countDate)
                                    .build();
                            actualLessonRepository.save(actualLesson);
                        }

                        countDate = countDate.plusDays(1);
                    }
                }
//                countDate = countDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            }
        }
        return "База данных успешно обновлена";
    }

    // Вспомагательные

    private List<Teacher> existsTeacherAndCreate(String[] disciplinInfo) {

        String[] fioTeachers = Arrays
                .stream(Arrays.toString(disciplinInfo)
                        .replaceAll("\\[|\\]", "")
                        .split("\\s|\\.,\\s|\\."))
                .map(String::trim)
                .toArray(String[]::new);

        List<Teacher> teacherList = new ArrayList<>();
        Teacher teacher = null;
        for (int i = 0; i < fioTeachers.length / 3; i+=3) {
            teacher = teacherRepository.findBySecondNameAndFirstNameStartingWithAndOtchestvoStartingWith(
                    fioTeachers[0], fioTeachers[1], fioTeachers[2]).orElse(null);

            if (teacher == null) {
                teacher = Teacher.builder()
                        .secondName(fioTeachers[i])
                        .firstName(fioTeachers[i+1])
                        .otchestvo(fioTeachers[i+2])
                        .build();
                teacherRepository.save(teacher);
            }
            teacherList.add(teacher);
        }

        return teacherList;
    }

    private Discipline existsDisciplinNameAndCreate(String name) {

        Discipline discipline = null;

        if (name.length() >= 7){
            discipline = disciplineRepository.findByNameLikeIgnoreCase(name).orElse(null);
            if (discipline == null){
                discipline = Discipline.builder()
                        .name(name)
                        .shortName(keepFirstThreeLetters(name))
                        .build();
                disciplineRepository.save(discipline);
            }
        } else {
            discipline = disciplineRepository.findByShortNameLikeIgnoreCase(name).orElse(null);
            if (discipline == null){
                discipline = Discipline.builder()
                        .shortName(name)
                        .build();
                disciplineRepository.save(discipline);
            }
        }

        return discipline;
    }

    public String keepFirstThreeLetters(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        String[] words = input.split("\\s+"); // Разделение строки на слова по пробелам

        for (String word : words) {
            if (word.length() <= 3) {
                result.append(word).append(" ");
            } else {
                result.append(word.substring(0, 3)).append(" ");
            }
        }

        return result.toString().trim(); // Удаление лишних пробелов в конце
    }

    private String[] disciplinInfoToArray(String nowCell){
        return Arrays.stream(nowCell.replaceAll("\\d.*|\\(.*", "")
                .split("преп."))
                .map(String::trim)
                .toList().toArray(String[]::new);
    }

    public boolean isCellMergedWithAdjacentCells(Sheet sheet, int rowIndex, int columnIndex) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(rowIndex, columnIndex)) {
                // Проверка, объединена ли ячейка с ячейкой выше или ниже
                if (mergedRegion.getFirstRow() == rowIndex - 1 || mergedRegion.getLastRow() == rowIndex + 1) {
                    return true;
                }
            }
        }
        return false;
    }


}
