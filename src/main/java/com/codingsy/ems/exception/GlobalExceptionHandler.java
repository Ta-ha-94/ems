package com.codingsy.ems.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	// ✅ Handles "Resource Not Found" Errors
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // ✅ Handles Validation Errors (Like salary being too low)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        // Extract field validation errors
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex){
    	Map<String, String> response = new HashMap<>();
    	response.put("error", ex.getMessage());
    	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(exception = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
    	Map<String, String> response = new HashMap<>();
    	response.put("error", "Invalid value for field '" + ex.getName() + "'. Expected type: " + ex.getRequiredType().getSimpleName());
    	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    // ✅ Handles Database Integrity Constraint Violations (Unique email, foreign key issues, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityException(DataIntegrityViolationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", extractDuplicateValue(ex.getMostSpecificCause().getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 🔍 Extract the duplicate entry value from the SQL error message
    private String extractDuplicateValue(String message) {
        Pattern pattern = Pattern.compile("Duplicate entry '(.*?)'");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return "Duplicate entry '" + matcher.group(1) + "'";
        }

        return "Duplicate entry found";
    }
    
 // ✅ Handles Any Other Unexpected Exception (Generic Handler)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
//        Map<String, String> response = new HashMap<>();
//        response.put("error", "An unexpected error occurred: " + ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
