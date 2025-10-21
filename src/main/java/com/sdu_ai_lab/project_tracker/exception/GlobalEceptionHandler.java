package com.sdu_ai_lab.project_tracker.exception;

import com.sdu_ai_lab.project_tracker.dto.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalEceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage());
        var errorDto = new ErrorResponseDto(
                "Entity not found",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(exception = {
            IllegalArgumentException.class,
            IllegalStateException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ){
        log.error(ex.getMessage());

        var errorDto = new ErrorResponseDto("Illegal Argument Exception", ex.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }



}
