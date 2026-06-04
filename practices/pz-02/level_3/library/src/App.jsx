import React, { useState } from 'react';

function App() {
  const [books, setBooks] = useState([]);

  const [title, setTitle] = useState('');
  const [author, setAuthor] = useState('');

  const handleAddBook = (e) => {
    e.preventDefault(); 
    
    if (!title.trim() || !author.trim()) return;

    const newBook = {
      id: Date.now(),
      title: title,
      author: author,
      mark: 'Немає' 
    };

    setBooks([...books, newBook]);

    setTitle('');
    setAuthor('');
  };

  const handleDeleteBook = (idToRemove) => {
    const updatedBooks = books.filter(book => book.id !== idToRemove);
    setBooks(updatedBooks);
  };

  const handleUpdateMark = (idToUpdate, newMark) => {
    const updatedBooks = books.map(book => {
      if (book.id === idToUpdate) {
        return { ...book, mark: newMark }; 
      }
      return book; 
    });
    setBooks(updatedBooks);
  };

  return (
    <div style={{ maxWidth: '600px', margin: '40px auto', padding: '20px', fontFamily: 'sans-serif' }}>
      <h1 style={{ textAlign: 'center', color: '#333' }}>Бібліотека</h1>

      {/* Форма для додавання нових книг */}
      <form onSubmit={handleAddBook} style={{ display: 'flex', gap: '10px', marginBottom: '30px' }}>
        <input 
          type="text" 
          placeholder="Назва книги" 
          value={title} 
          onChange={(e) => setTitle(e.target.value)} 
          style={{ flex: 1, padding: '10px', fontSize: '16px', borderRadius: '4px', border: '1px solid #ccc' }}
        />
        <input 
          type="text" 
          placeholder="Автор" 
          value={author} 
          onChange={(e) => setAuthor(e.target.value)} 
          style={{ flex: 1, padding: '10px', fontSize: '16px', borderRadius: '4px', border: '1px solid #ccc' }}
        />
        <button type="submit" style={{ padding: '10px 20px', fontSize: '16px', backgroundColor: '#28a745', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
          Додати
        </button>
      </form>

      {/* Список книг.*/}
      {books.length === 0 ? (
        <p style={{ textAlign: 'center', color: '#777', fontSize: '18px' }}>У бібліотеці ще немає книг.</p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
          {books.map(book => (
            <div key={book.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '15px', border: '1px solid #ddd', borderRadius: '6px', backgroundColor: '#f9f9f9' }}>
              <div>
                <h3 style={{ margin: '0 0 5px 0', color: '#222' }}>{book.title}</h3>
                <p style={{ margin: 0, color: '#666', fontSize: '14px' }}>Автор: {book.author}</p>
                <span style={{ display: 'inline-block', marginTop: '5px', padding: '3px 8px', fontSize: '12px', backgroundColor: '#e2e8f0', borderRadius: '12px', fontWeight: 'bold' }}>
                  Статус: {book.mark}
                </span>
              </div>
              
              <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
                {/* Випадаючий список для зміни помітки книги */}
                <select 
                  value={book.mark} 
                  onChange={(e) => handleUpdateMark(book.id, e.target.value)}
                  style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc', cursor: 'pointer' }}
                >
                  <option value="Немає">Без помітки</option>
                  <option value="Вибране">Вибране</option>
                  <option value="Прочитане">Прочитане</option>
                  <option value="Бажане">Бажане</option>
                </select>
                
                {/* Кнопка видалення книги */}
                <button 
                  onClick={() => handleDeleteBook(book.id)}
                  style={{ padding: '8px 12px', backgroundColor: '#dc3545', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                >
                  Видалити
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default App;