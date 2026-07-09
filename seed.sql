-- =============================================================================
-- TravelMatch – Script de seed de base de datos
-- Ejecutar una vez sobre la BD vacía después de que Hibernate haya creado
-- el esquema (ddl-auto=update / create).
-- Idempotente: usa ON CONFLICT DO NOTHING / WHERE NOT EXISTS.
-- =============================================================================

-- ─────────────────────────────────────────────────────────────────────────────
-- 1. ROLES  (tabla: roles)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT INTO roles (name) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_AGENCY_STAFF'),
    ('ROLE_TOURIST')
ON CONFLICT (name) DO NOTHING;

-- ─────────────────────────────────────────────────────────────────────────────
-- 2. CATEGORÍAS  (tabla: categories)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT INTO categories (name) VALUES
    ('CULTURA'),
    ('GASTRONOMIA'),
    ('NATURALEZA'),
    ('DEPORTE')
ON CONFLICT (name) DO NOTHING;

-- ─────────────────────────────────────────────────────────────────────────────
-- 3. TIPOS DE TICKET  (tabla: ticket_types)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT INTO ticket_types (name) VALUES
    ('TICKET_GENERAL'),
    ('TICKET_VIP')
ON CONFLICT (name) DO NOTHING;

-- ─────────────────────────────────────────────────────────────────────────────
-- 4. DESTINOS  (tabla: destinations)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT INTO destinations (name, address, district, city, state, country, created_at, updated_at)
SELECT 'Cusco', 'Plaza de Armas s/n', 'Cusco', 'Cusco', 'Cusco', 'Peru', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE name = 'Cusco');

INSERT INTO destinations (name, address, district, city, state, country, created_at, updated_at)
SELECT 'Lima Centro', 'Jr. de la Union 300', 'Cercado de Lima', 'Lima', 'Lima', 'Peru', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE name = 'Lima Centro');

INSERT INTO destinations (name, address, district, city, state, country, created_at, updated_at)
SELECT 'Arequipa', 'Calle Santa Catalina 210', 'Cercado', 'Arequipa', 'Arequipa', 'Peru', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE name = 'Arequipa');

-- ─────────────────────────────────────────────────────────────────────────────
-- 5. EXPERIENCIAS + DISPONIBILIDAD + TICKET TYPES  (bloque PL/pgSQL)
-- ─────────────────────────────────────────────────────────────────────────────
DO $$
DECLARE
    v_cultura     BIGINT := (SELECT id FROM categories WHERE name = 'CULTURA');
    v_gastronomia BIGINT := (SELECT id FROM categories WHERE name = 'GASTRONOMIA');
    v_naturaleza  BIGINT := (SELECT id FROM categories WHERE name = 'NATURALEZA');
    v_deporte     BIGINT := (SELECT id FROM categories WHERE name = 'DEPORTE');
    v_cusco       BIGINT := (SELECT id FROM destinations WHERE name = 'Cusco');
    v_lima        BIGINT := (SELECT id FROM destinations WHERE name = 'Lima Centro');
    v_arequipa    BIGINT := (SELECT id FROM destinations WHERE name = 'Arequipa');
    v_gen         BIGINT := (SELECT id FROM ticket_types WHERE name = 'TICKET_GENERAL');
    v_vip         BIGINT := (SELECT id FROM ticket_types WHERE name = 'TICKET_VIP');
    v_exp_id      BIGINT;
    v_avail_id    BIGINT;
BEGIN

-- 1. Machu Picchu Full Day
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Machu Picchu Full Day' AND destination_id = v_cusco) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Machu Picchu Full Day',
            'Embark on an unforgettable journey to one of the Seven Wonders of the World. ' ||
            'Enjoy a scenic train ride through the Sacred Valley, followed by a fully guided tour ' ||
            'exploring the legendary Inca citadel ruins, temples, terraces, and panoramic viewpoints.',
            1, v_cultura, v_cusco, '10h', 'Cusco Main Square', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 180.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 270.00, 10);
END IF;

-- 2. Sacred Valley Explorer
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Sacred Valley Explorer' AND destination_id = v_cusco) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Sacred Valley Explorer',
            'Discover the ancient heart of the Inca Empire. Traverse breathtaking Andean valley landscapes, ' ||
            'visit the vibrant Pisac artisan market, marvel at the steep agricultural terraces, ' ||
            'and tour the impressive stone fortress at Ollantaytambo with our expert historian guide.',
            1, v_cultura, v_cusco, '8h', 'Hotel lobby pickup', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 95.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 142.50, 10);
END IF;

-- 3. Cusco City Walking Tour
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Cusco City Walking Tour' AND destination_id = v_cusco) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Cusco City Walking Tour',
            'Unravel the rich colonial and pre-Hispanic tapestry of Cusco. Wander through narrow cobblestone ' ||
            'streets, admire imperial Inca stone walls, and visit historic landmarks like the majestic temple ' ||
            'of Qorikancha and the bohemian neighborhood of San Blas.',
            1, v_cultura, v_cusco, '3h', 'Plaza de Armas fountain', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 35.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 52.50, 10);
END IF;

-- 4. Cusco Gastronomic Experience
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Cusco Gastronomic Experience' AND destination_id = v_cusco) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Cusco Gastronomic Experience',
            'Tempt your palate with the rich culinary heritage of the Andes. Learn to prepare classic Peruvian ' ||
            'dishes like fresh ceviche, savory lomo saltado, and shake up the perfect pisco sour cocktail ' ||
            'using organic ingredients sourced directly from San Pedro Market.',
            1, v_gastronomia, v_cusco, '4h', 'San Pedro Market entrance', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 65.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 97.50, 10);
END IF;

-- 5. Rainbow Mountain Trek
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Rainbow Mountain Trek' AND destination_id = v_cusco) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Rainbow Mountain Trek',
            'Test your limits with a high-altitude expedition to the stunning striped peaks of Vinicunca. ' ||
            'Hike along dramatic valleys, see herds of native alpacas, and stand in awe of the surreal ' ||
            'natural minerals that dye this sacred mountain in a rainbow of pastel colors.',
            1, v_deporte, v_cusco, '12h', 'Hotel lobby pickup', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 120.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 180.00, 10);
END IF;

-- 6. Humantay Lake Trek
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Humantay Lake Trek' AND destination_id = v_cusco) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Humantay Lake Trek',
            'Journey to a hidden turquoise gem tucked beneath the snow-capped Humantay Peak. ' ||
            'Trek through crisp alpine trails, breathe in the pure mountain air, and take iconic photos ' ||
            'of the crystal-clear glacial waters reflecting the dramatic Andean skies.',
            1, v_naturaleza, v_cusco, '10h', 'Cusco bus terminal', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 85.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 127.50, 10);
END IF;

-- 7. Lima Historic Center Tour
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Lima Historic Center Tour' AND destination_id = v_lima) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Lima Historic Center Tour',
            'Immerse yourself in the colonial grandeur of the ''City of the Kings''. Marvel at ornate wooden balconies, ' ||
            'walk beneath the cavernous stone arches of the Plaza Mayor, and descend into the fascinating ' ||
            'subterranean catacombs of San Francisco Basilica.',
            1, v_cultura, v_lima, '4h', 'Plaza San Martin', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 40.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 60.00, 10);
END IF;

-- 8. Miraflores Food Tour
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Miraflores Food Tour' AND destination_id = v_lima) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Miraflores Food Tour',
            'Savor the world''s most acclaimed culinary capital. Walk through beautiful seaside neighborhoods, ' ||
            'tasting succulent street food, gourmet amazon ingredients, fresh seafood delicacies, ' ||
            'and gourmet artisanal desserts from Lima''s trendiest award-winning bistros.',
            1, v_gastronomia, v_lima, '3h', 'Parque Kennedy', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 55.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 82.50, 10);
END IF;

-- 9. Lima Bike Tour
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Lima Bike Tour' AND destination_id = v_lima) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Lima Bike Tour',
            'Feel the cool ocean breeze as you pedal along the magnificent cliffs of the Costa Verde greenway. ' ||
            'Explore the beautiful parks of Miraflores and cruise into the bohemian streets of Barranco ' ||
            'to admire colorful street murals, bridges, and artistic architecture.',
            1, v_deporte, v_lima, '3h', 'Larcomar mall entrance', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 30.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 45.00, 10);
END IF;

-- 10. Pachacamac Ruins Tour
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Pachacamac Ruins Tour' AND destination_id = v_lima) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Pachacamac Ruins Tour',
            'Step back in time at this sacred pre-Inca pilgrimage complex overlooking the Pacific Ocean. ' ||
            'Explore massive mud-brick adobe temples, palaces, and pyramid structures built by the ancient ' ||
            'Ychsma and Wari cultures centuries before the Incas arrived.',
            1, v_cultura, v_lima, '5h', 'Miraflores park', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 50.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 75.00, 10);
END IF;

-- 11. Colca Canyon Trek
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Colca Canyon Trek' AND destination_id = v_arequipa) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Colca Canyon Trek',
            'Venture into one of the deepest river canyons in the world. Traverse dry high-altitude plains, ' ||
            'soak in therapeutic volcanic hot springs, and stand at the Cruz del Condor viewpoint ' ||
            'to witness majestic Andean condors soaring gracefully on thermal winds.',
            1, v_naturaleza, v_arequipa, '2 days', 'Arequipa main plaza', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 150.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 225.00, 10);
END IF;

-- 12. Arequipa City Tour
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Arequipa City Tour' AND destination_id = v_arequipa) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Arequipa City Tour',
            'Stroll through the sparkling ''White City'', constructed from pearlescent volcanic sillar stone. ' ||
            'Explore the vast, colorful walled cloisters of Santa Catalina Monastery, a miniature city ' ||
            'in itself, and admire majestic views of the Misti and Chachani volcanoes.',
            1, v_cultura, v_arequipa, '4h', 'Plaza de Armas', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 45.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 67.50, 10);
END IF;

-- 13. Arequipa Picanterias Tour
IF NOT EXISTS (SELECT 1 FROM experiences WHERE title = 'Arequipa Picanterias Tour' AND destination_id = v_arequipa) THEN
    INSERT INTO experiences (title, description, agency_id, category_id, destination_id, duration,
                             meeting_point, cancellation_policy_type, cancellation_policy_description, created_at, updated_at)
    VALUES ('Arequipa Picanterias Tour',
            'Delve into the spicy regional cuisine of southern Peru. Visit traditional ''picanteria'' kitchens, ' ||
            'learning about the wood-fired clay stoves, and sample typical specialties like rocoto relleno, ' ||
            'creamy pastel de papa, and refreshing chicha de jora corn beer.',
            1, v_gastronomia, v_arequipa, '3h', 'Calle Santa Catalina', 'FLEXIBLE', 'Flexible cancellation policy.', NOW(), NOW())
    RETURNING id INTO v_exp_id;
    INSERT INTO availabilities (experience_id, start_date_time, end_date_time, total_capacity, created_at, updated_at)
    VALUES (v_exp_id, NOW(), NOW() + INTERVAL '90 days', 50, NOW(), NOW()) RETURNING id INTO v_avail_id;
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_gen, 50.00, 30);
    INSERT INTO availability_ticket_types (availability_id, ticket_type_id, price, capacity) VALUES (v_avail_id, v_vip, 75.00, 10);
END IF;

END $$;

-- ─────────────────────────────────────────────────────────────────────────────
-- 6. USUARIOS DEMO  (tabla: users + user_roles)
-- Se insertan 4 usuarios de prueba para que las reseñas funcionen.
-- Password: "password123" (BCrypt hash pre-generado)
-- ─────────────────────────────────────────────────────────────────────────────
INSERT INTO users (email, password, first_name, last_name, phone, profile_type, is_active, email_verified, created_at, updated_at)
SELECT 'tourist@travelmatch.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Carlos', 'Lopez', '+51 999 111 222', 'STANDARD', true, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'tourist@travelmatch.com');

INSERT INTO users (email, password, first_name, last_name, phone, profile_type, is_active, email_verified, created_at, updated_at)
SELECT 'corporate@travelmatch.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Maria', 'Garcia', '+51 999 222 000', 'CORPORATE', true, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'corporate@travelmatch.com');

INSERT INTO users (email, password, first_name, last_name, phone, profile_type, is_active, email_verified, created_at, updated_at)
SELECT 'staff@agency.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ana', 'Torres', '+51 1 999 444 555', 'UNDEFINED', true, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'staff@agency.com');

INSERT INTO users (email, password, first_name, last_name, phone, profile_type, is_active, email_verified, created_at, updated_at)
SELECT 'admin@travelmatch.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'User', '+51 1 888 777 000', 'UNDEFINED', true, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@travelmatch.com');

-- Asignar roles a los usuarios demo
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.email = 'tourist@travelmatch.com' AND r.name = 'ROLE_TOURIST'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.email = 'corporate@travel.com' AND r.name = 'ROLE_TOURIST'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.email = 'staff@agency.com' AND r.name = 'ROLE_AGENCY_STAFF'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.email = 'admin@travelmatch.com' AND r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────────────────────
-- 7. RESEÑAS  (tabla: reviews)
-- ─────────────────────────────────────────────────────────────────────────────
DO $$
DECLARE
    v_exp BIGINT;
BEGIN
    v_exp := (SELECT id FROM experiences WHERE title = 'Machu Picchu Full Day' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 1, v_exp, 5, 'An absolute dream come true! The guide was very knowledgeable.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 1 AND experience_id = v_exp);
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 2, v_exp, 5, 'Extremely well organized. The train views were breathtaking.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 2 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Sacred Valley Explorer' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 3, v_exp, 4, 'A great way to see multiple ruins in a single day. Excellent buffet lunch.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 3 AND experience_id = v_exp);
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 4, v_exp, 5, 'Highly recommend this tour before heading to Machu Picchu.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 4 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Cusco City Walking Tour' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 1, v_exp, 4, 'A pleasant walking pace. The San Blas neighborhood is charming.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 1 AND experience_id = v_exp);
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 2, v_exp, 5, 'Incredible history told by a very passionate local guide.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 2 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Cusco Gastronomic Experience' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 3, v_exp, 5, 'Fabulous cooking class! The chef was fun and the food tasted amazing.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 3 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Rainbow Mountain Trek' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 4, v_exp, 5, 'Challenging altitude but the view at the summit is worth every single step.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 4 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Humantay Lake Trek' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 1, v_exp, 5, 'Prettiest lake I have ever seen. Absolutely magical landscape.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 1 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Lima Historic Center Tour' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 2, v_exp, 4, 'Catacombs tour was creepy but super cool. Nice historical walk.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 2 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Miraflores Food Tour' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 3, v_exp, 5, 'Lima food is unmatched. Best ceviche ever.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 3 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Lima Bike Tour' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 4, v_exp, 5, 'Super fun bike ride. Barranco murals are really beautiful.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 4 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Pachacamac Ruins Tour' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 1, v_exp, 4, 'A great historical archaeological site very close to Lima.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 1 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Colca Canyon Trek' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 2, v_exp, 5, 'Incredible landscape. Seeing the condors fly so close was unforgettable.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 2 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Arequipa City Tour' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 3, v_exp, 5, 'Santa Catalina monastery is absolutely stunning. Great city photos!', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 3 AND experience_id = v_exp);
    END IF;

    v_exp := (SELECT id FROM experiences WHERE title = 'Arequipa Picanterias Tour' LIMIT 1);
    IF v_exp IS NOT NULL THEN
        INSERT INTO reviews (user_id, experience_id, rating, comment, created_at, updated_at)
        SELECT 4, v_exp, 5, 'Spicy, authentic, and delicious. Real Peruvian country flavors.', NOW(), NOW()
        WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE user_id = 4 AND experience_id = v_exp);
    END IF;
END $$;

-- =============================================================================
-- FIN DEL SCRIPT
-- Nota: los usuarios demo se crean en la sección 6 del script.
-- Los roles/categorías/ticket types también se insertan aquí (sección 1-2).
-- Este script es autónomo: se puede ejecutar completo sobre una BD vacía
-- después de que Hibernate haya creado las tablas (ddl-auto=update/create).
-- =============================================================================
