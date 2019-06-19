insert into authority values ('ROLE_ADMIN');
insert into authority values ('ROLE_MANAGER');
insert into authority values ('ROLE_USER');
insert into authority values ('ROLE_ANONYMOUS');

insert into `user` (id, login, password_hash, mobile, activated, created_by, locked) values (1, 'admin', '$2a$10$y8yET4bE5nlvtxjjtmZ2oO4qy8Dlxi1Nw5qjkvYh5zJiC.8e4YWw.', '11111111111', true, 'system', 0);
insert into `user_authority` (user_id, authority_name) values (1, 'ROLE_ADMIN');
