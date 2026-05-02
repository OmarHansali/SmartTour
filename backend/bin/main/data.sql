-- Clear existing data first
DELETE FROM visit_history;
DELETE FROM favorites;
DELETE FROM trip_places;
DELETE FROM trips;
DELETE FROM places;
DELETE FROM user_preferences;
DELETE FROM users;

-- Demo user
INSERT INTO users (id, name, email, avatar_url, created_at) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'Demo User', 'demo@smarttour.com', null, CURRENT_TIMESTAMP);

-- Places around Marrakech Medina
INSERT INTO places (id, name, description, latitude, longitude, category, rating, address, image_url, created_at) VALUES
('660e8400-e29b-41d4-a716-446655440001', 'Koutoubia Mosque', 'The largest mosque in Marrakech, built in the 12th century. Its minaret is a masterpiece of Almohad architecture.', 31.6238, -7.9930, 'RELIGIOUS', 4.8, 'Avenue Mohammed V, Marrakech', null, CURRENT_TIMESTAMP),
('660e8400-e29b-41d4-a716-446655440002', 'Jardin Majorelle', 'Iconic garden designed by French painter Jacques Majorelle, now owned by Yves Saint Laurent.', 31.6417, -8.0033, 'NATURE', 4.7, 'Rue Yves Saint Laurent, Marrakech', null, CURRENT_TIMESTAMP),
('660e8400-e29b-41d4-a716-446655440003', 'Bahia Palace', 'A stunning 19th-century palace with beautiful gardens and intricate tilework.', 31.6212, -7.9830, 'HISTORIC', 4.6, 'Rue Riad Zitoun el Jdid, Marrakech', null, CURRENT_TIMESTAMP),
('660e8400-e29b-41d4-a716-446655440004', 'Jemaa el-Fna Square', 'The famous main square of Marrakech, a UNESCO World Heritage site buzzing with life day and night.', 31.6258, -7.9891, 'LANDMARK', 4.9, 'Place Jemaa el-Fna, Marrakech', null, CURRENT_TIMESTAMP),
('660e8400-e29b-41d4-a716-446655440005', 'Saadian Tombs', 'Magnificent royal necropolis dating from the Saadian dynasty, rediscovered in 1917.', 31.6185, -7.9884, 'HISTORIC', 4.5, 'Rue de la Kasbah, Marrakech', null, CURRENT_TIMESTAMP),
('660e8400-e29b-41d4-a716-446655440006', 'Medina Souks', 'A labyrinth of traditional markets selling spices, leather goods, textiles and handicrafts.', 31.6295, -7.9851, 'SHOPPING', 4.7, 'Medina, Marrakech', null, CURRENT_TIMESTAMP),
('660e8400-e29b-41d4-a716-446655440007', 'Badi Palace', 'Ruins of a once-magnificent 16th-century palace built by Sultan Ahmed al-Mansour.', 31.6194, -7.9857, 'HISTORIC', 4.3, 'Place des Ferblantiers, Marrakech', null, CURRENT_TIMESTAMP),
('660e8400-e29b-41d4-a716-446655440008', 'Mellah Jewish Quarter', 'Historic Jewish quarter of Marrakech with its distinctive architecture and heritage.', 31.6220, -7.9820, 'LANDMARK', 4.2, 'Mellah, Marrakech', null, CURRENT_TIMESTAMP),
('660e8400-e29b-41d4-a716-446655440009', 'Le Jardin Secret', 'Beautifully restored 16th-century garden in the heart of the medina.', 31.6310, -7.9870, 'NATURE', 4.6, 'Rue Mouassine, Marrakech', null, CURRENT_TIMESTAMP),
('660e8400-e29b-41d4-a716-446655440010', 'Museum of Marrakech', 'Housed in a 19th-century palace, displaying Moroccan art and historical artifacts.', 31.6305, -7.9875, 'MUSEUM', 4.4, 'Place Ben Youssef, Marrakech', null, CURRENT_TIMESTAMP),
('660e8400-e29b-41d4-a716-446655440011', 'Cafe de France', 'Historic cafe overlooking Jemaa el-Fna square, perfect for watching the city buzz below.', 31.6261, -7.9882, 'CAFE', 4.1, 'Place Jemaa el-Fna, Marrakech', null, CURRENT_TIMESTAMP),
('660e8400-e29b-41d4-a716-446655440012', 'Menara Gardens', 'Vast olive grove and gardens with a beautiful pavilion and reflecting pool, dating from the 12th century.', 31.6103, -8.0283, 'NATURE', 4.5, 'Avenue de la Menara, Marrakech', null, CURRENT_TIMESTAMP);

-- Favorites for demo user
INSERT INTO favorites (id, user_id, place_id, created_at) VALUES
('770e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP),
('770e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP),
('770e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440004', CURRENT_TIMESTAMP);

-- Visit history
INSERT INTO visit_history (id, user_id, place_id, user_rating, notes, visited_at) VALUES
('880e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440001', 5.0, 'Stunning architecture, visited at sunset!', CURRENT_TIMESTAMP),
('880e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440004', 4.5, 'Amazing atmosphere in the evening', CURRENT_TIMESTAMP),
('880e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440002', 4.8, 'Beautiful garden, very peaceful', CURRENT_TIMESTAMP),
('880e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440006', 4.0, 'Great shopping experience', CURRENT_TIMESTAMP);