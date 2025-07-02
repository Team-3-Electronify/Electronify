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
(1, 'admin', 'admin@example.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2'),
(2, 'user', 'user@example.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2');

-- User roles
INSERT INTO user_roles (user_id, roles) VALUES
(1, 'ADMIN'), -- admin user with ADMIN role
(2, 'USER'); -- user with USER role

-- Reviews for each product (3 reviews per product)
INSERT INTO reviews (id, rating, body, product_id, user_id) VALUES
-- iPhone 15 Pro (Product ID: 1)
(1, 5, 'Amazing phone! The camera quality is outstanding and the battery life is impressive.', 1, 2),
(2, 4, 'Great performance but a bit pricey. The new titanium design feels premium.', 1, 1),
(3, 5, 'Best iPhone yet! The Action Button is a game changer for productivity.', 1, 2),

-- Samsung Galaxy S24 Ultra (Product ID: 2)
(4, 5, 'Incredible display and the S Pen functionality is perfect for note-taking.', 2, 1),
(5, 4, 'Excellent camera zoom capabilities. The 200MP sensor produces stunning photos.', 2, 2),
(6, 5, 'Superior Android experience with great multitasking features.', 2, 1),

-- Anker Wireless Charger (Product ID: 3)
(7, 4, 'Reliable wireless charging. Works perfectly with my iPhone and AirPods.', 3, 2),
(8, 5, 'Great value for money. Charges fast and has a sleek design.', 3, 1),
(9, 4, 'Convenient and well-built. The LED indicator is helpful.', 3, 2),

-- MacBook Air M3 (Product ID: 4)
(10, 5, 'Incredible performance and battery life. Perfect for development work.', 4, 1),
(11, 5, 'Silent operation and lightning fast. The M3 chip is a beast!', 4, 2),
(12, 4, 'Great laptop but I wish it had more ports. The display is beautiful though.', 4, 1),

-- Dell XPS 15 Laptop (Product ID: 5)
(13, 4, 'Solid Windows laptop with great build quality. The 4K display is stunning.', 5, 2),
(14, 5, 'Perfect for content creation. The keyboard and trackpad feel premium.', 5, 1),
(15, 4, 'Good performance but runs a bit hot under heavy load. Overall satisfied.', 5, 2),

-- Logitech MX Master 3S Mouse (Product ID: 6)
(16, 5, 'Best mouse I have ever used! The scroll wheel is incredibly smooth.', 6, 1),
(17, 5, 'Ergonomic design and excellent precision. Great for long work sessions.', 6, 2),
(18, 4, 'Very good mouse but takes time to get used to all the buttons.', 6, 1),

-- Sony Alpha a7 IV Camera (Product ID: 7)
(19, 5, 'Professional-grade camera with exceptional image quality. Worth every penny.', 7, 2),
(20, 5, 'Amazing dynamic range and low-light performance. Perfect for portraits.', 7, 1),
(21, 4, 'Great camera but the menu system could be more intuitive.', 7, 2),

-- DJI Mini 4 Pro Drone (Product ID: 8)
(22, 5, 'Compact yet powerful drone. The obstacle avoidance works flawlessly.', 8, 1),
(23, 4, 'Great aerial photography capabilities. Easy to fly even for beginners.', 8, 2),
(24, 5, 'Excellent build quality and the 4K video recording is crystal clear.', 8, 1),

-- GoPro HERO12 Black (Product ID: 9)
(25, 4, 'Perfect action camera for adventures. The stabilization is incredible.', 9, 2),
(26, 5, 'Durable and reliable. Great for underwater and extreme sports.', 9, 1),
(27, 4, 'Good camera but battery life could be better during long recording sessions.', 9, 2),

-- Sony WH-1000XM5 Headphones (Product ID: 10)
(28, 5, 'Best noise-cancelling headphones on the market. Sound quality is pristine.', 10, 1),
(29, 5, 'Comfortable for hours of use. The adaptive sound control is fantastic.', 10, 2),
(30, 4, 'Great headphones but the touch controls can be sensitive sometimes.', 10, 1),

-- Samsung 65" QLED 4K TV (Product ID: 11)
(31, 5, 'Stunning picture quality with vibrant colors. Perfect for movie nights.', 11, 2),
(32, 4, 'Great smart TV features but the remote could be more intuitive.', 11, 1),
(33, 5, 'Excellent for gaming with low input lag. The HDR performance is outstanding.', 11, 2),

-- Bose SoundLink Revolve+ Speaker (Product ID: 12)
(34, 4, 'Great portable speaker with 360-degree sound. Perfect for outdoor use.', 12, 1),
(35, 5, 'Amazing bass and clear highs. The battery life is impressive.', 12, 2),
(36, 4, 'Good sound quality but could be louder for large gatherings.', 12, 1),

-- Apple Watch Series 9 (Product ID: 13)
(37, 5, 'Essential smartwatch with great health tracking features. Love the always-on display.', 13, 2),
(38, 4, 'Good fitness tracking but I wish the battery lasted longer than a day.', 13, 1),
(39, 5, 'Perfect integration with iOS. The Digital Crown is so responsive.', 13, 2),

-- Garmin Forerunner 265 (Product ID: 14)
(40, 5, 'Best running watch for serious athletes. GPS accuracy is spot-on.', 14, 1),
(41, 4, 'Great training features but the interface takes time to learn.', 14, 2),
(42, 5, 'Excellent battery life and the AMOLED display is beautiful.', 14, 1),

-- Oura Ring Gen3 (Product ID: 15)
(43, 4, 'Innovative health tracking in a small form factor. Sleep tracking is accurate.', 15, 2),
(44, 3, 'Interesting concept but the monthly subscription is a bit expensive.', 15, 1),
(45, 4, 'Good for passive health monitoring. The app provides useful insights.', 15, 2);
