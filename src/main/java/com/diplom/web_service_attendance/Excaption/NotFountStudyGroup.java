package com.diplom.web_service_attendance.Excaption;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class NotFountStudyGroup extends Exception {
    String name = "Группа не надена.\nПопробуйте ввести другую группу";
}
