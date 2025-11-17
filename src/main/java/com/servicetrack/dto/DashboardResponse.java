package com.servicetrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardResponse {
    private long totalComplaints;
    private long pendingComplaints;
    private long inProgressComplaints;
    private long resolvedComplaints;
    private long closedComplaints;
}
