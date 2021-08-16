package com.fkoc.bankingmanagement.exception;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    public void givenEntityNotFoundExceptionWhenHandleEntityNotFoundExceptionThenReturnResponseEntity() {
        // Arrange
        final EntityNotFoundException exception = new EntityNotFoundException("Account not found");

        // Act
        ResponseEntity responseEntity = handler.handleEntityNotFoundException(exception);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Account not found");
    }

    @Test
    public void givenRuntimeExceptionWhenHandleRuntimeExceptionThenReturnResponseEntity() {
        // Arrange
        final RuntimeException exception = new RuntimeException("Insufficient balance");

        // Act
        ResponseEntity responseEntity = handler.handleRuntimeException(exception);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("Insufficient balance");
    }

}
