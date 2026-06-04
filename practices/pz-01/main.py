# Task 1
print("\nЗавдання 1")
for i in range(1, 11):
    print(i)

# Task 2
print("\nЗавдання 2")
num1 = float(input("Перше число -> "))
num2 = float(input("Друге число -> "))
num3 = float(input("Третє число -> "))

average = (num1 + num2 + num3) / 3

print(f"Середнє значення -> {average:.2f}")

# Task 3
print("\nЗавдання 3")
from datetime import datetime

current_year = datetime.now().year
birth_year = int(input("Ваш рік народження -> "))
age = current_year - birth_year

# Виводимо результат
print(f"Ваш вік -> {age} років.")

# Task 4
print("\nЗавдання 4")
class Book:
    def __init__(self, title, author, year):
        self.title = title
        self.author = author
        self.year = year

    def display_info(self):
        print("~ Інформація про книгу ~")
        print(f"Назва ->       {self.title}")
        print(f"Автор ->       {self.author}")
        print(f"Рік видання -> {self.year}")

my_book = Book(
    title="Тигролови", 
    author="Іван Багряний", 
    year=1944
)

my_book.display_info()