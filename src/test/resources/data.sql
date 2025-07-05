-- Test data for integration tests

-- Categories
INSERT INTO categories (id, name) VALUES (1, 'Smartphones & Accessories');
INSERT INTO categories (id, name) VALUES (2, 'Laptops & Computers');
INSERT INTO categories (id, name) VALUES (3, 'Gaming');

-- Users (password is 'password123' for all users)
INSERT INTO users (username, email, password) VALUES ('testuser', 'test@example.com', '$2a$10$39Ioql0b9MW8sbK64R4TauEN4qc7.rSaMjJYN7bF.JOdJHnQnu8CC');
INSERT INTO users (username, email, password) VALUES ('adminuser', 'admin@example.com', '$2a$10$39Ioql0b9MW8sbK64R4TauEN4qc7.rSaMjJYN7bF.JOdJHnQnu8CC');
INSERT INTO users (username, email, password) VALUES ('john', 'john@example.com', '$2a$10$39Ioql0b9MW8sbK64R4TauEN4qc7.rSaMjJYN7bF.JOdJHnQnu8CC');
INSERT INTO users (username, email, password) VALUES ('existinguser', 'existing@example.com', '$2a$10$39Ioql0b9MW8sbK64R4TauEN4qc7.rSaMjJYN7bF.JOdJHnQnu8CC');

-- User roles
INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'testuser'), 'USER');
INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'adminuser'), 'ADMIN');
INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'john'), 'USER');
INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'existinguser'), 'USER');

-- Products
INSERT INTO products (name, price, image_url, featured, category_id, rating, review_count) VALUES ('iPhone 15 Pro', 999.99, 'https://example.com/iphone.jpg', true, 1, 4.5, 2);
INSERT INTO products (name, price, image_url, featured, category_id, rating, review_count) VALUES ('Samsung Galaxy S24 Ultra', 1199.99, 'https://example.com/samsung.jpg', true, 1, 4.6, 1);
INSERT INTO products (name, price, image_url, featured, category_id, rating, review_count) VALUES ('MacBook Pro 16', 2499.99, 'https://example.com/macbook.jpg', true, 2, 4.8, 1);
INSERT INTO products (name, price, image_url, featured, category_id, rating, review_count) VALUES ('Gaming Laptop ASUS ROG', 1899.99, 'https://example.com/asus.jpg', false, 3, 4.3, 1);

-- Reviews
INSERT INTO reviews (rating, body, product_id, user_id) VALUES (4.5, 'Great phone, excellent camera!', 1, (SELECT id FROM users WHERE username = 'testuser'));
INSERT INTO reviews (rating, body, product_id, user_id) VALUES (4.5, 'Love the design and performance', 1, (SELECT id FROM users WHERE username = 'john'));
INSERT INTO reviews (rating, body, product_id, user_id) VALUES (4.6, 'Amazing display quality', 2, (SELECT id FROM users WHERE username = 'testuser'));
INSERT INTO reviews (rating, body, product_id, user_id) VALUES (4.8, 'Perfect for development work', 3, (SELECT id FROM users WHERE username = 'adminuser'));
INSERT INTO reviews (rating, body, product_id, user_id) VALUES (4.3, 'Good gaming performance', 4, (SELECT id FROM users WHERE username = 'john')); 