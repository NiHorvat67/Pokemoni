-- 1. Table: account (Independent)
CREATE TABLE account (
    account_id SERIAL PRIMARY KEY, 
    oauth2_id VARCHAR(100) UNIQUE,
    user_email VARCHAR(255) NOT NULL UNIQUE,
    user_first_name VARCHAR(50),
    user_last_name VARCHAR(50),
    user_contact VARCHAR(255),
    user_contact_email VARCHAR(255) UNIQUE,
    user_location VARCHAR(255),
    registration_date DATE NOT NULL DEFAULT CURRENT_DATE,
    account_role VARCHAR(20) NOT NULL CHECK (account_role IN ('trader', 'buyer', 'admin')),
    account_rating DECIMAL(2, 1)
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
    reservation_end TIMESTAMPTZ,
    reservation_grade INT,

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
CREATE OR REPLACE FUNCTION check_trader_role()
RETURNS TRIGGER AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM account
        WHERE account_id = NEW.trader_id
        AND account_role = 'trader'
    ) THEN
        RAISE EXCEPTION 'User ID % does not have trader role. Only traders can create advertisements.', NEW.trader_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_check_trader_role
BEFORE INSERT OR UPDATE ON advertisement
FOR EACH ROW EXECUTE FUNCTION check_trader_role();

INSERT INTO account (oauth2_id, user_email, user_first_name, user_last_name, user_contact, user_contact_email, user_location, account_role, account_rating) VALUES
('1', 'alice.smith@hire.com', 'Alice', 'Smith', '555-1001', 'alice.contact@hire.com', 'London', 'trader', 3.9),         -- 1 (Trader)
('2', 'bob.johnson@mail.com', 'Bob', 'Johnson', '555-1002', 'bob.contact@mail.com', 'Paris', 'buyer', NULL),            -- 2 (Buyer)
('3', 'charlie.t@mail.com', 'Charlie', 'Trader', '555-1003', 'charlie.contact@mail.com', 'New York', 'trader', 4.4),    -- 3 (Trader)
('4', 'gear.hire@global.com', 'George', 'Gear', '555-1004', 'george.contact@global.com', 'Berlin', 'trader', 4.1),      -- 4 (Trader)
('5', 'jane.doe@mail.com', 'Jane', 'Doe', '555-1005', 'jane.contact@mail.com', 'Tokyo', 'buyer', NULL),                 -- 5 (Buyer)
('6', 'nh55636@fer.hr', 'Nikola', 'Horvat', '0917343740', 'nikola.contact@fer.hr', 'Zagreb', 'admin', NULL),            -- 6 (Admin)
('7', 'lk55939@fer.hr', 'Leon', 'Katic', '0999999999', 'leon.contact@fer.hr', 'Zagreb', 'admin', NULL);                 -- 7 (Admin)
-- Insert into itemtype
INSERT INTO itemtype (itemtype_name) VALUES
('Skis'),                    -- 1
('Snowboard'),              -- 2
('Climbing Gear'),          -- 3
('Camping Equipment'),      -- 4
('Cycling Gear'),           -- 5
('Water Sports'),           -- 6
('Hiking Gear'),            -- 7
('Winter Sports'),          -- 8
('Mountain Equipment');     -- 9

-- Insert into advertisement (using traders: 1, 3, 4)
INSERT INTO advertisement (
    advertisement_price, advertisement_deposit,
    advertisement_location_takeover, advertisement_location_return,
    advertisement_start, advertisement_end, trader_id, itemtype_id, item_name, item_description, item_image_path
) VALUES
-- Alice's items (account_id 1)
(25.00, 100.00, 'London Sports Center', 'London Sports Center', '2024-01-15', '2024-04-15', 1, 1, 'Professional Skis Set', 'High-quality professional skis with poles, perfect for intermediate to advanced skiers.', '/images/skis1.jpg'),
(18.50, 75.00, 'London Downtown', 'London Downtown', '2024-01-10', '2024-12-31', 1, 2, 'Snowboard Package', 'Complete snowboard set with bindings, great for beginners.', '/images/snowboard1.jpg'),
(12.00, 50.00, 'London Outdoor Shop', 'London Outdoor Shop', '2024-02-01', '2024-11-30', 1, 7, 'Hiking Backpack 65L', 'Spacious hiking backpack with rain cover, multiple compartments.', '/images/backpack1.jpg'),

-- Charlie's items (account_id 3)
(35.00, 150.00, 'NYC Gear Hub', 'NYC Gear Hub', '2024-01-20', '2024-05-20', 3, 3, 'Professional Climbing Rope', '60m dynamic climbing rope, UIAA certified, used but in excellent condition.', '/images/climbing_rope1.jpg'),
(22.50, 120.00, 'Central Park Rental', 'Central Park Rental', '2024-03-01', '2024-10-31', 3, 5, 'Mountain Bike', 'Full-suspension mountain bike, 27.5 wheels, recent service.', '/images/mountain_bike1.jpg'),
(15.00, 60.00, 'NYC Waterfront', 'NYC Waterfront', '2024-05-01', '2024-09-30', 3, 6, 'Kayak Single Person', 'Lightweight kayak with paddle and life vest included.', '/images/kayak1.jpg'),

-- George's items (account_id 4)
(28.00, 200.00, 'Berlin Outdoor Center', 'Berlin Outdoor Center', '2024-01-25', '2024-03-31', 4, 8, 'Winter Sports Package', 'Complete winter gear: skis, boots, helmet, and goggles.', '/images/winter_package1.jpg'),
(20.00, 80.00, 'Berlin Camping World', 'Berlin Camping World', '2024-04-01', '2024-10-31', 4, 4, '4-Season Tent', 'High-quality 4-season tent for 2 people, waterproof and windproof.', '/images/tent1.jpg'),
(10.50, 40.00, 'Berlin City Location', 'Berlin City Location', '2024-01-15', '2024-12-15', 4, 7, 'Hiking Boots', 'Waterproof hiking boots, size 42, excellent grip and comfort.', '/images/boots1.jpg'),

-- More items for variety
(30.00, 180.00, 'London Ski Shop', 'London Ski Shop', '2024-12-01', '2025-03-31', 1, 1, 'Premium Ski Set', 'Top-of-the-line skis with advanced bindings for expert skiers.', '/images/skis2.jpg'),
(40.00, 250.00, 'NYC Climbing Gym', 'NYC Climbing Gym', '2024-02-01', '2024-12-31', 3, 3, 'Complete Climbing Set', 'Harness, carabiners, belay device, and chalk bag included.', '/images/climbing_set1.jpg'),
(24.00, 110.00, 'Berlin Bike Station', 'Berlin Bike Station', '2024-03-15', '2024-11-15', 4, 5, 'Road Bike', 'Lightweight road bike for city and long-distance cycling.', '/images/road_bike1.jpg');

-- Insert into reservation (using buyers: 2, 5 and connecting to advertisements)
INSERT INTO reservation (
    reservation_start, reservation_end, reservation_grade, buyer_id, advertisement_id
) VALUES
-- Bob's reservations (account_id 2)
('2024-02-10 09:00:00+00', '2024-02-17 18:00:00+00', 5, 2, 1),
('2024-03-15 10:00:00+00', '2024-03-22 17:00:00+00', 4,2, 5),
('2024-06-10 08:00:00+00', '2024-06-12 20:00:00+00', 5,2, 6),

-- Jane's reservations (account_id 5)
('2024-02-20 14:00:00+00', '2024-02-27 16:00:00+00', 4, 5, 2),
('2024-04-05 09:00:00+00', '2024-04-12 18:00:00+00', 5, 5, 8),
('2024-07-15 07:00:00+00', '2024-07-20 19:00:00+00', 4, 5, 9),

-- Mixed reservations
('2024-03-01 11:00:00+00', '2024-03-08 15:00:00+00', 5, 2, 3),
('2024-05-10 10:00:00+00', '2024-05-15 17:00:00+00', 4,5, 4),
('2024-08-20 08:00:00+00', '2024-08-25 18:00:00+00', 5, 2, 7);

-- Update advertisement table to connect with reservations (circular reference)
UPDATE advertisement SET reservation_id = 1 WHERE advertisement_id = 1;
UPDATE advertisement SET reservation_id = 2 WHERE advertisement_id = 2;
UPDATE advertisement SET reservation_id = 3 WHERE advertisement_id = 3;
UPDATE advertisement SET reservation_id = 4 WHERE advertisement_id = 4;
UPDATE advertisement SET reservation_id = 5 WHERE advertisement_id = 5;
UPDATE advertisement SET reservation_id = 6 WHERE advertisement_id = 6;
UPDATE advertisement SET reservation_id = 7 WHERE advertisement_id = 7;
UPDATE advertisement SET reservation_id = 8 WHERE advertisement_id = 8;
UPDATE advertisement SET reservation_id = 9 WHERE advertisement_id = 9;

-- Add more accounts for a richer dataset
INSERT INTO account (oauth2_id, user_email, user_first_name, user_last_name, user_contact,user_contact_email, user_location, account_role, account_rating) VALUES
('8', 'sarah.wilson@outdoor.com', 'Sarah', 'Wilson', '555-1008','sarah.wilson@outdoor.com', 'Munich', 'trader', 4.7),      -- 8
('9', 'mike.brown@adventure.com', 'Mike', 'Brown', '555-1009', 'mike.brown@adventure.com','Vienna', 'trader', 4.2),          -- 9
('10', 'lisa.garcia@travel.com', 'Lisa', 'Garcia', '555-1010', 'lisa.garcia@travel.com','Madrid', 'buyer', NULL),          -- 10
('11', 'david.miller@sports.com', 'David', 'Miller', '555-1011','david.miller@sports.com', 'Rome', 'buyer', NULL),           -- 11
('12', 'emma.jones@climb.com', 'Emma', 'Jones', '555-1012','emma.jones@climb.com','Zurich', 'trader', 4.9),              -- 12
('13', 'tom.davis@bike.com', 'Tom', 'Davis', '555-1013','tom.davis@bike.com', 'Amsterdam', 'trader', 4.5);               -- 13

-- Add more advertisements from new traders
INSERT INTO advertisement (
    advertisement_price, advertisement_deposit,
    advertisement_location_takeover, advertisement_location_return,
    advertisement_start, advertisement_end, trader_id, itemtype_id, item_name, item_description, item_image_path
) VALUES
-- Sarah's items (Munich)
(32.00, 160.00, 'Munich Sports Arena', 'Munich Sports Arena', '2024-02-01', '2024-11-30', 8, 1, 'Cross-country Skis', 'Professional cross-country skis with boots, perfect for winter training.', '/images/xc_skis1.jpg'),
(27.50, 130.00, 'Munich Outdoor', 'Munich Outdoor', '2024-04-01', '2024-09-30', 8, 6, 'Stand-up Paddleboard', 'Stable inflatable SUP with paddle and pump, great for lakes.', '/images/sup1.jpg'),

-- Mike's items (Vienna)
(19.00, 90.00, 'Vienna City Center', 'Vienna City Center', '2024-01-20', '2024-12-31', 9, 4, 'Camping Cook Set', 'Complete camping kitchen with stove, pots, and utensils.', '/images/cookset1.jpg'),
(14.50, 70.00, 'Vienna Outdoor Shop', 'Vienna Outdoor Shop', '2024-03-01', '2024-10-31', 9, 7, 'Sleeping Bag -10°C', 'Warm sleeping bag suitable for spring and autumn camping.', '/images/sleepingbag1.jpg'),

-- Emma's items (Zurich)
(45.00, 300.00, 'Zurich Climbing Center', 'Zurich Climbing Center', '2024-01-15', '2024-12-31', 12, 3, 'Advanced Climbing Gear', 'Professional climbing equipment including quickdraws and helmet.', '/images/climbing_advanced1.jpg'),
(29.00, 140.00, 'Zurich Lake', 'Zurich Lake', '2024-05-01', '2024-09-15', 12, 6, 'Double Kayak', 'Two-person kayak with paddles and safety equipment.', '/images/kayak_double1.jpg'),

-- Tom's items (Amsterdam)
(21.00, 100.00, 'Amsterdam Bike Central', 'Amsterdam Bike Central', '2024-02-15', '2024-11-30', 13, 5, 'City Bike', 'Comfortable city bike with basket and lock, perfect for urban exploration.', '/images/city_bike1.jpg'),
(16.50, 80.00, 'Amsterdam Outdoor', 'Amsterdam Outdoor', '2024-04-01', '2024-10-31', 13, 7, 'Day Hiking Pack', '25L daypack with hydration system, ideal for day trips.', '/images/daypack1.jpg');

-- Add more reservations from new buyers
INSERT INTO reservation (
    reservation_start, reservation_end, reservation_grade ,buyer_id, advertisement_id
) VALUES
-- Lisa's reservations (Madrid)
('2024-02-25 10:00:00+00', '2024-03-03 18:00:00+00', 5, 10, 10),
('2024-06-15 09:00:00+00', '2024-06-20 17:00:00+00', 4, 10, 11),

-- David's reservations (Rome)
('2024-03-10 08:00:00+00', '2024-03-17 19:00:00+00', 5, 11, 12),
('2024-07-01 07:00:00+00', '2024-07-07 20:00:00+00', 4, 11, 13),

-- More mixed reservations
('2024-04-15 11:00:00+00', '2024-04-22 16:00:00+00', 5,2, 14),
('2024-08-10 10:00:00+00', '2024-08-15 18:00:00+00', 4, 5, 15),
('2024-09-05 08:00:00+00', '2024-09-12 17:00:00+00', 5,10, 16);
-- Add more advertisements without reservations
INSERT INTO advertisement (
    advertisement_price, advertisement_deposit,
    advertisement_location_takeover, advertisement_location_return,
    advertisement_start, advertisement_end, trader_id, itemtype_id, item_name, item_description, item_image_path
) VALUES
-- Alice's additional items (account_id 1)
(22.00, 110.00, 'London Sports Center', 'London Sports Center', '2024-03-01', '2024-12-31', 1, 1, 'Beginner Ski Set', 'Perfect skis for beginners with soft flex and easy control.', '/images/skis_beginner1.jpg'),
(15.75, 65.00, 'London Downtown', 'London Downtown', '2024-02-15', '2024-11-30', 1, 2, 'Freestyle Snowboard', 'Twin-tip snowboard ideal for park and freestyle riding.', '/images/snowboard_freestyle1.jpg'),
(8.50, 35.00, 'London Outdoor Shop', 'London Outdoor Shop', '2024-03-10', '2024-10-31', 1, 7, 'Hiking Poles Set', 'Lightweight adjustable hiking poles with comfortable grips.', '/images/hiking_poles1.jpg'),

-- Charlie's additional items (account_id 3)
(42.00, 220.00, 'NYC Gear Hub', 'NYC Gear Hub', '2024-04-01', '2024-12-31', 3, 3, 'Climbing Shoes Set', 'Various sizes of climbing shoes for different skill levels.', '/images/climbing_shoes1.jpg'),
(18.00, 95.00, 'Central Park Rental', 'Central Park Rental', '2024-03-20', '2024-11-15', 3, 5, 'Hybrid Bike', 'Versatile hybrid bike for city and light trail use.', '/images/hybrid_bike1.jpg'),
(12.50, 55.00, 'NYC Waterfront', 'NYC Waterfront', '2024-06-01', '2024-09-15', 3, 6, 'Canoe 2-Person', 'Stable canoe perfect for river and lake exploration.', '/images/canoe1.jpg'),

-- George's additional items (account_id 4)
(33.00, 180.00, 'Berlin Outdoor Center', 'Berlin Outdoor Center', '2024-11-01', '2025-02-28', 4, 8, 'Snowboard Package Pro', 'High-performance snowboard with step-in bindings.', '/images/snowboard_pro1.jpg'),
(25.50, 120.00, 'Berlin Camping World', 'Berlin Camping World', '2024-05-01', '2024-09-30', 4, 4, 'Family Camping Tent', 'Spacious 4-person tent with separate rooms.', '/images/family_tent1.jpg'),
(11.00, 45.00, 'Berlin City Location', 'Berlin City Location', '2024-04-01', '2024-12-31', 4, 7, 'Waterproof Jacket', 'Breathable waterproof jacket for all weather conditions.', '/images/waterproof_jacket1.jpg'),

-- Sarah's additional items (account_id 8)
(28.50, 140.00, 'Munich Sports Arena', 'Munich Sports Arena', '2024-09-01', '2025-03-31', 8, 1, 'All-Mountain Skis', 'Versatile skis suitable for all types of terrain.', '/images/all_mountain_skis1.jpg'),
(20.00, 85.00, 'Munich Outdoor', 'Munich Outdoor', '2024-05-15', '2024-08-31', 8, 6, 'Inflatable Kayak', 'Compact inflatable kayak perfect for travel and storage.', '/images/inflatable_kayak1.jpg'),

-- Mike's additional items (account_id 9)
(16.25, 75.00, 'Vienna City Center', 'Vienna City Center', '2024-04-10', '2024-10-31', 9, 4, 'Camping Hammock', 'Comfortable camping hammock with mosquito net.', '/images/camping_hammock1.jpg'),
(13.75, 60.00, 'Vienna Outdoor Shop', 'Vienna Outdoor Shop', '2024-03-01', '2024-11-30', 9, 7, 'Camping Stove', 'Portable gas stove with wind protection.', '/images/camping_stove1.jpg'),

-- Emma's additional items (account_id 12)
(38.00, 250.00, 'Zurich Climbing Center', 'Zurich Climbing Center', '2024-02-01', '2024-12-31', 12, 3, 'Bouldering Crash Pad', 'Large crash pad for safe bouldering outdoors.', '/images/crash_pad1.jpg'),
(24.50, 110.00, 'Zurich Lake', 'Zurich Lake', '2024-06-01', '2024-08-31', 12, 6, 'Stand-up Paddleboard Premium', 'High-quality rigid SUP with carbon fiber paddle.', '/images/sup_premium1.jpg'),

-- Tom's additional items (account_id 13)
(19.25, 90.00, 'Amsterdam Bike Central', 'Amsterdam Bike Central', '2024-03-01', '2024-12-31', 13, 5, 'Electric City Bike', 'E-bike with pedal assist for easy city commuting.', '/images/ebike_city1.jpg'),
(14.00, 65.00, 'Amsterdam Outdoor', 'Amsterdam Outdoor', '2024-04-15', '2024-10-31', 13, 7, 'Camping Chair Set', 'Comfortable lightweight camping chairs.', '/images/camping_chairs1.jpg'),

-- More diverse items from various traders
(50.00, 300.00, 'London Premium Outlet', 'London Premium Outlet', '2024-01-01', '2024-12-31', 1, 9, 'Mountain Expedition Pack', 'Professional backpack for multi-day mountain expeditions.', '/images/expedition_pack1.jpg'),
(35.75, 175.00, 'NYC Adventure Store', 'NYC Adventure Store', '2024-03-01', '2024-11-30', 3, 9, 'Portable Power Station', 'Solar-ready power station for off-grid adventures.', '/images/power_station1.jpg'),
(27.25, 130.00, 'Berlin Tech Gear', 'Berlin Tech Gear', '2024-02-01', '2024-12-31', 4, 7, 'GPS Navigation Device', 'Rugged GPS with topographic maps and long battery life.', '/images/gps_device1.jpg'),
(31.50, 160.00, 'Munich Winter Sports', 'Munich Winter Sports', '2024-11-15', '2025-03-15', 8, 8, 'Ice Climbing Gear', 'Complete ice climbing set with axes and crampons.', '/images/ice_climbing1.jpg'),
(22.75, 115.00, 'Vienna Photography', 'Vienna Photography', '2024-04-01', '2024-10-31', 9, 7, 'Outdoor Photography Kit', 'Camera protection and accessories for outdoor photography.', '/images/photo_kit1.jpg');




-- Migration script for Stripe Connect implementation
-- This creates a separate mapping table for Account ID to Stripe Connect Account ID

-- Create the stripe_connect_account mapping table
CREATE TABLE IF NOT EXISTS stripe_connect_account (
    stripe_connect_account_id SERIAL PRIMARY KEY,
    account_id INTEGER NOT NULL UNIQUE,
    stripe_account_id VARCHAR(255) NOT NULL UNIQUE,
    account_status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_stripe_connect_account_id ON stripe_connect_account(account_id);
CREATE INDEX IF NOT EXISTS idx_stripe_account_id ON stripe_connect_account(stripe_account_id);

-- Optional: If you had existing data in account table, you would migrate it here
-- INSERT INTO stripe_connect_account (account_id, stripe_account_id, account_status)
-- SELECT account_id, stripe_connect_account_id, stripe_account_status
-- FROM account
-- WHERE stripe_connect_account_id IS NOT NULL;
CREATE TABLE payment (
    payment_id SERIAL PRIMARY KEY,
    payer_id INT NOT NULL,
    payment_description VARCHAR(255),
    payment_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP, -- Added for better tracking
    
    FOREIGN KEY (payer_id) REFERENCES account(account_id) ON DELETE SET NULL
);

-- 2. Table: report
-- Allows users to report other users (e.g., for bad behavior or scams)
CREATE TABLE report (
    report_id SERIAL PRIMARY KEY,
    reporter_id INT NOT NULL,  -- The user filing the report
    reported_id INT NOT NULL,  -- The user being reported
    report_details TEXT,
    report_status VARCHAR(20) DEFAULT 'pending' CHECK (report_status IN ('pending', 'reviewed', 'resolved', 'dismissed')),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (reporter_id) REFERENCES account(account_id) ON DELETE CASCADE,
    FOREIGN KEY (reported_id) REFERENCES account(account_id) ON DELETE CASCADE
);



INSERT INTO payment (payer_id, payment_description) VALUES
(2, 'Payment for Skis rental (Reservation #1)'),           -- Bob paying
(2, 'Security deposit for Hiking Gear'),                   -- Bob paying
(5, 'Snowboard rental fee'),                               -- Jane paying
(5, 'Late return fee for Camping Equipment'),              -- Jane paying
(10, 'Payment for Madrid Ski rental'),                     -- Lisa paying
(11, 'Full payment for Climbing Gear in Rome');            -- David paying

-- Insert dummy data into report
INSERT INTO report (reporter_id, reported_id, report_details, report_status) VALUES
-- Bob (Buyer) reports George (Trader)
(2, 4, 'The item description was misleading. The tent was not waterproof as stated.', 'pending'),

-- Jane (Buyer) reports Alice (Trader)
(5, 1, 'Trader was 45 minutes late for the handover meeting.', 'reviewed'),

-- Alice (Trader) reports Bob (Buyer)
(1, 2, 'returned the skis with significant scratch damage on the base.', 'resolved'),

-- Charlie (Trader) reports Jane (Buyer)
(3, 5, 'User cancelled last minute and refused to pay the cancellation fee.', 'dismissed'),

-- Nikola (Admin) logs a report against a suspicious account
(6, 13, 'Suspicious activity detected on this account. Investigating for fraud.', 'pending');



CREATE TABLE subscription_price (
    price INTEGER NOT NULL
);

INSERT INTO subscription_price (price) VALUES (100000);



-- FIX

ALTER TABLE reservation DROP CONSTRAINT reservation_buyer_id_fkey;
ALTER TABLE reservation ADD CONSTRAINT reservation_buyer_id_fkey 
    FOREIGN KEY (buyer_id) REFERENCES account(account_id) ON DELETE CASCADE;

ALTER TABLE payment DROP CONSTRAINT payment_payer_id_fkey;
ALTER TABLE payment ADD CONSTRAINT payment_payer_id_fkey 
    FOREIGN KEY (payer_id) REFERENCES account(account_id) ON DELETE CASCADE;



ALTER TABLE reservation DROP CONSTRAINT fk_advertisement;
ALTER TABLE reservation ADD CONSTRAINT fk_advertisement 
    FOREIGN KEY (advertisement_id) REFERENCES advertisement(advertisement_id) ON DELETE CASCADE;