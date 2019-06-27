INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO user (created_at, updated_at, email, name, password, username) VALUES (
    NOW(), NOW(), 'viktor.antipin@gmail.com', 'Viktor',	'$2a$10$GWXBmyPHW1p420c2azuFAOOsrRSic.kidIHuOKRLjhCelBlfmq66O',	'admin'
);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);