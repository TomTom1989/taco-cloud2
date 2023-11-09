-- Create the Ingredient table
CREATE TABLE IF NOT EXISTS Ingredient (
    id varchar(4) not null,
    name varchar(25) not null,
    type TINYINT not null,
    PRIMARY KEY (id)  -- Add a primary key constraint
);

-- Create the Taco_Order table
CREATE TABLE IF NOT EXISTS Taco_Order (
    id identity,
    delivery_Name varchar(50) not null,
    delivery_Street varchar(50) not null,
    delivery_City varchar(50) not null,
    delivery_State varchar(255) not null,
    delivery_Zip varchar(10) not null,
    cc_number varchar(16) not null,
    cc_expiration varchar(5) not null,
    cc_cvv varchar(3) not null,
    placed_at timestamp not null,
    PRIMARY KEY (id)  -- Add a primary key constraint
);

-- Create the Taco table
CREATE TABLE IF NOT EXISTS Taco (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    taco_order BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (taco_order) REFERENCES Taco_Order(id)
);

-- Create the Ingredient_Ref table
CREATE TABLE IF NOT EXISTS Ingredient_Ref (
    ingredient VARCHAR(4) NOT NULL,
    taco BIGINT NOT NULL,
    FOREIGN KEY (ingredient) REFERENCES Ingredient(id),
    PRIMARY KEY (taco, ingredient)
);

-- Add foreign key constraints after all tables have been created
ALTER TABLE Taco
ADD FOREIGN KEY (taco_order) REFERENCES Taco_Order(id);

ALTER TABLE Ingredient_Ref
ADD FOREIGN KEY (ingredient) REFERENCES Ingredient(id);
