package com.servicetrack.controller;

import com.servicetrack.dto.DashboardResponse;
import com.servicetrack.entity.Complaint.Status;
import com.servicetrack.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    
    private final ComplaintRepository complaintRepository;
    
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboardStats() {
        long total = complaintRepository.count();
        long pending = complaintRepository.countByStatus(Status.PENDING);
        long inProgress = complaintRepository.countByStatus(Status.IN_PROGRESS);
        long resolved = complaintRepository.countByStatus(Status.RESOLVED);
        long closed = complaintRepository.countByStatus(Status.CLOSED);
        
        return ResponseEntity.ok(new DashboardResponse(total, pending, inProgress, resolved, closed));
    }
}
