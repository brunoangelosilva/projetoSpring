CREATE TABLE pessoa (
 codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
 nome VARCHAR(50) NOT NULL,
 bairro VARCHAR(50),
 cep   VARCHAR(12),
 cidade VARCHAR(50),
 complemento VARCHAR(30),
 estado VARCHAR(30),
 logradouro VARCHAR(50),
 numero VARCHAR(10),
 ativo Boolean NOT NULL
) ENGINE=innoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa (nome, bairro, cep, cidade, complemento, estado, logradouro, numero, ativo) values('João de Deus','Iputinga', '50610520', 'Recife', 'rua sem saída', 'PE', 'Rua dos Aliados', '122',true);
INSERT INTO pessoa (nome, bairro, cep, cidade, complemento, estado, logradouro, numero, ativo) values('Maria José','Iputinga', '50731490', 'Recife', 'rua sem saída', 'PE', 'Avenida Mauricio de nassau', '42',true);
INSERT INTO pessoa (nome, ativo) values('Fulano de Tal', true);
INSERT INTO pessoa (nome, ativo) values('Beltrano de não sei o que', true);
INSERT INTO pessoa (nome, ativo) values('Zezinho da Esquina', true);
INSERT INTO pessoa (nome ,bairro, cep, cidade, complemento, estado, logradouro, numero, ativo) values('Naruto Shippuden','Konoha', '55533320', 'Recife', 'rua sem saída', 'Vila da Folha', 'Avenida Mauricio de nassau', '42',true);
INSERT INTO pessoa (nome, bairro, cep, cidade, complemento, estado, logradouro, numero, ativo) values('Uzumaki boruto' ,'Konoha', '55533320', 'Recife', 'rua sem saída', 'Vila da Folha', 'Avenida Mauricio de nassau', '42',true);
INSERT INTO pessoa (nome, bairro, cep, cidade, complemento, estado, logradouro, numero, ativo) values('Ushira sasuke' ,'Konoha', '55533320', 'Recife', 'rua sem saída', 'Vila da Folha', 'Avenida Mauricio de nassau', '42',true);
INSERT INTO pessoa (nome, bairro, cep, cidade, complemento, estado, logradouro, numero, ativo) values('Ushira Sarada' ,'Konoha', '55533320', 'Recife', 'rua sem saída', 'Vila da Folha', 'Avenida Mauricio de nassau', '42',true);
INSERT INTO pessoa (nome, bairro, cep, cidade, complemento, estado, logradouro, numero, ativo) values('Jyraya sensei' ,'Konoha', '55533320', 'Recife', 'rua sem saída', 'Vila da Folha', 'Avenida Mauricio de nassau', '42',true);
INSERT INTO pessoa (nome, bairro, cep, cidade, complemento, estado, logradouro, numero, ativo) values('Kagashi sensei' ,'Konoha', '55533320', 'Recife', 'rua sem saída', 'Vila da Folha', 'Avenida Mauricio de nassau', '42',true);