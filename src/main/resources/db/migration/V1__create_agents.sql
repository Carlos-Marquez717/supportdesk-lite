create table agents (
    id uuid primary key,
    name varchar(120) not null,
    email varchar(160) not null unique,
    role varchar(40) not null,
    active boolean not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

create index idx_agents_active on agents(active);
