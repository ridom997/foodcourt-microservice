-- HU -- 2
-- insert an owner, keep the id to use it in the endpoint "create Restaurant" hu-2
INSERT INTO `user` ( address, dni_number, id_dni_type, id_person_type, mail, name, password, phone, surname, token_password, id_role) VALUES( 'string', '333', 'string', 'string', 'strin@w.g', 'string', '$2a$10$VKq4On5a/bqUjWU8lh5d3OXCQyCfx1vcBPkqrpR/Bvz65uCVkAsw6', '210852442', 'string', NULL, 3);


-- HU -- 3
ALTER TABLE powerup.restaurant ADD CONSTRAINT restaurant_FK FOREIGN KEY (id_owner) REFERENCES powerup.`user`(id);
