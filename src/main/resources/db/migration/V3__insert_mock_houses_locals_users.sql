INSERT INTO locals (street, number, postal, name, description, type) VALUES
('Rua das Flores', 123, '12345678', 'Casa Azul', 'Uma bela casa azul de esquina.', 'Residencial'),
('Avenida Central', 456, '87654321', 'Prédio Comercial', 'Um prédio com escritórios modernos.', 'Comercial');

INSERT INTO houses (local_id, description) VALUES
(1, 'Uma casa espaçosa com quintal amplo.'),
(1, 'Uma casa pouca espaçoca.'),
(2, 'Apartamento moderno no centro da cidade.');

INSERT INTO users (name, email, password, house_id) VALUES
('João Silva', 'joao@email.com', 'senha123', 1),
('Maria Souza', 'maria@email.com', 'senha456', 2),
('Miguel Luiz', 'miguel@email.com', 'senha123', 3);
