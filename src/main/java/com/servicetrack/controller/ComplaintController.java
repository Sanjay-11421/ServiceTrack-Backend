package com.servicetrack.controller;

import com.servicetrack.dto.ComplaintRequest;
import com.servicetrack.dto.ComplaintResponse;
import com.servicetrack.dto.CommentRequest;
import com.servicetrack.dto.CommentResponse;
import com.servicetrack.entity.Complaint.Status;
import com.servicetrack.service.ComplaintService;
import com.servicetrack.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {
    
    private final ComplaintService complaintService;
    private final CommentService commentService;
    
    @PostMapping
    public ResponseEntity<ComplaintResponse> createComplaint(
            @Valid @RequestBody ComplaintRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(complaintService.createComplaint(request, authentication.getName()));
    }
    
    @GetMapping
    public ResponseEntity<List<ComplaintResponse>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }
    
    @GetMapping("/my-complaints")
    public ResponseEntity<List<ComplaintResponse>> getUserComplaints(Authentication authentication) {
        return ResponseEntity.ok(complaintService.getUserComplaints(authentication.getName()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getComplaintById(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(complaintService.getComplaintById(id, authentication.getName()));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<ComplaintResponse> updateComplaintStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates,
            Authentication authentication) {
        Status status = Status.valueOf(updates.get("status"));
        String assignedTechnician = updates.get("assignedTechnician");
        return ResponseEntity.ok(complaintService.updateComplaintStatus(
                id, status, assignedTechnician, authentication.getName()));
    }
    
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(commentService.addComment(id, request, authentication.getName()));
    }
}
