package com.kimo.assignment.Exception.Controller;

import com.kimo.assignment.Exception.NotFound;
import com.kimo.assignment.Exception.RequestParamNotValidException;
import com.kimo.assignment.Response.BaseResponse;
import com.kimo.assignment.Exception.Error;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleAllGenericException(Exception ex) {
        log.error("Exception stack:{}", ex);
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.builder().error(new Error(ex.getMessage())).build());
    }

    @ExceptionHandler(value = RequestParamNotValidException.class)
    public ResponseEntity<?> requestParamNotValidException(Exception ex) {
        log.error("RequestParamNotValidException:",ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder().error(new Error(ex.getMessage())).build());
    }

    @ExceptionHandler(value = NotFound.class)
    public ResponseEntity<?> NotFound(Exception ex) {
        log.error("RequestParamNotValidException:",ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.builder().error(new Error(ex.getMessage())).build());
    }

}

