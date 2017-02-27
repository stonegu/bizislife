-- sha-1 password is stone0331 --
INSERT INTO users(userid,username,password,enabled)
VALUES ('89c16ede-0a3b-45ac-ba7f-8dd9c0a7de4d','stone','85a486d4d33189912ea205ec4f188566022cd47e', true);

-- sha-1 password is mandy0620 --
INSERT INTO users(userid,username,password,enabled)
VALUES ('0475eeb3-fd41-4fb9-adbd-2f7ecfce2a0e','mandy','f8aa8fbbe15f7b5ca06013d6064ca03c8e05a4d6', true);

-- sha-1 password is erica0620 --
INSERT INTO users(userid,username,password,enabled)
VALUES ('c54e99f4-76a5-4939-b819-254cfda01c83','erica','488d9b88148d55d470b328b3aa29409d381243cb', false);



INSERT INTO user_roles (userid, role)
VALUES ('89c16ede-0a3b-45ac-ba7f-8dd9c0a7de4d', 'ROLE_ADMIN');

INSERT INTO user_roles (userid, role)
VALUES ('0475eeb3-fd41-4fb9-adbd-2f7ecfce2a0e', 'ROLE_USER');

INSERT INTO user_roles (userid, role)
VALUES ('c54e99f4-76a5-4939-b819-254cfda01c83', 'ROLE_USER');

