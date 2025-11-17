package com.servicetrack.service;

import com.servicetrack.dto.ComplaintRequest;
import com.servicetrack.dto.ComplaintResponse;
import com.servicetrack.dto.CommentResponse;
import com.servicetrack.entity.Complaint;
import com.servicetrack.entity.Complaint.Status;
import com.servicetrack.entity.Role;
import com.servicetrack.entity.User;
import com.servicetrack.exception.ResourceNotFoundException;
import com.servicetrack.exception.UnauthorizedException;
import com.servicetrack.repository.ComplaintRepository;
import com.servicetrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public ComplaintResponse createComplaint(ComplaintRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Complaint complaint = new Complaint();
        complaint.setTitle(request.getTitle());
        complaint.setDescription(request.getDescription());
        complaint.setCategory(request.getCategory());
        complaint.setPriority(request.getPriority());
        complaint.setUser(user);
        
        complaint = complaintRepository.save(complaint);
        return mapToResponse(complaint);
    }
    
    public List<ComplaintResponse> getUserComplaints(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return complaintRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<ComplaintResponse> getAllComplaints() {
        return complaintRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public ComplaintResponse getComplaintById(Long id, String email) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (user.getRole() != Role.ADMIN && !complaint.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to view this complaint");
        }
        
        return mapToResponse(complaint);
    }
    
    @Transactional
    public ComplaintResponse updateComplaintStatus(Long id, Status status, String assignedTechnician, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (user.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admins can update complaint status");
        }
        
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));
        
        complaint.setStatus(status);
        complaint.setUpdatedAt(LocalDateTime.now());
        
        if (assignedTechnician != null && !assignedTechnician.isEmpty()) {
            complaint.setAssignedTechnician(assignedTechnician);
        }
        
        if (status == Status.RESOLVED || status == Status.CLOSED) {
            complaint.setResolvedAt(LocalDateTime.now());
        }
        
        complaint = complaintRepository.save(complaint);
        return mapToResponse(complaint);
    }
    
    private ComplaintResponse mapToResponse(Complaint complaint) {
        List<CommentResponse> comments = complaint.getComments().stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getMessage(),
                        comment.getUser().getEmail(),
                        comment.getUser().getFullName(),
                        comment.getCreatedAt()
                ))
                .collect(Collectors.toList());
        
        return new ComplaintResponse(
                complaint.getId(),
                complaint.getTitle(),
                complaint.getDescription(),
                complaint.getCategory(),
                complaint.getPriority(),
                complaint.getStatus(),
                complaint.getUser().getEmail(),
                complaint.getAssignedTechnician(),
                complaint.getCreatedAt(),
                complaint.getUpdatedAt(),
                complaint.getResolvedAt(),
                comments
        );
    }
}
