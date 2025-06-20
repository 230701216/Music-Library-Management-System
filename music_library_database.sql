CREATE DATABASE IF NOT EXISTS musiclibrarydb;

USE musiclibrarydb;

CREATE TABLE IF NOT EXISTS Songs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    artist VARCHAR(255) NOT NULL,
    album VARCHAR(255),
    genre VARCHAR(100)
);
