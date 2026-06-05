import React, { useState, useEffect } from 'react';

function App() {
  const [tracks, setTracks] = useState([]);
  const [playlists, setPlaylists] = useState([]);
  const [activeGenre, setActiveGenre] = useState('');
  const [currentTrack, setCurrentTrack] = useState(null);
  
  // Імітація авторизованого користувача 
  const [currentUser, setCurrentUser] = useState({ id: 1, username: 'music_lover', profileInfo: 'Хто я..?' });
  const [reviewText, setReviewText] = useState('');
  const [trackReviews, setTrackReviews] = useState([]);

  // Завантаження даних
  useEffect(() => {
    fetchTracks();
    fetchPlaylists();
  }, [activeGenre]);

  const fetchTracks = () => {
    const url = activeGenre 
      ? `http://localhost:3000/api/tracks/genre/${activeGenre}` 
      : 'http://localhost:3000/api/tracks';
    
    fetch(url)
      .then(res => res.json())
      .then(data => setTracks(data));
  };

  const fetchPlaylists = () => {
    fetch('http://localhost:3000/api/playlists')
      .then(res => res.json())
      .then(data => setPlaylists(data));
  };

  const fetchReviews = (trackId) => {
    fetch(`http://localhost:3000/api/reviews/track/${trackId}`)
      .then(res => res.json())
      .then(data => setTrackReviews(data));
  };

  const handlePlay = (track) => {
    setCurrentTrack(track);
    fetchReviews(track.id);
  };

  const submitReview = (e) => {
    e.preventDefault();

    if (!currentTrack || !reviewText.trim()) return;

    fetch('http://localhost:3000/api/reviews', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ trackId: currentTrack.id, userId: currentUser.id, text: reviewText })
    })
    .then(res => res.json())
    .then(newReview => {
      setTrackReviews([...trackReviews, newReview]);
      setReviewText('');
    });
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial' }}>
      <h1>Super Swag Music Stream</h1>
      
      <div style={{ background: '#f0f0f0', padding: '10px', borderRadius: '8px', marginBottom: '20px' }}>
        <strong>👤 Профіль:</strong> {currentUser.username} | <em>{currentUser.profileInfo}</em>
      </div>

      <div style={{ display: 'flex', gap: '20px' }}>
        <div style={{ flex: 2 }}>
          <h2>Усі треки</h2>
          <div style={{ marginBottom: '10px' }}>
            <label>Фільтр за жанром: </label>
            <select value={activeGenre} onChange={(e) => setActiveGenre(e.target.value)}>
              <option value="">Всі жанри</option>
              <option value="Synth">Synth</option>
              <option value="Hyper-pop">Hyper-pop</option>
              <option value="Classical">Classical</option>
            </select>
          </div>

          <ul style={{ listStyle: 'none', padding: 0 }}>
            {tracks.map(track => (
              <li key={track.id} style={{ border: '1px solid #ccc', padding: '10px', margin: '5px 0', display: 'flex', justifyContent: 'space-between' }}>
                <div>
                  <strong>{track.title}</strong> - {track.artist} <br/>
                  <small>Жанр: {track.genre}</small>
                </div>
                <div>
                  <button onClick={() => handlePlay(track)} style={{ marginRight: '10px' }}>▶ Слухати</button>
                  <a href={`http://localhost:3000/api/download/${track.file}`} download>
                    <button>Завантажити</button>
                  </a>
                </div>
              </li>
            ))}
          </ul>
        </div>

        {/* Права колонка*/}
        <div style={{ flex: 1 }}>
          <h2>🎵 Плеєр</h2>
          {currentTrack ? (
            <div style={{ background: '#e3f2fd', padding: '15px', borderRadius: '8px' }}>
              <h3>{currentTrack.title}</h3>
              <p>{currentTrack.artist}</p>
              {/* Рівень 2: Аудіоплеєр */}
              <audio 
                controls 
                autoPlay 
                src={`http://localhost:3000/api/stream/${currentTrack.file}`} 
                style={{ width: '100%' }}
              />
              
              <hr />
              {/* Відгуки */}
              <h4>Відгуки:</h4>
              <ul>
                {trackReviews.map(r => (
                  <li key={r.id}>Користувач #{r.userId}: {r.text}</li>
                ))}
              </ul>
              
              <form onSubmit={submitReview}>
                <input 
                  type="text" 
                  value={reviewText} 
                  onChange={(e) => setReviewText(e.target.value)} 
                  placeholder="Залиште відгук..." 
                  style={{ width: '70%', padding: '5px' }}
                />
                <button type="submit" style={{ padding: '5px' }}>Надіслати</button>
              </form>
            </div>
          ) : (
            <p>Оберіть трек для відтворення</p>
          )}

          <h2>Плейлисти</h2>
          <ul>
            {playlists.map(pl => (
              <li key={pl.id}>
                <strong>{pl.name}</strong> (Треків: {pl.tracks.length})
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}

export default App;