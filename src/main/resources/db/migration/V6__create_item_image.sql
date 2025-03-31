CREATE TABLE item_image(
    id SERIAL PRIMARY KEY,
    item_id SERIAL NOT NULL,
    image_path TEXT NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
)