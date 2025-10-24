-- ============================================
-- TABLA: gimnasios
-- ============================================
CREATE TABLE gyms (
    id_gym SERIAL PRIMARY KEY,
    gym VARCHAR(50) NOT NULL
);

-- ============================================
-- TABLA: clientes
-- ============================================
CREATE TABLE customers (
    id_customer SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cologne VARCHAR(150) NOT NULL,
    phone VARCHAR(10) UNIQUE,
    birth_date DATE NOT NULL,
    medical_condition BOOLEAN NOT NULL,
    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    photo VARCHAR(255) NOT NULL DEFAULT,
    photo_credential VARCHAR(255) NOT NULL,
    verified_number BOOLEAN NOT NULL DEFAULT FALSE,
    id_gym INT NOT NULL,
    CONSTRAINT fk_customer_gym FOREIGN KEY (id_gym) REFERENCES gyms (id_gym)
);

-- ============================================
-- TABLA: contactos_emergencia
-- ============================================
CREATE TABLE emergencys_contacts (
    id_contact SERIAL PRIMARY KEY,
    contact_name VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(10) NOT NULL,
    id_customer INT NOT NULL UNIQUE,
    CONSTRAINT fk_contact_customer FOREIGN KEY (id_customer) REFERENCES customers (id_customer)
);

-- ============================================
-- TABLA: roles
-- ============================================
CREATE TABLE roles (
    id_role SERIAL PRIMARY KEY,
    role VARCHAR(50) NOT NULL,
    status INT NOT NULL
);

-- ============================================
-- TABLA: usuarios
-- ============================================
CREATE TABLE users (
    id_user SERIAL PRIMARY KEY,
    user VARCHAR(50) NOT NULL,
    mail VARCHAR(255) NOT NULL,
    phone VARCHAR(10) NOT NULL,
    name VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status INT NOT NULL,
    id_role INT NOT NULL,
    id_gym INT NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (id_role) REFERENCES roles (id_role),
    CONSTRAINT fk_user_gym FOREIGN KEY (id_gym) REFERENCES gyms (id_gym)
);

-- ============================================
-- TABLA: productos
-- ============================================
CREATE TABLE products (
    id_product SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    stock INT NOT NULL,
    status INT NOT NULL,
    photo VARCHAR(255) NOT NULL
);

-- ============================================
-- TABLA: tickets
-- ============================================
CREATE TABLE tickets (
    id_ticket SERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    total NUMERIC(10,2) NOT NULL,
    status INT NOT NULL,
    sale_date TIMESTAMP,
    method_payment INT,
    payment_with NUMERIC(10,2),
    id_customer INT,
    id_user INT NOT NULL,
    CONSTRAINT fk_ticket_user FOREIGN KEY (id_user) REFERENCES users (id_user)
    CONSTRAINT fk_ticket_customer FOREIGN KEY (id_customer) REFERENCES customers (id_customer)
);

-- ============================================
-- TABLA: detalles_tickets
-- ============================================
CREATE TABLE tickets_details (
    id_datail_ticket SERIAL PRIMARY KEY,
    amount INT NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL,
    id_product INT NOT NULL,
    id_ticket INT NOT NULL,
    CONSTRAINT fk_detail_producto FOREIGN KEY (id_product) REFERENCES products (id_product) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_detail_ticket FOREIGN KEY (id_ticket) REFERENCES tickets (id_ticket) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ============================================
-- TABLA: membresias
-- ============================================
CREATE TABLE memberships (
    id_membership SERIAL PRIMARY KEY,
    membership VARCHAR(50) NOT NULL,
    duration VARCHAR(4) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    status INT NOT NULL,
    id_gym INT NOT NULL,
    CONSTRAINT fk_membership_gym FOREIGN KEY (id_gym) REFERENCES gyms (id_gym)
);

-- ============================================
-- TABLA: membresias_clientes (Muchos a muchos)
-- ============================================
CREATE TABLE customers_memberships (
    start_date DATE,
    end_date DATE,
    member_since TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    membership_status BOOLEAN NOT NULL DEFAULT TRUE,
    id_customer INT NOT NULL,
    id_membership INT NOT NULL,
    id_gym INT NOT NULL,
    PRIMARY KEY (id_customer, id_membership),
    CONSTRAINT fk_cm_customer FOREIGN KEY (id_customer) REFERENCES customers (id_customer),
    CONSTRAINT fk_cm_membership FOREIGN KEY (id_membership) REFERENCES memberships (id_membership),
    CONSTRAINT fk_cm_gym FOREIGN KEY (id_gym) REFERENCES gyms (id_gym)
);

-- ============================================
-- TABLA: ventas_membresias
-- ============================================
CREATE TABLE memberships_sales (
    id_membership_sale SERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    payment DOUBLE PRECISION NOT NULL,
    cancellation BOOLEAN NOT NULL DEFAULT FALSE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    id_customer INT,
    id_membership INT NOT NULL,
    id_gym INT NOT NULL,
    id_user INT NOT NULL,
    CONSTRAINT fk_ms_customer FOREIGN KEY (id_customer) REFERENCES customers (id_customer),
    CONSTRAINT fk_ms_membership FOREIGN KEY (id_membership) REFERENCES memberships (id_membership),
    CONSTRAINT fk_ms_gym FOREIGN KEY (id_gym) REFERENCES gyms (id_gym),
    CONSTRAINT fk_ms_user FOREIGN KEY (id_user) REFERENCES users (id_user)
);

-- ============================================
-- TABLA: visitas
-- ============================================
CREATE TABLE visits (
    id_visit SERIAL PRIMARY KEY,
    id_customer INT,
    date TIMESTAMP NOT NULL,
    pending INT,
    id_gym INT NOT NULL,
    CONSTRAINT fk_visit_gym FOREIGN KEY (id_gym) REFERENCES gyms (id_gym)
    CONSTRAINT fk_visit_customer FOREIGN KEY (id_customer) REFERENCES customers (id_customer)
);