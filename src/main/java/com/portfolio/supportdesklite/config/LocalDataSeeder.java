package com.portfolio.supportdesklite.config;

import com.portfolio.supportdesklite.agent.model.Agent;
import com.portfolio.supportdesklite.agent.model.AgentRole;
import com.portfolio.supportdesklite.agent.repository.AgentRepository;
import com.portfolio.supportdesklite.comment.model.CommentVisibility;
import com.portfolio.supportdesklite.comment.model.TicketComment;
import com.portfolio.supportdesklite.comment.repository.TicketCommentRepository;
import com.portfolio.supportdesklite.ticket.model.Ticket;
import com.portfolio.supportdesklite.ticket.model.TicketCategory;
import com.portfolio.supportdesklite.ticket.model.TicketPriority;
import com.portfolio.supportdesklite.ticket.model.TicketStatus;
import com.portfolio.supportdesklite.ticket.repository.TicketRepository;
import com.portfolio.supportdesklite.ticket.service.SlaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalDataSeeder {

    @Bean
    CommandLineRunner seedLocalData(
            AgentRepository agentRepository,
            TicketRepository ticketRepository,
            TicketCommentRepository ticketCommentRepository,
            SlaService slaService
    ) {
        return args -> {
            if (agentRepository.count() > 0 || ticketRepository.count() > 0) {
                return;
            }

            Agent admin = createAgent("Joaquin Ureta", "joaquin@example.com", AgentRole.ADMIN, true);
            Agent support = createAgent("Sofia Ramirez", "sofia@example.com", AgentRole.SUPPORT_AGENT, true);
            agentRepository.save(admin);
            agentRepository.save(support);

            Ticket accountTicket = createTicket(
                    "No puedo acceder a mi cuenta",
                    "El login devuelve error 500 desde el portal principal.",
                    TicketPriority.HIGH,
                    TicketCategory.ACCOUNT,
                    "Maria Perez",
                    "maria@example.com",
                    slaService
            );
            accountTicket.setStatus(TicketStatus.IN_PROGRESS);
            accountTicket.setAssignedAgent(support);

            Ticket billingTicket = createTicket(
                    "Error en facturacion mensual",
                    "La factura descargada no coincide con el plan contratado.",
                    TicketPriority.MEDIUM,
                    TicketCategory.BILLING,
                    "Carlos Soto",
                    "carlos@example.com",
                    slaService
            );

            ticketRepository.save(accountTicket);
            ticketRepository.save(billingTicket);

            TicketComment comment = new TicketComment();
            comment.setTicket(accountTicket);
            comment.setAuthorName("Sofia Ramirez");
            comment.setAuthorEmail("sofia@example.com");
            comment.setVisibility(CommentVisibility.INTERNAL);
            comment.setContent("Se revisan logs del servicio de autenticacion.");
            ticketCommentRepository.save(comment);
        };
    }

    private Agent createAgent(String name, String email, AgentRole role, boolean active) {
        Agent agent = new Agent();
        agent.setName(name);
        agent.setEmail(email);
        agent.setRole(role);
        agent.setActive(active);
        return agent;
    }

    private Ticket createTicket(
            String title,
            String description,
            TicketPriority priority,
            TicketCategory category,
            String requesterName,
            String requesterEmail,
            SlaService slaService
    ) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(priority);
        ticket.setCategory(category);
        ticket.setRequesterName(requesterName);
        ticket.setRequesterEmail(requesterEmail);
        ticket.setDueAt(slaService.calculateDueAt(priority));
        ticket.setSlaBreached(false);
        return ticket;
    }
}
