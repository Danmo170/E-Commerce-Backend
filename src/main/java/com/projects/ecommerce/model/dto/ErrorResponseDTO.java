package com.projects.ecommerce.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDTO {

    private LocalDateTime timeStamp;

    private int status;

    private String error;

    private String message;

    private String path;

}
