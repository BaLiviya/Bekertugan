-- Table: users
CREATE TABLE users (
  id       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
)
  ENGINE = InnoDB;


-- Insert data



INSERT INTO users VALUES (1, 'admin', '$2a$11$uSXS6rLJ91WjgOHhEGDx..VGs7MkKZV68Lv5r1uwFu7HgtRn3dcXG');
INSERT INTO users VALUES (2, 'user', '$2a$11$uSXS6rLJ91WjgOHhEGDx..VGs7MkKZV68Lv5r1uwFu7HgtRn3dcXG');
