CREATE TABLE user_inf (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE,
    gender VARCHAR(10),
    address VARCHAR(255),
    email VARCHAR(255),
    phone_number VARCHAR(20)
);
