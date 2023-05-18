package com.kimo.assignment.Response;

import com.kimo.assignment.Exception.Error;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    Error error;

    T data;

    public BaseResponse(Error error) {
        this.error = error;
        this.data = null;
    }

    public BaseResponse(T data) {
        this.data = data;
        this.error = null;
    }
}
