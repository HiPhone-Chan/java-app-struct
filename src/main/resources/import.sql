insert into app_authority values ('ROLE_ADMIN');
insert into app_authority values ('ROLE_MANAGER');
insert into app_authority values ('ROLE_USER');
insert into app_authority values ('ROLE_ANONYMOUS');

insert into `app_user` (id, login, password_hash, mobile, activated, created_by, locked) values (1, 'admin', '$2a$10$y8yET4bE5nlvtxjjtmZ2oO4qy8Dlxi1Nw5qjkvYh5zJiC.8e4YWw.', '11111111111', true, 'system', 0);
insert into `app_user_authority` (user_id, authority_name) values (1, 'ROLE_ADMIN');
insert into `app_user` (id, login, password_hash, mobile, activated, created_by, locked) values (2, 'manager', '$2a$10$HPRUaUhN9N2YVXjj2ckpnedTT1yX3HRTcxQKZZYBq6P5QvJTeaApC', '11111111112', true, 'system', 0);
insert into `app_user_authority` (user_id, authority_name) values (2, 'ROLE_MANAGER');