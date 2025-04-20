CREATE DATABASE IF NOT EXISTS ecomdb;
USE ecomdb;

CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    description TEXT,
    stock_quantity INT NOT NULL
);

CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    product_id INT,
    quantity INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE TABLE order_table (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    order_date DATE,
    total_price DOUBLE,
    shipping_address VARCHAR(255),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE order_item (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT,
    FOREIGN KEY (order_id) REFERENCES order_table(order_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE TABLE feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    feedback_message TEXT,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

INSERT INTO customer (name, email, password)
VALUES ('John Doe', 'john@example.com', 'password123');

INSERT INTO product (name, price, description, stock_quantity)
VALUES ('Chair', 149.99, 'Ergonomic office chair', 20);

ALTER TABLE product CHANGE stock_quantity stock INT;

describe product;

INSERT INTO customer (name, email, password)
VALUES ('John Doe', 'john@example.com', 'password123');

INSERT INTO product (name, price, description, stock)
VALUES ('Chair', 149.99, 'Ergonomic office chair', 20);
