// Вхідні дані: JSON-рядок
const inputJsonString = '{"user": "Maks", "age": 20, "courses": ["React", "JavaScript"], "isActive": true}';

console.log("JSON-рядок ->");
console.log(inputJsonString);

try {
    const parsedData = JSON.parse(inputJsonString);

    console.log("\nРезультат ->");
    console.table(parsedData);

   } catch (error) {
    console.error("Помилка: Вхідний рядок не є дійсним JSON.", error);
}