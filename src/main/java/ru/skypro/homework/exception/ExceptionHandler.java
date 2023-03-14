package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<String> handlesImageNotFoundException(ImageNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("Волантер с id = %d не найден", e.getId()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AdsNotFoundException.class)
    public ResponseEntity<String> handlesAdsNotFoundException(AdsNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("Волантер с id = %d не найден", e.getId()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AvatarNotFoundException.class)
    public ResponseEntity<String> handlesAvatarNotFoundException(AvatarNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("Волантер с id = %d не найден", e.getId()));
    }
}
