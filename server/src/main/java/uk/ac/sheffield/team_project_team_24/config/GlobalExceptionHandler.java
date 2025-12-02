package uk.ac.sheffield.team_project_team_24.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.ac.sheffield.team_project_team_24.dto.ApiErrorDTO;
import uk.ac.sheffield.team_project_team_24.exception.user.UsernameExistsException;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<ApiErrorDTO> handleUsernameExists(UsernameExistsException ex) {
        ApiErrorDTO errorDTO = new ApiErrorDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldname = error instanceof FieldError fieldError
                    ? fieldError.getField()
                    : error.getObjectName();
            errors.computeIfAbsent(fieldname, k -> new ArrayList<>())
                    .add(error.getDefaultMessage());
        });

        ApiErrorDTO errorDTO = new ApiErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Some of the form values were invalid",
                errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }
}
