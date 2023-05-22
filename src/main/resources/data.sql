-- HU -- 2
-- insert an owner, keep the id to use it in the endpoint "create Restaurant" hu-2
INSERT INTO `user` ( address, dni_number, id_dni_type, id_person_type, mail, name, password, phone, surname, token_password, id_role) VALUES( 'string', '333', 'string', 'string', 'strin@w.g', 'string', '$2a$10$VKq4On5a/bqUjWU8lh5d3OXCQyCfx1vcBPkqrpR/Bvz65uCVkAsw6', '210852442', 'string', NULL, 3);


-- HU -- 3
ALTER TABLE powerup.restaurant ADD CONSTRAINT restaurant_FK FOREIGN KEY (id_owner) REFERENCES powerup.`user`(id);






-- HU -- 4
-- Add this dish so we can edit it using the endpoint "/dishes/{id}" (id = 100), in the request body: idOwnerRestaurant = 100)
INSERT INTO powerup.dish (id, active, description, name, price, url_image, id_category, id_restaurant) VALUES(100, 1, 'Arroz, frijoles y carne', 'Tipico mexicano 1', 11500, 'https://cdn0.recetasgratis.net/es/posts/1/4/0/arroz_con_carne_molida_74041_600.webp', 100, 100);
