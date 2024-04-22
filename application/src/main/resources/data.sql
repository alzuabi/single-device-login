INSERT INTO ROLES (ID, ROLE_NAME, CREATED_ON, LAST_UPDATED_ON, VERSION)
VALUES (1, 'ROLE_CUSTOMER', now(), now(), 0),
       (2, 'ROLE_ADMIN', now(), now(), 0),
       (3, 'ROLE_GENERAL', now(), now(), 0);

INSERT INTO AUTHORITIES (ID, AUTHORITY_NAME, CREATED_ON, LAST_UPDATED_ON, VERSION)
VALUES (1, 'READ', now(), now(), 0),
       (2, 'WRITE', now(), now(), 0),
       (3, 'DELETE', now(), now(), 0);

INSERT INTO ROLES_AUTHORITIES (ROLE_ID, AUTHORITY_ID)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 1);