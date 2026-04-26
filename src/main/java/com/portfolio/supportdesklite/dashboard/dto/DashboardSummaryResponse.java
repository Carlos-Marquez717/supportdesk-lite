package com.portfolio.supportdesklite.dashboard.dto;

public record DashboardSummaryResponse(
        long totalTickets,
        long openTickets,
        long inProgressTickets,
        long resolvedTickets,
        long slaBreachedTickets,
        double averageResolutionHours
) {
}
