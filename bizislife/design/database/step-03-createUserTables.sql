 CREATE  TABLE users (
  userid CHAR(60) NOT NULL,
  username VARCHAR(255) NOT NULL ,
  password VARCHAR(255) NOT NULL ,
  enabled BIT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (userid));
  
CREATE TABLE user_roles (
  user_role_id int(11) zerofill NOT NULL AUTO_INCREMENT,
  userid CHAR(60) NOT NULL,
  role varchar(255) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_userid_role (role,userid),
  KEY fk_userid_idx (userid),
  CONSTRAINT fk_username FOREIGN KEY (userid) REFERENCES users (userid));  