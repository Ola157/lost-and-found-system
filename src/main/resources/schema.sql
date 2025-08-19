-- Persistent logins table for remember-me functionality
create table persistent_logins (username varchar(64) not null,
                                series varchar(64) primary key,
                                token varchar(64) not null,
                                last_used timestamp not null);

-- Users table
create table users (id integer not null auto_increment,
                    username varchar(50) not null unique,
                    password varchar(255) not null,
                    enabled boolean not null default true,
                    primary key (id));

-- Roles table
create table roles (id integer not null auto_increment,
                    role_name varchar(50) not null unique,
                    primary key (id));

-- Authorities table (user-role mapping)
create table authorities (id integer not null auto_increment,
                          username varchar(50) not null,
                          authority varchar(50) not null,
                          primary key (id),
                          foreign key (username) references users(username));

-- Login success table
create table login_success (id integer not null auto_increment,
                            username varchar(50) not null,
                            source_ip varchar(100),
                            created_date timestamp,
                            primary key (id));

-- Login failure table
create table login_failure (id integer not null auto_increment,
                            username varchar(50) not null,
                            source_ip varchar(100),
                            created_date timestamp,
                            primary key (id));

-- Lost and found items table
create table lost_found_item (id varchar(36) not null,
                              created_timestamp timestamp,
                              last_modified_timestamp timestamp,
                              version integer,
                              category varchar(20),
                              created_by varchar(50),
                              description varchar(255),
                              image_file_name varchar(255),
                              lost_found_date date,
                              lost_found_location varchar(150),
                              modified_by varchar(50),
                              reporter_email varchar(100),
                              reporter_name varchar(50),
                              reporter_phone_no varchar(20),
                              title varchar(150),
                              type varchar(10),
                              user_id integer,
                              primary key (id),
                              foreign key (user_id) references users(id));