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
