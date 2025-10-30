

CREATE TABLE account (
    account_id SERIAL PRIMARY KEY, 
    username VARCHAR(100) NOT NULL UNIQUE,
    oauth2_id VARCHAR(100) UNIQUE,
    account_password VARCHAR(100) NOT NULL,
    user_email VARCHAR(255) NOT NULL UNIQUE,
    user_first_name VARCHAR(50),
    user_last_name VARCHAR(50),
    user_contact VARCHAR(255),
    user_location VARCHAR(255),
    registration_date DATE NOT NULL DEFAULT CURRENT_DATE,
    -- NEW COLUMN: Role specification
    account_role VARCHAR(20) NOT NULL CHECK (account_role IN ('trader', 'buyer', 'admin')) 
);


CREATE TABLE reservation (
    reservation_id SERIAL PRIMARY KEY,
    reservation_start TIMESTAMPTZ NOT NULL,
    reservation_end TIMESTAMPTZ NOT NULL,
    reservation_trader_grade INT,
    reservation_gear_grade INT,
    
    -- FK now references the unified account table
    buyer_id INT,
    -- We'll rely on application logic to ensure buyer_id links to an account with role='buyer'
    FOREIGN KEY (buyer_id) REFERENCES account(account_id) ON DELETE SET NULL
);

-- 4. Table: advertisement
CREATE TABLE advertisement (
    advertisement_id SERIAL PRIMARY KEY,
    advertisement_price DECIMAL(10, 2),
    advertisement_deposit DECIMAL(10, 2),
    advertisement_location_takeover VARCHAR(255),
    advertisement_location_return VARCHAR(255),
    advertisement_start DATE NOT NULL,
    advertisement_end DATE NOT NULL,
    
    -- FK now references the unified account table
    trader_id INT NOT NULL,
    -- We'll rely on application logic to ensure trader_id links to an account with role='trader'
    FOREIGN KEY (trader_id) REFERENCES account(account_id) ON DELETE CASCADE,

    reservation_id INT UNIQUE,
    item_description TEXT,
    item_image_path VARCHAR(255) UNIQUE,

    FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id) ON DELETE SET NULL
);



INSERT INTO account (username, account_password, oauth2_id, user_email, user_first_name, user_last_name, user_contact, user_location, account_role) VALUES
('alice_trader', 'pass1' ,'1','alice.smith@hire.com', 'Alice', 'Smith', '555-1001', 'London', 'trader'),        -- account_id 1 
('bob_buyer', 'pass1','2' ,'bob.johnson@mail.com', 'Bob', 'Johnson', '555-1002', 'Paris', 'buyer'),                -- account_id 2 
('max_admin', 'pass1' ,'3','max.chief@sys.com', 'Max', 'Chief', '555-1003', 'New York', 'admin'),              -- account_id 3 
('trader_gear', 'pass1' ,'4','gear.hire@global.com', 'George', 'Gear', '555-1004', 'Berlin', 'trader'),         -- account_id 4 
('jane_buyer', 'pass1' ,'5','jane.doe@mail.com', 'Jane', 'Doe', '555-1005', 'Tokyo', 'buyer'),                -- account_id 5 
('adminNH', 'oauth_login', '6', 'nh55636@fer.hr ', 'Nikola', 'Horvat', '0917343740', 'Zagreb', 'admin'),                                 -- account_id 6
('adminLK', 'oauth_login', '7', 'lk55939@fer.hr ', 'Leon', 'Katic', '0999999999', 'Zagreb', 'admin');
INSERT INTO reservation (reservation_start, reservation_end, buyer_id, reservation_trader_grade, reservation_gear_grade) VALUES
-- Reservation 1: Buyer Bob (ID 2) reserves a future item
('2025-11-10 10:00:00+01', '2025-11-12 18:00:00+01', 2, NULL, NULL), 
-- Reservation 2: Buyer Jane (ID 5) reserved a past item and left a grade
('2025-09-01 14:00:00+09', '2025-09-05 10:00:00+09', 5, 5, 4);   


INSERT INTO advertisement (trader_id, advertisement_start, advertisement_end, advertisement_price, advertisement_deposit, advertisement_location_takeover, advertisement_location_return, reservation_id, item_description, item_image_path) VALUES
-- Advertisement 1: Trader Alice (ID 1) - Available
(1, '2025-10-15', '2026-03-30', 50.00, 100.00, 'Piccadilly Circus', 'Piccadilly Circus', NULL,'Canon EOS R5 body, mint condition, includes 2 batteries.','/images/ad1/camera.jpg'),
-- Advertisement 2: Trader George (ID 4) - Currently Reserved by Bob (Reservation 1)
(4, '2025-11-01', '2025-11-30', 80.00, 150.00, 'Berlin Central Station', 'Berlin Central Station', 1,'RF 24-70mm f/2.8L IS USM lens.', '/images/ad1/lens.jpg');
-- Advertisement 3: Trader George (ID 4) - Completed Reservation by Jane (Reservation 2)




