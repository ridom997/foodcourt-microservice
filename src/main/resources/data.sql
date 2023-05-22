-- HU -- 2
-- insert an owner, keep the id to use it in the endpoint "create Restaurant" hu-2
INSERT INTO `user` ( id,address, dni_number, id_dni_type, id_person_type, mail, name, password, phone, surname, token_password, id_role) VALUES( 100,'string', '7979799', 'string', 'string', 'corr@e.o', 'string', '$2a$10$VKq4On5a/bqUjWU8lh5d3OXCQyCfx1vcBPkqrpR/Bvz65uCVkAsw6', '210852442', 'string', NULL, 3);


-- HU -- 3
-- Execute this so we can create dishes using the endpoint "/dishes", in the request body: idOwnerRestaurant = 100, idCategory = 100 and idRestaurant = 100
ALTER TABLE powerup.restaurant ADD CONSTRAINT restaurant_FK FOREIGN KEY (id_owner) REFERENCES powerup.`user`(id);
INSERT INTO powerup.category (id,description, name) VALUES(100,'Mexican food', 'Mexican');
INSERT INTO powerup.restaurant (id,address, id_owner, name, nit, phone, url_logo) VALUES(100,'Unicentro CC', 100, 'Food Pragmatica', '123321', '333333333', 'https://www.pragma.com.co/hubfs/pragma_theme2021/images/Header%20Logo.svg');

