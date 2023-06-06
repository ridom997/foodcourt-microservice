-- HU -- 2
-- insert an owner, keep the id to use it in the endpoint "create Restaurant" hu-2
INSERT INTO `user` ( id,address, dni_number, id_dni_type, id_person_type, mail, name, password, phone, surname, token_password, id_role) VALUES( 100,'string', '7979799', 'string', 'string', 'corr@e.o', 'string', '$2a$10$GlsGSNhkbVon6ZOSNMptOu5RikedRzlCAhMa7YpwvUSS0c88WT99S', '210852442', 'string', NULL, 3);


-- HU -- 3
-- Execute this so we can create dishes using the endpoint "/dishes", in the request body: idOwnerRestaurant = 100, idCategory = 100 and idRestaurant = 100
ALTER TABLE powerup.restaurant ADD CONSTRAINT restaurant_FK FOREIGN KEY (id_owner) REFERENCES powerup.`user`(id);
INSERT INTO powerup.category (id,description, name) VALUES(100,'Mexican food', 'Mexican');
INSERT INTO powerup.restaurant (id,address, id_owner, name, nit, phone, url_logo) VALUES(100,'Unicentro CC', 100, 'Food Pragmatica', '123321', '333333333', 'https://www.pragma.com.co/hubfs/pragma_theme2021/images/Header%20Logo.svg');

-- HU -- 4
-- Add this dish so we can edit it using the endpoint "/dishes/{id}" (id = 100), in the request body: idOwnerRestaurant = 100)
INSERT INTO powerup.dish (id, active, description, name, price, url_image, id_category, id_restaurant) VALUES(100, 1, 'Arroz, frijoles y carne', 'Tipico mexicano 1', 11500, 'https://cdn0.recetasgratis.net/es/posts/1/4/0/arroz_con_carne_molida_74041_600.webp', 100, 100);

--HU --10
INSERT INTO category (id, description, name) VALUES(101, 'Colombian Food', 'Colombian');
INSERT INTO category (id, description, name) VALUES(102, 'Italian Food', 'Italian');
INSERT INTO category (id, description, name) VALUES(103, 'Fishes and sea food.', 'Fishes');
INSERT INTO powerup.dish (active,description,name,price,url_image,id_category,id_restaurant) VALUES
	 (1,'Chicharron, frisoles, arroz y platano.','Bandeja paisa',32000,'https://cdn.colombia.com/gastronomia/2011/08/02/bandeja-paisa-1616.gif',101,100);

-- HU --11
ALTER TABLE powerup.`order` MODIFY date DATETIME;