package com.example.monitoring.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class EmailExistException  extends RuntimeException{

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
