package com.servicetrack.dto;

import com.servicetrack.entity.Complaint.Priority;
import com.servicetrack.entity.Complaint.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ComplaintResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Priority priority;
    private Status status;
    private String userEmail;
    private String assignedTechnician;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private List<CommentResponse> comments;
}
