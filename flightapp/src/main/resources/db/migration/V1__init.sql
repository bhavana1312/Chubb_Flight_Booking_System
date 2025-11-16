CREATE DATABASE IF NOT EXISTS flightdb;
USE flightdb;

CREATE TABLE airline (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(20) NOT NULL,
  name VARCHAR(255) NOT NULL,
  logo_url VARCHAR(512)
);

CREATE TABLE inventory (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  airline_id BIGINT NOT NULL,
  flight_number VARCHAR(50) NOT NULL,
  departure_datetime DATETIME NOT NULL,
  arrival_datetime DATETIME NOT NULL,
  origin VARCHAR(10) NOT NULL,
  destination VARCHAR(10) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_inventory_airline FOREIGN KEY (airline_id) REFERENCES airline(id)
);

CREATE TABLE seat_class (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  inventory_id BIGINT NOT NULL,
  class_type VARCHAR(50) NOT NULL,
  total_seats INT NOT NULL,
  available_seats INT NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  refundable BOOLEAN DEFAULT false,
  baggage_limit_kg INT DEFAULT 0,
  meals_included BOOLEAN DEFAULT false,
  CONSTRAINT fk_seatclass_inventory FOREIGN KEY (inventory_id) REFERENCES inventory(id)
);

CREATE TABLE bookings (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  inventory_id BIGINT NOT NULL,
  pnr VARCHAR(50) NOT NULL UNIQUE,
  booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  contact_email VARCHAR(255),
  contact_name VARCHAR(255),
  class_type VARCHAR(50),
  seats_booked INT,
  total_amount DECIMAL(10,2),
  status VARCHAR(50),
  payment_txn VARCHAR(255),
  travel_date DATE,
  CONSTRAINT fk_booking_inventory FOREIGN KEY (inventory_id) REFERENCES inventory(id)
);

CREATE TABLE passengers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_id BIGINT NOT NULL,
  name VARCHAR(255),
  gender VARCHAR(10),
  age INT,
  seat_number VARCHAR(20),
  meal VARCHAR(50),
  CONSTRAINT fk_passenger_booking FOREIGN KEY (booking_id) REFERENCES bookings(id)
);
