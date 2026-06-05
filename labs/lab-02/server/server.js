const express = require('express');
const cors = require('cors');
const path = require('path');
const app = express();

app.use(cors());
app.use(express.json());

// БД
const tracks = [
    { id: 1, title: 'Elevator music', artist: 'Felix', genre: 'Synth', file: 'elevator.mp3' },
    { id: 2, title: 'DOGMATICA', artist: 'Femtanyl', genre: 'Hyper-pop', file: 'elevator.mp3' },
    { id: 3, title: 'Gymnopédie No. 3', artist: 'Satie', genre: 'Classical', file: 'elevator.mp3' }
];

const playlists = [
    { id: 1, name: 'Для роботи', tracks: [1, 2] },
    { id: 2, name: 'Тренування', tracks: [1, 3] }
];

let users = [
    { id: 1, username: 'music_lover', profileInfo: 'Хто я..?' },
    { id: 2, username: 'crazy_dave', profileInfo: 'Bueahgoiuafheogu!' }
];

let reviews = [
    { id: 1, trackId: 1, userId: 1, text: 'Тестовий відгук.' }
];

// Відображення треків 
app.get('/api/tracks', (req, res) => {
    res.json(tracks);
});

// Прослуховування, завантаження, жанри та плейлисти
app.get('/api/tracks/genre/:genre', (req, res) => {
    const filtered = tracks.filter(t => t.genre.toLowerCase() === req.params.genre.toLowerCase());

    res.json(filtered);
});

// Стрімінг 
app.get('/api/stream/:filename', (req, res) => {
  const filePath = path.join(__dirname, 'music', req.params.filename);

    res.sendFile(filePath);
});

// Завантаження файлу
app.get('/api/download/:filename', (req, res) => {
    const filePath = path.join(__dirname, 'music', req.params.filename);

    res.download(filePath);
});

// Отримання плейлистів
app.get('/api/playlists', (req, res) => {
    res.json(playlists);
});

// Користувачі, профілі, відгуки 
app.get('/api/users/:id', (req, res) => {
    const user = users.find(u => u.id === parseInt(req.params.id));

    if (user) res.json(user);
    else res.status(404).json({ message: 'Користувача не знайдено' });
});

// Отримання відгуків 
app.get('/api/reviews/track/:trackId', (req, res) => {
    const trackReviews = reviews.filter(r => r.trackId === parseInt(req.params.trackId));

    res.json(trackReviews);
});

// Створення відгуку
app.post('/api/reviews', (req, res) => {
    const { trackId, userId, text } = req.body;
    const newReview = { id: reviews.length + 1, trackId, userId, text };

    reviews.push(newReview);

    res.status(201).json(newReview);
});

const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});