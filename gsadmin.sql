-- 1. Table: account (Independent)
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
    account_role VARCHAR(20) NOT NULL CHECK (account_role IN ('trader', 'buyer', 'admin')) 
);

---

-- 2. Table: itemtype (Independent)
CREATE TABLE itemtype (
  itemtype_id SERIAL PRIMARY KEY,
  itemtype_name character varying
);

---

-- 3. Table: reservation (Deferred FK to advertisement)
CREATE TABLE reservation (
    reservation_id SERIAL PRIMARY KEY,
    reservation_start TIMESTAMPTZ NOT NULL,
    reservation_end TIMESTAMPTZ NOT NULL,
    reservation_trader_grade INT,
    reservation_gear_grade INT,
    
    buyer_id INT,
    FOREIGN KEY (buyer_id) REFERENCES account(account_id) ON DELETE SET NULL,
    
    advertisement_id INT -- Column for the FK (Constraint added later)
);

---

-- 4. Table: advertisement (Deferred FK to reservation)
CREATE TABLE advertisement (
    advertisement_id SERIAL PRIMARY KEY,
    advertisement_price DECIMAL(10, 2),
    advertisement_deposit DECIMAL(10, 2),
    advertisement_location_takeover VARCHAR(255),
    advertisement_location_return VARCHAR(255),
    advertisement_start DATE NOT NULL,
    advertisement_end DATE NOT NULL,
    
    trader_id INT NOT NULL,
    FOREIGN KEY (trader_id) REFERENCES account(account_id) ON DELETE CASCADE,

    itemtype_id INT,
    FOREIGN KEY (itemtype_id) REFERENCES itemtype(itemtype_id),
    
    reservation_id INT UNIQUE, -- Column for the FK (Constraint added later)
    item_name VARCHAR(255),
    item_description TEXT,
    item_image_path VARCHAR(255) UNIQUE
);

---


-- Add the Foreign Key from reservation to advertisement
ALTER TABLE reservation
ADD CONSTRAINT fk_advertisement
    FOREIGN KEY (advertisement_id) 
    REFERENCES advertisement(advertisement_id)
    ON DELETE SET NULL;

-- Add the Foreign Key from advertisement to reservation
ALTER TABLE advertisement
ADD CONSTRAINT fk_reservation
    FOREIGN KEY (reservation_id) 
    REFERENCES reservation(reservation_id)
    ON DELETE SET NULL;

---


-- Insert into account (IDs 1-7)
INSERT INTO account (username, account_password, oauth2_id, user_email, user_first_name, user_last_name, user_contact, user_location, account_role) VALUES
('alice_trader', 'pass1' ,'1','alice.smith@hire.com', 'Alice', 'Smith', '555-1001', 'London', 'trader'),         -- 1 (Trader)
('bob_buyer', 'pass1','2' ,'bob.johnson@mail.com', 'Bob', 'Johnson', '555-1002', 'Paris', 'buyer'),                 -- 2 (Buyer)
('charlie_trader', 'pass1' ,'3','charlie.t@mail.com', 'Charlie', 'Trader', '555-1003', 'New York', 'trader'),     -- 3 (Trader)
('george_trader', 'pass1' ,'4','gear.hire@global.com', 'George', 'Gear', '555-1004', 'Berlin', 'trader'),         -- 4 (Trader)
('jane_buyer', 'pass1' ,'5','jane.doe@mail.com', 'Jane', 'Doe', '555-1005', 'Tokyo', 'buyer'),                  -- 5 (Buyer)
('adminNH', 'oauth_login', '6', 'nh55636@fer.hr ', 'Nikola', 'Horvat', '0917343740', 'Zagreb', 'admin'),         -- 6 (Admin)
('adminLK', 'oauth_login', '7', 'lk55939@fer.hr ', 'Leon', 'Katic', '0999999999', 'Zagreb', 'admin');           -- 7 (Admin)

-- Insert into itemtype (IDs 1-5)
INSERT INTO itemtype (itemtype_name) VALUES
('Camera Lens'),   -- 1
('Drone'),         -- 2
('Projector'),     -- 3
('Musical Instrument'), -- 4
('Ski Gear');      -- 5

-- Step 1 of data linking: Insert advertisements (Ads 1-3)
-- We insert these first, then link them to reservations using UPDATE later.
INSERT INTO advertisement (trader_id, advertisement_start, advertisement_end, advertisement_price, advertisement_deposit, advertisement_location_takeover, advertisement_location_return, itemtype_id, item_name,item_description, item_image_path) VALUES
(1, '2025-10-15', '2026-03-30', 50.00, 100.00, 'Piccadilly Circus', 'Piccadilly Circus', 1, 'Canon Printer','Canon EOS R5 body, mint condition.','/images/ad1/camera.jpg'), -- Ad ID 1 (Alice)
(4, '2025-11-01', '2025-11-30', 80.00, 150.00, 'Berlin Central Station', 'Berlin Central Station', 1,'Lenses', 'RF 24-70mm f/2.8L IS USM lens.', '/images/ad2/lens.jpg'), -- Ad ID 2 (George)
(3, '2025-12-01', '2025-12-10', 15.00, 50.00, 'London Bridge, LDN', 'London Bridge, LDN', 3, 'Projektor','Portable mini projector.', '/images/ad3/projector.jpg'); -- Ad ID 3 (Charlie)

-- Step 2 of data linking: Insert reservations (Res 1-3)
-- These reference the newly created advertisement_ids.
INSERT INTO reservation (reservation_start, reservation_end, buyer_id, advertisement_id, reservation_trader_grade, reservation_gear_grade) VALUES
('2025-11-10 10:00:00+01', '2025-11-12 18:00:00+01', 2, 2, NULL, NULL), -- Res ID 1: Bob reserves Ad ID 2 (Future)
('2025-09-01 14:00:00+09', '2025-09-05 10:00:00+09', 5, 3, 5, 4),      -- Res ID 2: Jane reserved Ad ID 3 (Past, Graded)
('2026-01-15 08:00:00+01', '2026-01-20 20:00:00+01', 2, 1, NULL, NULL); -- Res ID 3: Bob reserves Ad ID 1 (Future)

-- Step 3 of data linking: Update advertisements
-- This completes the circular link for the data.
UPDATE advertisement SET reservation_id = 1 WHERE advertisement_id = 2; -- Ad 2 is linked to Res 1
UPDATE advertisement SET reservation_id = 2 WHERE advertisement_id = 3; -- Ad 3 is linked to Res 2
UPDATE advertisement SET reservation_id = 3 WHERE advertisement_id = 1; -- Ad 1 is linked to Res 3
