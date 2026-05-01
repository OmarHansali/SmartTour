INSERT INTO users (id, name, email, avatar_url, created_at) VALUES 
('550e8400-e29b-41d4-a716-446655440000', 'Demo User', 'demo@smarttour.com', 'https://i.pravatar.cc/150?img=12', CURRENT_TIMESTAMP);

INSERT INTO places (id, name, description, latitude, longitude, category, rating, image_url, address, google_place_id, created_at) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'National History Museum', 'A world-class museum featuring artifacts from ancient civilizations through modern times', 40.7489, -73.9680, 'MUSEUM', 4.7, 'https://picsum.photos/400/300?random=1', 'Central Park West & 79th St, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcA', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440002', 'Riverside Park', 'A scenic waterfront park perfect for walking, jogging, and picnicking with river views', 40.7829, -73.9654, 'PARK', 4.5, 'https://picsum.photos/400/300?random=2', 'Riverside Dr, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcB', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440003', 'The Corner Cafe', 'A charming local cafe with artisanal coffee and homemade pastries', 40.7580, -73.9855, 'CAFE', 4.3, 'https://picsum.photos/400/300?random=3', '123 Main St, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcC', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440004', 'St. Patrick Cathedral', 'A magnificent neo-gothic cathedral and iconic landmark', 40.7586, -73.9762, 'RELIGIOUS', 4.8, 'https://picsum.photos/400/300?random=4', '5th Ave, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcD', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440005', 'Downtown Shopping Center', 'A vibrant shopping district with boutiques, restaurants, and entertainment', 40.7614, -73.9776, 'SHOPPING', 4.2, 'https://picsum.photos/400/300?random=5', 'Broadway & 7th Ave, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcE', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440006', 'Empire View Tower', 'Historic observation tower offering panoramic city views', 40.7505, -73.9934, 'LANDMARK', 4.6, 'https://picsum.photos/400/300?random=6', '350 5th Ave, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcF', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440007', 'Botanical Garden', 'A lush garden showcasing diverse plant species from around the world', 40.7673, -73.9803, 'NATURE', 4.7, 'https://picsum.photos/400/300?random=7', 'Bronx River Pkwy, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcG', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440008', 'Modern Art Gallery', 'Contemporary art space featuring rotating exhibitions and installations', 40.7396, -73.9987, 'MUSEUM', 4.4, 'https://picsum.photos/400/300?random=8', '11 W 53rd St, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcH', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440009', 'Metropolitan Theater', 'A grand performing arts venue hosting Broadway shows and concerts', 40.7590, -73.9845, 'ENTERTAINMENT', 4.6, 'https://picsum.photos/400/300?random=9', '214 W 42nd St, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcI', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440010', 'Skyline Restaurant', 'Upscale dining with rooftop views and international cuisine', 40.7527, -73.9772, 'RESTAURANT', 4.5, 'https://picsum.photos/400/300?random=10', '45 Rockefeller Plaza, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcJ', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440011', 'Heritage Library', 'A stunning architectural landmark housing rare books and manuscripts', 40.7532, -73.9822, 'MUSEUM', 4.3, 'https://picsum.photos/400/300?random=11', '476 5th Ave, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcK', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440012', 'Sunset Pier', 'A waterfront recreational area with boats, fishing, and sunset views', 40.7282, -73.9942, 'NATURE', 4.2, 'https://picsum.photos/400/300?random=12', 'Hudson River Park, New York', 'ChIJd8BlQ2BZwokRAFUEcm_qrcL', CURRENT_TIMESTAMP);

INSERT INTO favorites (id, user_id, place_id, created_at) VALUES
('550e8400-e29b-41d4-a716-446655440020', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440021', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440004', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440022', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440006', CURRENT_TIMESTAMP);

INSERT INTO visit_history (id, user_id, place_id, visited_at, user_rating, notes) VALUES
('550e8400-e29b-41d4-a716-446655440030', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - 7, 5.0, 'Amazing collection of artifacts!'),
('550e8400-e29b-41d4-a716-446655440031', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP - 5, 4.5, 'Great coffee and cozy atmosphere.'),
('550e8400-e29b-41d4-a716-446655440032', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440010', CURRENT_TIMESTAMP - 3, 4.0, 'Beautiful views from the rooftop.'),
('550e8400-e29b-41d4-a716-446655440033', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440008', CURRENT_TIMESTAMP - 1, 5.0, 'The modern art exhibition was breathtaking.');
