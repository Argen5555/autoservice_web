-- Создание таблицы ролей, если она не существует
CREATE TABLE IF NOT EXISTS roles (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     name VARCHAR(50) NOT NULL UNIQUE
    );

-- Вставка ролей, если их еще нет
INSERT OR IGNORE INTO roles (name) VALUES ('ROLE_USER');
INSERT OR IGNORE INTO roles (name) VALUES ('ROLE_ADMIN');

-- Вставка администратора, если его еще нет
-- Пароль: admin123 (закодирован с помощью BCrypt)
INSERT OR IGNORE INTO users (username, password, full_name, email, phone)
VALUES ('admin', '$2a$10$yfq/bqx2qE8eC2A4RY/W6.VBB5yBB3e8rJJn7A5YoO92HnlxQJ9xK', 'Администратор', 'admin@example.com', '+79001234567');

-- Вставка роли администратора
INSERT OR IGNORE INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

-- Вставка категорий услуг
INSERT OR IGNORE INTO service_categories (name, description)
VALUES ('Диагностика', 'Диагностика и осмотр автомобиля');

INSERT OR IGNORE INTO service_categories (name, description)
VALUES ('Техническое обслуживание', 'Плановое ТО автомобиля');

INSERT OR IGNORE INTO service_categories (name, description)
VALUES ('Ремонт двигателя', 'Ремонт и обслуживание двигателя');

INSERT OR IGNORE INTO service_categories (name, description)
VALUES ('Кузовной ремонт', 'Ремонт кузова и покраска');

INSERT OR IGNORE INTO service_categories (name, description)
VALUES ('Шиномонтаж', 'Замена и ремонт шин');

-- Вставка услуг
INSERT OR IGNORE INTO services (name, description, price, duration, category, featured)
VALUES ('Компьютерная диагностика', 'Полная диагностика всех систем автомобиля с помощью компьютера', 1500.00, '30-60 мин', 'Диагностика', 1);

INSERT OR IGNORE INTO services (name, description, price, duration, category, featured)
VALUES ('Замена масла и фильтра', 'Замена моторного масла и масляного фильтра', 1000.00, '30-45 мин', 'Техническое обслуживание', 1);

INSERT OR IGNORE INTO services (name, description, price, duration, category, featured)
VALUES ('Замена тормозных колодок', 'Замена передних или задних тормозных колодок', 1800.00, '45-60 мин', 'Техническое обслуживание', 0);

INSERT OR IGNORE INTO services (name, description, price, duration, category, featured)
VALUES ('Капитальный ремонт двигателя', 'Полный разбор и ремонт двигателя с заменой необходимых деталей', 45000.00, '3-5 дней', 'Ремонт двигателя', 0);

INSERT OR IGNORE INTO services (name, description, price, duration, category, featured)
VALUES ('Покраска элемента кузова', 'Покраска отдельного элемента кузова с подбором цвета', 5000.00, '1-2 дня', 'Кузовной ремонт', 0);

INSERT OR IGNORE INTO services (name, description, price, duration, category, featured)
VALUES ('Сезонная замена шин', 'Снятие, установка и балансировка 4 колес', 2000.00, '30-60 мин', 'Шиномонтаж', 1);