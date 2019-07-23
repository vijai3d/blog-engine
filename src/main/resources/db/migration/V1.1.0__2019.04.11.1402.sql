INSERT INTO roles(id, name) VALUES(1, 'ROLE_USER');
INSERT INTO roles(id, name) VALUES(2, 'ROLE_AUTHOR');
INSERT INTO roles(id, name) VALUES(3, 'ROLE_ADMIN');

INSERT INTO user (created_at, updated_at, email, name, password, username, domain, enabled) VALUES (
    NOW(), NOW(), 'viktor.antipin@gmail.com', 'Viktor',	'$2a$10$GWXBmyPHW1p420c2azuFAOOsrRSic.kidIHuOKRLjhCelBlfmq66O',	'admin', 'marshrut', 1
);
INSERT INTO user (created_at, updated_at, email, name, password, username, domain, enabled) VALUES (
    NOW(), NOW(), 'viktor.antipin.learning@gmail.com', 'Author', '$2a$10$GWXBmyPHW1p420c2azuFAOOsrRSic.kidIHuOKRLjhCelBlfmq66O',	'author222', 'marshrut', 1
                                                                                                   );
INSERT INTO user_roles (user_id, role_id) VALUES (1, 3);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);