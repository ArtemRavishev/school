package artefact.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SchoolExceptionHandler {


@ExceptionHandler({FacultyNotFoundException.class,StudentNotFondException.class})
    public ResponseEntity<?>handleNotFound(RuntimeException e){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

