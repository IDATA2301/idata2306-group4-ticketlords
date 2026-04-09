CREATE TABLE "category" (
  "category_id" bigserial PRIMARY KEY,
  "category_name" varchar(50) UNIQUE NOT NULL
);

create table "event_venue" (
"venue_id" bigserial primary key,
"arena" VARCHAR not null,
"city" VARCHAR,
"country" VARCHAR not null,
"address" VARCHAR not null
);

CREATE TABLE "event" (
  "event_id" bigint PRIMARY KEY,
  "event_name" varchar(255) NOT NULL,
  "host" varchar(255),
  "category_id" bigint references "category"("category_id"),
  "event_date_start" date,
  "event_date_end" date,
  "venue_id" bigint references "event_venue"("venue_id"),
  --"image_url" text,
  "event_description" text,
  "total_clicks" bigint default 0 not null
);

CREATE TABLE "booking_site" (
  "ticket_vendor_id" bigint PRIMARY KEY,
  "ticket_vendor" varchar(100),
  "website_link" text,
  "booking_site_description" text
);

CREATE TABLE "ticket" (
  "ticket_id" bigserial primary key,
  "event_id" bigint REFERENCES "event" ("event_id") on delete cascade,
  "ticket_vendor_id" bigint references "booking_site" ("ticket_vendor_id") on delete cascade,
  "price" decimal (10, 2) check (price >= 0),
  --"currency" varchar(10) default 'NOK',
  "amount_available" int,
  "ticket_type" varchar(100),
  "ticket_link" varchar,
  "ticket_description" text
);

CREATE TABLE "unregistered_user" (
  "user_id" bigserial primary key,
  "first_seen" timestamp default now()
);

CREATE TABLE "registered_user" (
  "display_name" varchar,
  "user_id" bigint primary key REFERENCES "unregistered_user"("user_id") ON DELETE CASCADE,
  "email" varchar unique not null,
  "first_name" varchar(60),
  "last_name" varchar(60),
  "password" varchar not null,
  "phonenumber" int,
  "user_role" varchar(255) default 'USER'
);

CREATE TABLE "review" (
  "user_id" bigint references "registered_user"("user_id") on delete CASCADE,
  "ticket_vendor_id" bigint references "booking_site"("ticket_vendor_id") on delete CASCADE,
  "score" decimal(2, 1) check (score >=1.0 and score <= 5.0),
  "review_content" text,
  primary key ("user_id", "ticket_vendor_id")
);

CREATE TABLE "event_clicks" (
    "event_id" bigint REFERENCES "event"("event_id") ON DELETE CASCADE,
    "user_id" bigint REFERENCES "unregistered_user"("user_id") ON DELETE CASCADE,
    "clicked_at" TIMESTAMP DEFAULT NOW(),
    
    -- This prevents the same user clicking the same event twice
    PRIMARY KEY ("event_id", "user_id")
);

create table "wishlist" (
"user_id" bigint references "registered_user"("user_id") on delete cascade,
"event_id" bigint references "event"("event_id") on delete cascade,
"added_at" TIMESTAMP default NOW(),
primary key ("user_id", "event_id")
);

CREATE TABLE "price_alert" (
  "alert_id" bigserial PRIMARY KEY,
  "user_id" bigint REFERENCES "registered_user"("user_id") ON DELETE CASCADE,
  "event_id" bigint REFERENCES "event"("event_id") ON DELETE CASCADE,
  "target_price" decimal(10, 2),
  "is_active" boolean DEFAULT true
);

CREATE TABLE "user_interest" (
  "user_id" bigint REFERENCES "registered_user"("user_id") ON DELETE CASCADE,
  "category_name" varchar(100), 
  "interest_score" int not null DEFAULT 0,
  PRIMARY KEY ("user_id", "category_name")
);

create table "search_log" (
"search_id" bigserial primary key,
"user_id" bigint references "unregistered_user"("user_id") on delete cascade,
"search_query" text not null,
"searched_at" TIMESTAMP default NOW()
);


CREATE OR REPLACE FUNCTION update_event_click_count()
RETURNS TRIGGER AS $$
BEGIN
    -- Increment the total_clicks in the events table
    UPDATE "event"
    SET "total_clicks" = "total_clicks" + 1
    WHERE "event_id" = NEW."event_id";
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- calls update_event_click_count() when a event_clicks gets a new insert
CREATE TRIGGER after_click_inserted
AFTER INSERT ON "event_clicks"
FOR EACH ROW
EXECUTE FUNCTION update_event_click_count();
