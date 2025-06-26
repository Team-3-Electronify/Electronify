INSERT INTO categories (id, name) VALUES
(1, 'Smartphones & Accessories'),
(2, 'Computers & Laptops'),
(3, 'Cameras & Drones'),
(4, 'Audio & Video'),
(5, 'Wearable Technology');

INSERT INTO products (id, name, price, image_url, featured, category_id, rating, review_count) VALUES
-- Smartphones & Accessories
(1, 'iPhone 15 Pro', 999.99, 'https://example.com/images/iphone15.jpg', true, 1, 4.8, 250),
(2, 'Samsung Galaxy S24 Ultra', 1299.99, 'https://example.com/images/s24ultra.jpg', true, 1, 4.7, 180),
(3, 'Anker Wireless Charger', 49.99, 'https://example.com/images/ankercharger.jpg', false, 1, 4.5, 500),

-- Computers & Laptops
(4, 'MacBook Air M3', 1099.00, 'https://example.com/images/macbookair.jpg', true, 2, 4.9, 150),
(5, 'Dell XPS 15 Laptop', 1599.00, 'https://example.com/images/dellxps15.jpg', true, 2, 4.6, 120),
(6, 'Logitech MX Master 3S Mouse', 99.99, 'https://example.com/images/logitechmouse.jpg', false, 2, 4.8, 800),

-- Cameras & Drones
(7, 'Sony Alpha a7 IV Camera', 2499.99, 'https://example.com/images/sonya7iv.jpg', true, 3, 4.9, 90),
(8, 'DJI Mini 4 Pro Drone', 759.00, 'https://example.com/images/djimini4.jpg', true, 3, 4.7, 130),
(9, 'GoPro HERO12 Black', 399.99, 'https://example.com/images/gopro12.jpg', false, 3, 4.6, 210),

-- Audio & Video
(10, 'Sony WH-1000XM5 Headphones', 399.99, 'https://example.com/images/sonyxm5.jpg', true, 4, 4.8, 1100),
(11, 'Samsung 65" QLED 4K TV', 1499.99, 'https://example.com/images/samsungtv.jpg', true, 4, 4.7, 350),
(12, 'Bose SoundLink Revolve+ Speaker', 329.00, 'https://example.com/images/bosespeaker.jpg', false, 4, 4.6, 600),

-- Wearable Technology
(13, 'Apple Watch Series 9', 399.00, 'https://example.com/images/applewatch9.jpg', true, 5, 4.8, 950),
(14, 'Garmin Forerunner 265', 449.99, 'https://example.com/images/garmin265.jpg', true, 5, 4.7, 280),
(15, 'Oura Ring Gen3', 299.00, 'https://example.com/images/ouraring.jpg', false, 5, 4.5, 450);

-- Users
-- Password for both users is 'password123'
INSERT INTO users (id, username, email, password) VALUES
(1, 'admin', 'admin@example.com', '$2a$10$VNoiCe5z.i8qKx2nyg26A.zG43TqlHqP1N4.l6y/iwuN9.q3v5FvS'),
(2, 'user', 'user@example.com', '$2a$10$VNoiCe5z.i8qKx2nyg26A.zG43TqlHqP1N4.l6y/iwuN9.q3v5FvS');

INSERT INTO user_roles (user_id, roles) VALUES
(1, 'ADMIN'), -- admin user with ADMIN role
(2, 'USER'); -- user with USER role