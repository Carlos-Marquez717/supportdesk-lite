package com.portfolio.supportdesklite.dashboard.controller;

import com.portfolio.supportdesklite.dashboard.dto.DashboardSummaryResponse;
import com.portfolio.supportdesklite.dashboard.dto.GroupedMetricResponse;
import com.portfolio.supportdesklite.dashboard.dto.RecentTicketResponse;
import com.portfolio.supportdesklite.dashboard.service.DashboardService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public DashboardSummaryResponse summary() {
        return dashboardService.summary();
    }

    @GetMapping("/tickets-by-priority")
    public List<GroupedMetricResponse> ticketsByPriority() {
        return dashboardService.ticketsByPriority();
    }

    @GetMapping("/tickets-by-agent")
    public List<GroupedMetricResponse> ticketsByAgent() {
        return dashboardService.ticketsByAgent();
    }

    @GetMapping("/recent-tickets")
    public List<RecentTicketResponse> recentTickets(@RequestParam(defaultValue = "10") int limit) {
        return dashboardService.recentTickets(limit);
    }
}
