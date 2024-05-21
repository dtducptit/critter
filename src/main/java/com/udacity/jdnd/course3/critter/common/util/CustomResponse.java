package com.udacity.jdnd.course3.critter.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class CustomResponse<T> implements Serializable {
    private String message;
    private List<T> data;

    public CustomResponse(List<T> data) {
        this.data = data;
        this.message = "";
    }
}
