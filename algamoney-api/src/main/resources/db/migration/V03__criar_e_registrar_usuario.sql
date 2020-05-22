CREATE TABLE usuario (
 codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
 nome VARCHAR(50) NOT NULL,
 email VARCHAR(50)  NOT NULL ,
 senha  VARCHAR(50) NOT NULL

) ENGINE=innoDB DEFAULT CHARSET=utf8;

INSERT INTO usuario (nome, email, senha) values('João de Deus','joao@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('José de Deus','jose@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('Maria de Deus','maria@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('Antonio de Deus','antonio@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('Vitor de Deus','vitor@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('Higor de Deus','higor@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('Rodrigo de Deus','rodrigo@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('Matheus de Deus','matheus@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('Andrey de Deus','andrey@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('Flavio de Deus','flavio@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('Shirley de Deus','shirley@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('Saladino de Deus','saladino@gmail.com', '123456');
INSERT INTO usuario (nome, email, senha) values('Itamar de Deus','itamar@gmail.com', '123456');