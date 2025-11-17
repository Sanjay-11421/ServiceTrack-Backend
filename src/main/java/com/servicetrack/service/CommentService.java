package com.servicetrack.service;

import com.servicetrack.dto.CommentRequest;
import com.servicetrack.dto.CommentResponse;
import com.servicetrack.entity.Comment;
import com.servicetrack.entity.Complaint;
import com.servicetrack.entity.User;
import com.servicetrack.exception.ResourceNotFoundException;
import com.servicetrack.repository.CommentRepository;
import com.servicetrack.repository.ComplaintRepository;
import com.servicetrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public CommentResponse addComment(Long complaintId, CommentRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));
        
        Comment comment = new Comment();
        comment.setMessage(request.getMessage());
        comment.setComplaint(complaint);
        comment.setUser(user);
        
        comment = commentRepository.save(comment);
        
        return new CommentResponse(
                comment.getId(),
                comment.getMessage(),
                user.getEmail(),
                user.getFullName(),
                comment.getCreatedAt()
        );
    }
}
