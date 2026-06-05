from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from models import Base, SystemLog

# Використовуємо SQLite 
DATABASE_URL = "sqlite:///social_network.db"
engine = create_engine(DATABASE_URL, echo=False) 

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

def init_db():
    Base.metadata.create_all(bind=engine)

def log_action(session, action: str, details: str):
    """Функція для збереження логів дій адміністратора/системи у БД"""
    log_entry = SystemLog(action=action, details=details)
    session.add(log_entry)
    session.commit()