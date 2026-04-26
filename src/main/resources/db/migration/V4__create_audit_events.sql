create table audit_events (
    id uuid primary key,
    entity_type varchar(80) not null,
    entity_id uuid not null,
    action varchar(80) not null,
    actor varchar(120) not null,
    metadata_json text,
    created_at timestamp not null
);

create index idx_audit_events_entity on audit_events(entity_type, entity_id);
create index idx_audit_events_created_at on audit_events(created_at);
