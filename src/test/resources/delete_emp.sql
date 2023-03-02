delete from departments where depart_name <> 'default';
delete from roles where role not in ('ROLE_ADMIN','ROLE_USER');
--delete from users where login <> '$$$admin_login$$$';
delete from users where login <> 'admin';