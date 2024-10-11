CREATE TABLE IF NOT EXISTS products
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    description      VARCHAR(255),
    quantity     INT          NOT NULL,
    unitary_value NUMERIC      NOT NULL
);

CREATE TABLE IF NOT EXISTS sales
(
    id          SERIAL PRIMARY KEY,
    client     VARCHAR(255)   NOT NULL,
    total_value NUMERIC(10, 2) NOT NULL,
    quantity     INT          NOT NULL
);


CREATE TABLE IF NOT EXISTS product_sales
(
    id         SERIAL PRIMARY KEY,
    sale_id    INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);
