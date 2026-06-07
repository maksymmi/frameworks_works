package com.example.mypz_3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ініціалізуємо тестові файли для завдань 3 і 4
        setupDummyFiles()

        // Частка чисел
        val etNumA = findViewById<EditText>(R.id.etNumA)
        val etNumB = findViewById<EditText>(R.id.etNumB)
        val btnDivide = findViewById<Button>(R.id.btnDivide)
        val tvResultDiv = findViewById<TextView>(R.id.tvResultDiv)

        btnDivide.setOnClickListener {
            val aStr = etNumA.text.toString()
            val bStr = etNumB.text.toString()

            if (aStr.isNotEmpty() && bStr.isNotEmpty()) {
                val b = bStr.toDouble()
                if (b == 0.0) {
                    tvResultDiv.text = "Помилка: Ділення на нуль!"
                } else {
                    val result = aStr.toDouble() / b
                    tvResultDiv.text = "Результат: $result"
                }
            } else {
                tvResultDiv.text = "Будь ласка, введіть обидва числа."
            }
        }

        // Гра кістки
        val etDiceCount = findViewById<EditText>(R.id.etDiceCount)
        val btnRollDice = findViewById<Button>(R.id.btnRollDice)
        val tvResultDice = findViewById<TextView>(R.id.tvResultDice)

        btnRollDice.setOnClickListener {
            val countStr = etDiceCount.text.toString()
            if (countStr.isNotEmpty()) {
                val count = countStr.toInt()
                var sumPlayer1 = 0
                var sumPlayer2 = 0

                for (i in 1..count) {
                    sumPlayer1 += (1..6).random()
                    sumPlayer2 += (1..6).random()
                }

                val winner = when {
                    sumPlayer1 > sumPlayer2 -> "Переміг Гравець 1!"
                    sumPlayer2 > sumPlayer1 -> "Переміг Гравець 2!"
                    else -> "Нічия!"
                }

                tvResultDice.text = "Гравець 1: $sumPlayer1 очок\nГравець 2: $sumPlayer2 очок\n$winner"
            } else {
                tvResultDice.text = "Введіть кількість кубиків."
            }
        }

        // Рівень 3 і 4 - файли
        val btnProcessFiles = findViewById<Button>(R.id.btnProcessFiles)
        val tvResultFiles = findViewById<TextView>(R.id.tvResultFiles)

        btnProcessFiles.setOnClickListener {
            val file1 = File(filesDir, "file1.txt")
            val file2 = File(filesDir, "file2.txt")

            // Підрахунок речень
            val sentencesRegex = Regex("[.!?]+")
            val text1 = file1.readText()
            val text2 = file2.readText()
            val sentencesCount1 = text1.split(sentencesRegex).count { it.isNotBlank() }
            val sentencesCount2 = text2.split(sentencesRegex).count { it.isNotBlank() }

            // Кількість однакових рядків
            val lines1 = file1.readLines()
            val lines2 = file2.readLines()
            val commonLinesCount = lines1.intersect(lines2.toSet()).size

            tvResultFiles.text = "Рівень 3:\n" +
                    "Речень у Файлі 1: $sentencesCount1\n" +
                    "Речень у Файлі 2: $sentencesCount2\n\n" +
                    "Рівень 4:\n" +
                    "Кількість однакових рядків: $commonLinesCount"
        }
    }

    // Допоміжна функція: створює два файли у внутрішній пам'яті телефону
    private fun setupDummyFiles() {
        val file1 = File(filesDir, "file1.txt")
        val file2 = File(filesDir, "file2.txt")

        if (!file1.exists()) {
            file1.writeText("Це перше речення. А ось і друге.\nЦей рядок однаковий в обох файлах.\nУнікальний рядок для першого файлу.")
        }

        if (!file2.exists()) {
            file2.writeText("Це тестовий файл.\nЦей рядок однаковий в обох файлах.\nЗовсім інший текст.")
        }
    }
}