-- ============================================
-- TABLA: gimnasios
-- ============================================
CREATE TABLE gimnasios (
    id_Gimnasio SERIAL PRIMARY KEY,
    gimnasio VARCHAR(50) NOT NULL
);

-- ============================================
-- TABLA: clientes
-- ============================================
CREATE TABLE clientes (
    id_Cliente SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    telefono VARCHAR(10) UNIQUE,
    fecha_Nacimiento DATE NOT NULL,
    condiciones_Medicas BOOLEAN NOT NULL,
    fecha_Registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fotografia VARCHAR(255) NOT NULL DEFAULT 'assets/images/avatars/01.png',
    foto_Credencial VARCHAR(255) NOT NULL,
    id_Gimnasio INT NOT NULL,
    numero_Verificado BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_cliente_gimnasio FOREIGN KEY (id_Gimnasio) REFERENCES gimnasios (id_Gimnasio)
);

-- ============================================
-- TABLA: contactos_emergencia
-- ============================================
CREATE TABLE contactos_emergencia (
    id_Contacto SERIAL PRIMARY KEY,
    nombre_Contacto VARCHAR(255) NOT NULL,
    telefono_Contacto VARCHAR(10) NOT NULL,
    id_Cliente INT NOT NULL UNIQUE,
    CONSTRAINT fk_contacto_cliente FOREIGN KEY (id_Cliente) REFERENCES clientes (id_Cliente)
);

-- ============================================
-- TABLA: roles
-- ============================================
CREATE TABLE roles (
    id_Rol SERIAL PRIMARY KEY,
    rol VARCHAR(50) NOT NULL,
    estado INT NOT NULL
);

-- ============================================
-- TABLA: usuarios
-- ============================================
CREATE TABLE usuarios (
    id_Usuario SERIAL PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL,
    correo VARCHAR(255) NOT NULL,
    telefono VARCHAR(10) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    id_Rol INT NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    estado INT NOT NULL,
    id_Gimnasio INT NOT NULL,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (id_Rol) REFERENCES roles (id_Rol),
    CONSTRAINT fk_usuario_gimnasio FOREIGN KEY (id_Gimnasio) REFERENCES gimnasios (id_Gimnasio)
);

-- ============================================
-- TABLA: productos
-- ============================================
CREATE TABLE productos (
    id_Producto SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio NUMERIC(10,2) NOT NULL,
    stock INT NOT NULL,
    estado INT NOT NULL,
    foto VARCHAR(255) NOT NULL
);

-- ============================================
-- TABLA: tickets
-- ============================================
CREATE TABLE tickets (
    id_Ticket SERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    id_Cliente INT,
    total NUMERIC(10,2) NOT NULL,
    estado INT NOT NULL,
    fecha_Venta TIMESTAMP,
    forma_Pago INT,
    pago_Con NUMERIC(10,2),
    id_Usuario INT NOT NULL,
    CONSTRAINT fk_ticket_usuario FOREIGN KEY (id_Usuario) REFERENCES usuarios (id_Usuario)
);

-- ============================================
-- TABLA: detalles_tickets
-- ============================================
CREATE TABLE detalles_tickets (
    id_Detalle SERIAL PRIMARY KEY,
    id_Ticket INT NOT NULL,
    id_Producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_Unitario NUMERIC(10,2) NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (id_Producto) REFERENCES productos (id_Producto) 
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_detalle_ticket FOREIGN KEY (id_Ticket) REFERENCES tickets (id_Ticket)
        ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ============================================
-- TABLA: membresias
-- ============================================
CREATE TABLE membresias (
    id_Membresia SERIAL PRIMARY KEY,
    membresia VARCHAR(50) NOT NULL,
    duracion VARCHAR(4) NOT NULL,
    precio DOUBLE PRECISION NOT NULL,
    estado INT NOT NULL,
    id_Gimnasio INT NOT NULL,
    CONSTRAINT fk_membresia_gimnasio FOREIGN KEY (id_Gimnasio) REFERENCES gimnasios (id_Gimnasio)
);

-- ============================================
-- TABLA: membresias_clientes (Muchos a muchos)
-- ============================================
CREATE TABLE membresias_clientes (
    id_Cliente INT NOT NULL,
    id_Membresia INT NOT NULL,
    fecha_Inicio DATE,
    fecha_Termino DATE,
    miembro_Desde TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado_Membresia BOOLEAN NOT NULL DEFAULT TRUE,
    id_Gimnasio INT NOT NULL,
    PRIMARY KEY (id_Cliente, id_Membresia),
    CONSTRAINT fk_mc_cliente FOREIGN KEY (id_Cliente) REFERENCES clientes (id_Cliente),
    CONSTRAINT fk_mc_membresia FOREIGN KEY (id_Membresia) REFERENCES membresias (id_Membresia),
    CONSTRAINT fk_mc_gimnasio FOREIGN KEY (id_Gimnasio) REFERENCES gimnasios (id_Gimnasio)
);

-- ============================================
-- TABLA: ventas_membresias
-- ============================================
CREATE TABLE ventas_membresias (
    id_Venta_Membresia SERIAL PRIMARY KEY,
    id_Cliente INT,
    id_Membresia INT NOT NULL,
    fecha TIMESTAMP NOT NULL,
    pago DOUBLE PRECISION NOT NULL,
    cancelado BOOLEAN NOT NULL DEFAULT FALSE,
    id_Gimnasio INT NOT NULL,
    id_Usuario INT NOT NULL,
    fecha_Inicio DATE NOT NULL,
    fecha_Termino DATE NOT NULL,
    CONSTRAINT fk_vm_cliente FOREIGN KEY (id_Cliente) REFERENCES clientes (id_Cliente),
    CONSTRAINT fk_vm_membresia FOREIGN KEY (id_Membresia) REFERENCES membresias (id_Membresia),
    CONSTRAINT fk_vm_gimnasio FOREIGN KEY (id_Gimnasio) REFERENCES gimnasios (id_Gimnasio),
    CONSTRAINT fk_vm_usuario FOREIGN KEY (id_Usuario) REFERENCES usuarios (id_Usuario)
);

-- ============================================
-- TABLA: visitas
-- ============================================
CREATE TABLE visitas (
    id_Visita SERIAL PRIMARY KEY,
    id_Cliente INT,
    fecha TIMESTAMP NOT NULL,
    id_Gimnasio INT NOT NULL,
    pendiente INT,
    CONSTRAINT fk_visita_gimnasio FOREIGN KEY (id_Gimnasio) REFERENCES gimnasios (id_Gimnasio)
);
