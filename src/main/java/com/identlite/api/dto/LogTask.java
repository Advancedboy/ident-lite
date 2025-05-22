package com.identlite.api.dto;

import com.identlite.api.enums.LogTaskStatus;
import lombok.Data;

@Data
public class LogTask {
    private String id;
    private LogTaskStatus status;
    private String filePath;
    private String error;
}
