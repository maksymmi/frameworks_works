from database import SessionLocal, init_db, log_action
from models import User, Post, Comment, SystemLog

def add_user(session, username, email):
    user = User(username=username, email=email)
    session.add(user)
    session.commit()
    log_action(session, "CREATE_USER", f"Created user: {username}")
    return user

# Додавання нових постів та коментарів 
def add_post(session, user_id, content):
    post = Post(user_id=user_id, content=content)

    session.add(post)
    session.commit()

    log_action(session, "CREATE_POST", f"User {user_id} created a post ID {post.id}")

    return post

def add_comment(session, post_id, user_id, content):
    comment = Comment(post_id=post_id, user_id=user_id, content=content)

    session.add(comment)
    session.commit()

    log_action(session, "CREATE_COMMENT", f"User {user_id} commented on post {post_id}")

    return comment

# Пошук й фільтрація постів 
def search_posts(session, keyword=None, user_id=None):
    query = session.query(Post)
    
    if keyword:
        query = query.filter(Post.content.ilike(f"%{keyword}%"))

    if user_id:
        query = query.filter(Post.user_id == user_id)
        
    results = query.all()
    log_action(session, "SEARCH", f"Searched posts. Keyword: {keyword}, User ID: {user_id}")

    return results

# Інтерфейс для перегляду логів 
def view_admin_logs(session):
    print("\n~ Панель адміністратора -> логи ~")

    logs = session.query(SystemLog).order_by(SystemLog.timestamp.desc()).limit(10).all()
    for log in logs:
        print(f"[{log.timestamp}] {log.action}: {log.details}")

if __name__ == "__main__":
    init_db()
    db = SessionLocal()

    try:
        # Створюємо користувачів
        print("Створення користувачів ~")
        user1 = add_user(db, "byron_brawl", "alice@example.com")
        user2 = add_user(db, "edgar_sarf", "bob@example.com")

        # Додаємо пости та коментарі 
        print("Створення постів та коментарів...")

        post1 = add_post(db, user1.id, "Це мій перший пост.")
        post2 = add_post(db, user2.id, "I hate everyone here.")
        
        add_comment(db, post2.id, user1.id, "Окей бро.?")

        # Пошук та фільтрація 
        print("\n~ Результати пошуку за словом 'everyone' ~")
        linux_posts = search_posts(db, keyword="everyone")

        for p in linux_posts:
            print(f"Автор ID {p.user_id}: {p.content}")

        # 4. Перегляд логів 
        view_admin_logs(db)

    except Exception as e:
        print(f"Сталася помилка -> {e}")
        db.rollback()
    finally:
        db.close()