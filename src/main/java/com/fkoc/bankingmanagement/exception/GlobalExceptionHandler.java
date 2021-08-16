package com.fkoc.bankingmanagement.exception;

import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(Exception e){
        logger.error("An error occurs "+ e);
        return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity handleRuntimeException(Exception e){
        logger.error("An error occurs "+ e);
        return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

}
