create table tickets (
    id uuid primary key,
    title varchar(160) not null,
    description text not null,
    status varchar(40) not null,
    priority varchar(40) not null,
    category varchar(60) not null,
    requester_name varchar(120) not null,
    requester_email varchar(160) not null,
    assigned_agent_id uuid references agents(id),
    due_at timestamp not null,
    sla_breached boolean not null default false,
    resolved_at timestamp,
    resolution_summary text,
    created_at timestamp not null,
    updated_at timestamp not null,
    deleted_at timestamp
);

create index idx_tickets_status on tickets(status);
create index idx_tickets_priority on tickets(priority);
create index idx_tickets_due_at on tickets(due_at);
create index idx_tickets_assigned_agent on tickets(assigned_agent_id);
create index idx_tickets_sla_breached on tickets(sla_breached);
