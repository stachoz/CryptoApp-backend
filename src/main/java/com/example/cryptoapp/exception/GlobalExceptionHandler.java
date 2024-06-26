package com.example.cryptoapp.exception;

import com.binance.connector.client.exceptions.BinanceClientException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    ResponseEntity<?> handleBadRequest(RuntimeException exception, HttpServletRequest request){
        ApiError apiError = new ApiError(
                exception.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(value = {UserNotFoundException.class, NoSuchElementException.class})
    ResponseEntity<?> handle(RuntimeException exception, HttpServletRequest request){
        ApiError apiError = new ApiError(
                exception.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request){
        List<String> errors = new ArrayList<>();
        exception.getFieldErrors().forEach(fieldError -> errors.add(fieldError.getField() + " " + fieldError.getDefaultMessage()));
        ApiValidationError body = new ApiValidationError(
                "validation failed",
                LocalDateTime.now(),
                request.getRequestURI(),
                errors
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(value = {ValidationException.class})
    ResponseEntity<?> handleValidationException(ValidationException exception, HttpServletRequest request){
        ApiValidationError body = new ApiValidationError(
                "validation failed",
                LocalDateTime.now(),
                request.getRequestURI(),
                exception.getErrors()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(value = {OperationConflictException.class})
    ResponseEntity<?> handleOperationConflict(OperationConflictException exception, HttpServletRequest request){
        ApiError body = new ApiError(
                exception.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    ResponseEntity<?> handleEmptyRequestBody(HttpServletRequest request){
        ApiError body = new ApiError(
                "Required request body is missing",
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BinanceClientException.class})
    ResponseEntity<?> handleBinanceClientException(BinanceClientException e, HttpServletRequest request){
        ApiError body = new ApiError(
                e.getErrMsg(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}