DELETE
FROM user_roles;
DELETE
FROM votes;
DELETE
FROM users;
DELETE
FROM restaurants;
DELETE
FROM dishes;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO restaurants (name)
VALUES ('Aragawa'),
       ('Erarta'),
       ('KFC'),
       ('Masa');

INSERT INTO votes (user_id, restaurant_id, date)
VALUES (100000, 100002, '2020-12-10'),
       (100000, 100002, '2020-12-11'),
       (100000, 100004, '2020-12-12'),
       (100001, 100005, '2020-12-10'),
       (100001, 100003, '2020-12-11'),
       (100001, 100005, '2020-12-12'),
       (100001, 100002, '2020-12-13');

INSERT INTO dishes (restaurant_id, name, price, date)
VALUES (100002, 'Salmon tartare with daikon', 660, '2020-12-10'),
       (100002, 'Tuna tataki', 590, '2020-12-10'),
       (100002, 'Salmon salad with tangerines', 780, '2020-12-10'),
       (100002, 'Duck leg confit', 570, '2020-12-11'),
       (100002, 'Roast beef salad', 680, '2020-12-11'),
       (100002, 'Chicken breast with spinach', 580, '2020-12-11'),
       (100002, 'Asparagus on carrot puree', 650, '2020-12-12'),
       (100002, 'Belgian Waffles', 375, '2020-12-12'),
       (100003, 'California Roll', 1000, '2020-12-10'),
       (100003, 'Philly roll', 1200, '2020-12-10'),
       (100003, 'Combo box', 700, '2020-12-10'),
       (100003, 'California Roll', 1000, '2020-12-11'),
       (100003, 'Philly roll', 1200, '2020-12-11'),
       (100003, 'Combo box', 700, '2020-12-11'),
       (100003, 'California Roll', 1000, '2020-12-12'),
       (100003, 'Philly roll', 1200, '2020-12-12'),
       (100003, 'Combo box', 700, '2020-12-12'),
       (100003, 'California Roll', 1000, '2020-12-13'),
       (100003, 'Philly roll', 1200, '2020-12-13'),
       (100003, 'Combo box', 700, '2020-12-13'),
       (100004, 'Double burger', 290, '2020-12-10'),
       (100004, 'Double burger', 290, '2020-12-11'),
       (100004, 'Double burger', 290, '2020-12-12'),
       (100004, 'Double burger', 290, '2020-12-13');