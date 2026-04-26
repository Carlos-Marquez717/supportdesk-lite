create table ticket_comments (
    id uuid primary key,
    ticket_id uuid not null references tickets(id),
    author_name varchar(120) not null,
    author_email varchar(160) not null,
    visibility varchar(30) not null,
    content text not null,
    created_at timestamp not null
);

create index idx_ticket_comments_ticket_id on ticket_comments(ticket_id);
