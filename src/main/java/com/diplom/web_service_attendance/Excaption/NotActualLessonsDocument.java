package com.diplom.web_service_attendance.Excaption;

public class NotActualLessonsDocument extends RuntimeException {
    public NotActualLessonsDocument(String message) {
        super(message);
    }
}
