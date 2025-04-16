-- Инициализация данных для PostgreSQL

-- Роли пользователей
INSERT INTO roles (name) VALUES ('ROLE_USER') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO NOTHING;

-- Администратор (пароль: admin123)
INSERT INTO users (username, password, full_name, email, phone)
VALUES ('admin', '$2a$10$yfq/bqx2qE8eC2A4RY/W6.VBB5yBB3e8rJJn7A5YoO92HnlxQJ9xK', 'Администратор', 'admin@example.com', '+79001234567')
    ON CONFLICT (username) DO NOTHING;

-- Тестовый пользователь (пароль: password123)
INSERT INTO users (username, password, full_name, email, phone)
VALUES ('user', '$2a$10$ZNoMOjd8Pn9mFhJBMQnwleVjMd3BD2XgB3z3FaJ.SZJUkzQVnWpFS', 'Тестовый Пользователь', 'user@example.com', '+79002223344')
    ON CONFLICT (username) DO NOTHING;

-- Связывание пользователей с ролями
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
    ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'user' AND r.name = 'ROLE_USER'
    ON CONFLICT (user_id, role_id) DO NOTHING;

-- Услуги автосервиса
INSERT INTO services (name, description, price, duration, category, featured)
VALUES ('Компьютерная диагностика', 'Полная диагностика всех систем автомобиля', 1500.00, '30-60 мин', 'Диагностика', true)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO services (name, description, price, duration, category, featured)
VALUES ('Замена масла', 'Замена масла и масляного фильтра', 1000.00, '30 мин', 'Техническое обслуживание', true)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO services (name, description, price, duration, category, featured)
VALUES ('Шиномонтаж', 'Замена колес, балансировка', 2000.00, '40 мин', 'Шиномонтаж', true)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO services (name, description, price, duration, category, featured)
VALUES ('Кузовной ремонт', 'Устранение вмятин, покраска', 5000.00, '1-2 дня', 'Кузовные работы', false)
    ON CONFLICT (id) DO NOTHING;