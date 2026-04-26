insert into agents (id, name, email, role, active, created_at, updated_at)
values
    ('11111111-1111-1111-1111-111111111111', 'Joaquin Ureta', 'joaquin@example.com', 'ADMIN', true, now(), now()),
    ('22222222-2222-2222-2222-222222222222', 'Sofia Ramirez', 'sofia@example.com', 'SUPPORT_AGENT', true, now(), now()),
    ('33333333-3333-3333-3333-333333333333', 'Viewer Demo', 'viewer@example.com', 'VIEWER', false, now(), now());

insert into tickets (
    id, title, description, status, priority, category, requester_name, requester_email,
    assigned_agent_id, due_at, sla_breached, resolved_at, resolution_summary, created_at, updated_at, deleted_at
)
values
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        'No puedo acceder a mi cuenta',
        'El login devuelve error 500 al intentar iniciar sesion desde el portal.',
        'IN_PROGRESS',
        'HIGH',
        'ACCOUNT',
        'Maria Perez',
        'maria@example.com',
        '22222222-2222-2222-2222-222222222222',
        now() + interval '24 hours',
        false,
        null,
        null,
        now() - interval '2 hours',
        now() - interval '1 hours',
        null
    ),
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        'Error en facturacion mensual',
        'La factura descargada no coincide con el plan contratado por la empresa.',
        'OPEN',
        'MEDIUM',
        'BILLING',
        'Carlos Soto',
        'carlos@example.com',
        null,
        now() + interval '48 hours',
        false,
        null,
        null,
        now() - interval '1 hours',
        now() - interval '1 hours',
        null
    ),
    (
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        'Incidente API publica',
        'El endpoint publico de integraciones responde con latencia elevada.',
        'RESOLVED',
        'URGENT',
        'INCIDENT',
        'Ana Torres',
        'ana@example.com',
        '11111111-1111-1111-1111-111111111111',
        now() - interval '1 hours',
        true,
        now() - interval '30 minutes',
        'Se optimizo la consulta principal y se normalizo el tiempo de respuesta.',
        now() - interval '6 hours',
        now() - interval '30 minutes',
        null
    );

insert into ticket_comments (id, ticket_id, author_name, author_email, visibility, content, created_at)
values
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Sofia Ramirez', 'sofia@example.com', 'INTERNAL', 'Se revisan logs del servicio de autenticacion.', now() - interval '90 minutes'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'Joaquin Ureta', 'joaquin@example.com', 'PUBLIC', 'El incidente fue resuelto y el servicio opera normalmente.', now() - interval '25 minutes');

insert into audit_events (id, entity_type, entity_id, action, actor, metadata_json, created_at)
values
    ('99999999-9999-9999-9999-999999999991', 'TICKET', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'TICKET_CREATED', 'maria@example.com', '{"priority":"HIGH"}', now() - interval '2 hours'),
    ('99999999-9999-9999-9999-999999999992', 'TICKET', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'TICKET_ASSIGNED', 'system', '{"agentId":"22222222-2222-2222-2222-222222222222"}', now() - interval '1 hours'),
    ('99999999-9999-9999-9999-999999999993', 'TICKET', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'TICKET_RESOLVED', 'system', null, now() - interval '30 minutes');
