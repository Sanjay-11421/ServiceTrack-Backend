package com.servicetrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String message;
    private String userEmail;
    private String userName;
    private LocalDateTime createdAt;
}
