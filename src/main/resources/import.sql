insert into authority values ('ROLE_ADMIN');
insert into authority values ('ROLE_MANAGER');
insert into authority values ('ROLE_STAFF');
insert into authority values ('ROLE_USER');
insert into authority values ('ROLE_ANONYMOUS');

insert into `user` (id, login, password_hash, mobile, activated, created_by, locked) values (1, 'admin', '$2a$10$y8yET4bE5nlvtxjjtmZ2oO4qy8Dlxi1Nw5qjkvYh5zJiC.8e4YWw.', '11111111111', true, 'system', 0);
insert into `user_authority` (user_id, authority_name) values (1, 'ROLE_ADMIN');
insert into `user` (id, login, password_hash, mobile, activated, created_by, locked) values (2, 'manager', '$2a$10$HPRUaUhN9N2YVXjj2ckpnedTT1yX3HRTcxQKZZYBq6P5QvJTeaApC', '11111111111', true, 'system', 0);
insert into `user_authority` (user_id, authority_name) values (2, 'ROLE_MANAGER');
insert into `user` (id, login, password_hash, mobile, activated, created_by, locked) values (3, 'staff', '$2a$10$Zh/DkDcMVX0kilKHalpJjeVbrrz14fmOa/4bXzxjD1KOY7HGGEWEa', '11111111111', true, 'system', 0);
insert into `user_authority` (user_id, authority_name) values (3, 'ROLE_STAFF');

insert into `staff` (staff_id, user_id) values ('000000', 3);