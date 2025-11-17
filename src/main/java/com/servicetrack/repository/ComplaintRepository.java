package com.servicetrack.repository;

import com.servicetrack.entity.Complaint;
import com.servicetrack.entity.Complaint.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Complaint> findByStatusOrderByCreatedAtDesc(Status status);
    List<Complaint> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.status = :status")
    long countByStatus(Status status);
}
