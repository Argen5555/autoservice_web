-- Создание ролей
MERGE INTO roles (id, name) KEY(name) VALUES (1, 'ROLE_USER');
MERGE INTO roles (id, name) KEY(name) VALUES (2, 'ROLE_ADMIN');

-- Создание администратора (пароль: admin123)
MERGE INTO users (id, username, password, full_name, email, phone)
    KEY(username)
    VALUES (1, 'admin', '$2a$10$yfq/bqx2qE8eC2A4RY/W6.VBB5yBB3e8rJJn7A5YoO92HnlxQJ9xK', 'Администратор', 'admin@example.com', '+79001234567');

-- Связывание пользователя с ролью администратора
MERGE INTO user_roles (user_id, role_id)
    KEY(user_id, role_id)
    VALUES (1, 2);

-- Вставка услуг
MERGE INTO services (id, name, description, price, duration, category, featured)
    KEY(name)
    VALUES (1, 'Компьютерная диагностика', 'Полная диагностика всех систем автомобиля с помощью компьютера', 1500.00, '30-60 мин', 'Диагностика', TRUE);

MERGE INTO services (id, name, description, price, duration, category, featured)
    KEY(name)
    VALUES (2, 'Замена масла и фильтра', 'Замена моторного масла и масляного фильтра', 1000.00, '30-45 мин', 'Техническое обслуживание', TRUE);

MERGE INTO services (id, name, description, price, duration, category, featured)
    KEY(name)
    VALUES (3, 'Замена тормозных колодок', 'Замена передних или задних тормозных колодок', 1800.00, '45-60 мин', 'Техническое обслуживание', FALSE);

MERGE INTO services (id, name, description, price, duration, category, featured)
    KEY(name)
    VALUES (4, 'Капитальный ремонт двигателя', 'Полный разбор и ремонт двигателя с заменой необходимых деталей', 45000.00, '3-5 дней', 'Ремонт двигателя', FALSE);

MERGE INTO services (id, name, description, price, duration, category, featured)
    KEY(name)
    VALUES (5, 'Покраска элемента кузова', 'Покраска отдельного элемента кузова с подбором цвета', 5000.00, '1-2 дня', 'Кузовной ремонт', FALSE);

MERGE INTO services (id, name, description, price, duration, category, featured)
    KEY(name)
    VALUES (6, 'Сезонная замена шин', 'Снятие, установка и балансировка 4 колес', 2000.00, '30-60 мин', 'Шиномонтаж', TRUE);