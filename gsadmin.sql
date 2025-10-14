-- The 'user' table is renamed to 'account'

-- 1. Supertype Table: account
CREATE TABLE account (
    account_id SERIAL PRIMARY KEY, 
    username VARCHAR(100) NOT NULL UNIQUE,
    account_password VARCHAR(100) NOT NULL,
    user_email VARCHAR(255) NOT NULL UNIQUE,
    user_first_name VARCHAR(50),
    user_last_name VARCHAR(50),
    user_contact VARCHAR(255),
    user_location VARCHAR(255),
    registration_date DATE NOT NULL DEFAULT CURRENT_DATE
);

-- 2. Subtype Table: trader (Inherits from account)
CREATE TABLE trader (
    account_id INT PRIMARY KEY,
    FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE
);

-- 3. Subtype Table: buyer (Inherits from account)
CREATE TABLE buyer (
    account_id INT PRIMARY KEY,
    FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE
);

-- 4. Subtype Table: admin (Inherits from account)
CREATE TABLE admin (
    account_id INT PRIMARY KEY,
    FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE
);

-- 5. Table: reservation
CREATE TABLE reservation (
    reservation_id SERIAL PRIMARY KEY,
    reservation_start TIMESTAMPTZ NOT NULL,
    reservation_end TIMESTAMPTZ NOT NULL,
    reservation_trader_grade INT,
    reservation_gear_grade INT,
    
    -- FK reference updated to point to buyer(account_id)
    buyer_id INT,
    FOREIGN KEY (buyer_id) REFERENCES buyer(account_id) ON DELETE SET NULL
);

-- 6. Table: advertisement
CREATE TABLE advertisement (
    advertisement_id SERIAL PRIMARY KEY,
    advertisement_price DECIMAL(10, 2),
    advertisement_deposit DECIMAL(10, 2),
    advertisement_location_takeover VARCHAR(255),
    advertisement_location_return VARCHAR(255),
    advertisement_start DATE NOT NULL,
    advertisement_end DATE NOT NULL,
    
    -- FK reference updated to point to trader(account_id)
    trader_id INT NOT NULL,
    FOREIGN KEY (trader_id) REFERENCES trader(account_id) ON DELETE CASCADE,

    reservation_id INT UNIQUE,
    FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id) ON DELETE SET NULL
);

-- 7. Table: item
CREATE TABLE item (
    item_id SERIAL PRIMARY KEY,
    item_description TEXT,
    item_image_path VARCHAR(255) UNIQUE, 
    
    advertisement_id INT NOT NULL,
    FOREIGN KEY (advertisement_id) REFERENCES advertisement(advertisement_id) ON DELETE CASCADE
);




-- Add Dummy data


INSERT INTO account (username, account_password, user_email, user_first_name, user_last_name, user_contact, user_location) VALUES
('alice_trader', 'pass1' ,'alice.smith@hire.com', 'Alice', 'Smith', '555-1001', 'London'),        -- account_id 1 (Trader)
('bob_buyer', 'pass1' ,'bob.johnson@mail.com', 'Bob', 'Johnson', '555-1002', 'Paris'),                -- account_id 2 (Buyer)
('max_admin', 'pass1' ,'max.chief@sys.com', 'Max', 'Chief', '555-1003', 'New York'),              -- account_id 3 (Admin)
('trader_gear', 'pass1' ,'gear.hire@global.com', 'George', 'Gear', '555-1004', 'Berlin'),         -- account_id 4 (Trader)
('jane_buyer', 'pass1' ,'jane.doe@mail.com', 'Jane', 'Doe', '555-1005', 'Tokyo');                 -- account_id 5 (Buyer)



-- Trader (IDs 1, 4)
INSERT INTO trader (account_id) VALUES
(1),
(4);

-- Buyer (IDs 2, 5)
INSERT INTO buyer (account_id) VALUES
(2),
(5);

-- Admin (ID 3)
INSERT INTO admin (account_id) VALUES
(3);



INSERT INTO reservation (reservation_start, reservation_end, buyer_id, reservation_trader_grade, reservation_gear_grade) VALUES
-- Reservation 1: Buyer Bob (ID 2) reserves a future item
('2025-11-10 10:00:00+01', '2025-11-12 18:00:00+01', 2, NULL, NULL), 
-- Reservation 2: Buyer Jane (ID 5) reserved a past item and left a grade
('2025-09-01 14:00:00+09', '2025-09-05 10:00:00+09', 5, 5, 4);   


INSERT INTO advertisement (trader_id, advertisement_start, advertisement_end, advertisement_price, advertisement_deposit, advertisement_location_takeover, advertisement_location_return, reservation_id) VALUES
-- Advertisement 1: Trader Alice (ID 1) - Available
(1, '2025-10-15', '2026-03-30', 50.00, 100.00, 'Piccadilly Circus', 'Piccadilly Circus', NULL), 
-- Advertisement 2: Trader George (ID 4) - Currently Reserved by Bob (Reservation 1)
(4, '2025-11-01', '2025-11-30', 80.00, 150.00, 'Berlin Central Station', 'Berlin Central Station', 1),          
-- Advertisement 3: Trader George (ID 4) - Completed Reservation by Jane (Reservation 2)
(4, '2025-08-20', '2025-09-10', 60.00, 100.00, 'Tokyo Shibuya Crossing', 'Tokyo Shibuya Crossing', 2);            


INSERT INTO item (advertisement_id, item_description, item_image_path) VALUES
-- Items for Advertisement 1 (Camera Gear)
(1, 'Canon EOS R5 body, mint condition, includes 2 batteries.', '/images/ad1/camera.jpg'),
(1, 'RF 24-70mm f/2.8L IS USM lens.', '/images/ad1/lens.jpg'),
-- Item for Advertisement 2 (Drone)
(2, 'DJI Mavic 3 Pro drone, Fly More Kit.', '/images/ad2/drone.jpg');


SELECT 'Demo Data Insertion Complete' AS Status;