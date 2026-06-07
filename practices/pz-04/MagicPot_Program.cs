using System;
using System.Collections.Generic;
using System.Threading;

namespace MagicPot
{
    // ==================== СТАНИ ГОРЩИКА ====================
    public enum PotState
    {
        Idle,        // Спокій
        Cooking,     // Вариться
        Overflowing, // Переповнюється
        Flooding     // Затоплює все навколо
    }

    // ==================== КЛАС ГОРЩИК ====================
    public class MagicPot
    {
        private PotState _state;
        private int _porridgeAmount;
        private const int MaxCapacity = 10;
        private const int HouseCapacity = 30;
        private const int VillageCapacity = 60;

        public PotState State => _state;
        public int PorridgeAmount => _porridgeAmount;
        public bool IsCooking => _state == PotState.Cooking || _state == PotState.Overflowing || _state == PotState.Flooding;

        public MagicPot()
        {
            _state = PotState.Idle;
            _porridgeAmount = 0;
        }

        public void StartCooking(string commandBy)
        {
            if (_state == PotState.Idle)
            {
                _state = PotState.Cooking;
                Print($"[Горщик] 🔥 Починає варити кашу на команду: {commandBy}");
            }
            else
            {
                Print("[Горщик] Я вже варю кашу!");
            }
        }

        public void StopCooking(string commandBy)
        {
            if (IsCooking)
            {
                _state = PotState.Idle;
                Print($"[Горщик] ✅ Зупиняється на команду: {commandBy}");
                Print($"[Горщик] Залишок каші: {_porridgeAmount} одиниць");
            }
            else
            {
                Print("[Горщик] Я і так не варю.");
            }
        }

        // Один «тік» варіння
        public void Cook()
        {
            if (!IsCooking) return;

            _porridgeAmount += 3;

            if (_porridgeAmount > VillageCapacity && _state != PotState.Flooding)
            {
                _state = PotState.Flooding;
                Print("[Горщик] 🌊🌊🌊 КАША ЗАТОПЛЮЄ ВУЛИЦЮ!");
            }
            else if (_porridgeAmount > HouseCapacity && _state != PotState.Flooding)
            {
                _state = PotState.Overflowing;
                Print("[Горщик] 🌊 Каша виливається з будинку на вулицю!");
            }
            else if (_porridgeAmount > MaxCapacity && _state == PotState.Cooking)
            {
                _state = PotState.Overflowing;
                Print("[Горщик] ⚠️  Каша переповнює горщик і ллється в хату!");
            }
            else
            {
                PrintPorridgeLevel();
            }
        }

        public void EatPorridge(string eater, int amount)
        {
            int actual = Math.Min(amount, _porridgeAmount);
            if (actual <= 0)
            {
                Print($"[Горщик] Каші немає для {eater}...");
                return;
            }
            _porridgeAmount -= actual;
            Print($"[{eater}] 😋 З'їв {actual} одиниць каші. Залишок: {_porridgeAmount}");
        }

        private void PrintPorridgeLevel()
        {
            string bar = new string('█', Math.Min(_porridgeAmount, 30));
            Print($"[Горщик] 🍲 Вариться... [{bar}] {_porridgeAmount} од.");
        }

        public string GetStateDescription() => _state switch
        {
            PotState.Idle => "спокій",
            PotState.Cooking => "вариться",
            PotState.Overflowing => "переповнений, каша ллється в хату",
            PotState.Flooding => "каша затоплює вулицю",
            _ => "невідомо"
        };

        private static void Print(string msg) => Console.WriteLine(msg);
    }

    // ==================== БАЗОВИЙ КЛАС ПЕРСОНАЖА ====================
    public abstract class Character
    {
        public string Name { get; protected set; }

        protected Character(string name)
        {
            Name = name;
        }

        public virtual void SayStartCommand(MagicPot pot)
        {
            Console.WriteLine($"[{Name}] 🗣️  «Горщик, вари!»");
            pot.StartCooking(Name);
        }

        public virtual void SayStopCommand(MagicPot pot)
        {
            Console.WriteLine($"[{Name}] 🗣️  «Горщик, не вари!»");
            pot.StopCooking(Name);
        }

        public void Eat(MagicPot pot, int amount = 5)
        {
            Console.WriteLine($"[{Name}] Хоче їсти кашу...");
            pot.EatPorridge(Name, amount);
        }
    }

    // ==================== КЛАС ДІВЧИНКА ====================
    public class Girl : Character
    {
        public Girl() : base("Дівчинка") { }

        public void CollectInForest()
        {
            Console.WriteLine($"[{Name}] 🌲 Іде в ліс за грибами та ягодами...");
            Thread.Sleep(500);
            Console.WriteLine($"[{Name}] 🍄🫐 Зібрала гриби та ягоди!");
        }

        public void MeetOldWoman(OldWoman oldWoman, out MagicPot pot)
        {
            Console.WriteLine($"[{Name}] Зустріла стареньку в лісі.");
            Console.WriteLine($"[{Name}] 🫐 Пригощає стареньку ягідками.");
            pot = oldWoman.GivePot();
            Console.WriteLine($"[{Name}] Дякує старенькій і біжить додому!");
        }
    }

    // ==================== КЛАС МАМА ====================
    public class Mom : Character
    {
        private bool _remembersStopCommand = true;

        public Mom() : base("Мама") { }

        public void ForgetStopCommand()
        {
            _remembersStopCommand = false;
            Console.WriteLine($"[{Name}] 😨 Забула, як зупинити горщик!!!");
        }

        public override void SayStopCommand(MagicPot pot)
        {
            if (!_remembersStopCommand)
            {
                Console.WriteLine($"[{Name}] 😱 Намагається зупинити горщик але не пам'ятає слів!");
                Console.WriteLine($"[{Name}] «Стій!».. «Зупинись!».. «Досить!».. — нічого не допомагає!");
            }
            else
            {
                base.SayStopCommand(pot);
            }
        }
    }

    // ==================== КЛАС СТАРА ЖІНКА ====================
    public class OldWoman : Character
    {
        public OldWoman() : base("Стара жінка") { }

        public MagicPot GivePot()
        {
            Console.WriteLine($"[{Name}] ✨ «Ти добра дитина. Прийми цей чарівний горщик!»");
            Console.WriteLine($"[{Name}] Скажи «Горщик, вари!» — і він варитиме кашу.");
            Console.WriteLine($"[{Name}] Скажи «Горщик, не вари!» — і він зупиниться.");
            return new MagicPot();
        }
    }

    // ==================== КЛАС СЕЛО (ОРКЕСТРАТОР) ====================
    public class Village
    {
        private MagicPot _pot;
        private bool _isFlooded;

        public void RunScenario()
        {
            Console.OutputEncoding = System.Text.Encoding.UTF8;
            PrintHeader("🍲 ГОРЩИК КАШІ — Інтерактивна симуляція 🍲");

            // --- СЦЕНА 1: Зустріч у лісі ---
            PrintScene("СЦЕНА 1: Зустріч у лісі");

            var girl = new Girl();
            var mom = new Mom();
            var oldWoman = new OldWoman();

            girl.CollectInForest();
            girl.MeetOldWoman(oldWoman, out _pot);

            // --- СЦЕНА 2: Перше знайомство з горщиком ---
            PrintScene("СЦЕНА 2: Дівчинка показує горщик мамі");

            Console.WriteLine("[Дівчинка] Мамо, дивись яке диво!");
            girl.SayStartCommand(_pot);
            Tick(3);
            girl.Eat(_pot, 4);
            girl.SayStopCommand(_pot);

            ShowPotStatus();
            WaitUser();

            // --- СЦЕНА 3: Мама сама вдома ---
            PrintScene("СЦЕНА 3: Мама залишилась вдома сама");

            girl.CollectInForest();
            Console.WriteLine("[Мама] Хочу каші. Горщик же казав треба щось сказати...");
            mom.SayStartCommand(_pot);
            Tick(2);
            mom.Eat(_pot, 5);
            Console.WriteLine("[Мама] Наїлась. Тепер треба зупинити... але як?");
            mom.ForgetStopCommand();

            // --- СЦЕНА 4: Катастрофа! ---
            PrintScene("СЦЕНА 4: КАТАСТРОФА — горщик не зупиняється!");

            // Мама намагається зупинити
            for (int i = 0; i < 3; i++)
            {
                mom.SayStopCommand(_pot); // Не зупиняється — вона забула
                Tick(2);
            }

            _isFlooded = _pot.State == PotState.Flooding || _pot.State == PotState.Overflowing;
            if (_isFlooded)
            {
                Console.WriteLine();
                Console.WriteLine("  🏠💦💦💦💦💦💦💦💦💦💦💦💦💦💦💦");
                Console.WriteLine("  Каша заповнила будинок і потекла на вулицю!");
                Console.WriteLine("  Всі сусіди плавають у каші!");
                Console.WriteLine("  🏘️🌊🍲🌊🍲🌊🍲🌊🏘️");
                Console.WriteLine();
            }

            ShowPotStatus();
            WaitUser();

            // --- СЦЕНА 5: Дівчинка рятує ситуацію ---
            PrintScene("СЦЕНА 5: Дівчинка повертається з лісу");

            Console.WriteLine("[Дівчинка] 😱 Що сталось?! Все в каші!");
            Thread.Sleep(300);
            girl.SayStopCommand(_pot);

            Console.WriteLine();
            Console.WriteLine("  ✅ Горщик зупинився!");
            if (_isFlooded)
            {
                Console.WriteLine("  Але каша все ще скрізь...");
                Console.WriteLine("  Сусідам доводилось проїдати собі дорогу додому! 😄");
            }

            ShowPotStatus();

            // --- ЕПІЛОГ ---
            PrintScene("ЕПІЛОГ");
            Console.WriteLine("  І відтоді кажуть:");
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("  «Заварити кашу легко, а розхльобувати — то важче буде!»");
            Console.ResetColor();
            Console.WriteLine();

            // --- ІНТЕРАКТИВНИЙ РЕЖИМ ---
            RunInteractiveMode(_pot, girl, mom);
        }

        private void RunInteractiveMode(MagicPot pot, Girl girl, Mom mom)
        {
            PrintHeader("🎮 ІНТЕРАКТИВНИЙ РЕЖИМ");
            Console.WriteLine("Тепер ти можеш сам керувати горщиком!");
            Console.WriteLine();

            bool running = true;
            while (running)
            {
                ShowPotStatus();
                Console.WriteLine();
                Console.WriteLine("Що зробити?");
                Console.WriteLine("  1 — «Горщик, вари!» (від Дівчинки)");
                Console.WriteLine("  2 — «Горщик, не вари!» (від Дівчинки)");
                Console.WriteLine("  3 — «Горщик, вари!» (від Мами)");
                Console.WriteLine("  4 — Почекати (горщик варить ще)");
                Console.WriteLine("  5 — Поїсти кашу (Дівчинка)");
                Console.WriteLine("  0 — Вийти");
                Console.Write("Твій вибір: ");

                string input = Console.ReadLine()?.Trim() ?? "";
                Console.WriteLine();

                switch (input)
                {
                    case "1": girl.SayStartCommand(pot); break;
                    case "2": girl.SayStopCommand(pot); break;
                    case "3": mom.SayStartCommand(pot); break;
                    case "4": Tick(3); break;
                    case "5": girl.Eat(pot, 5); break;
                    case "0": running = false; break;
                    default:
                        Console.WriteLine("Невідома команда. Спробуй ще раз.");
                        break;
                }
                Console.WriteLine();
            }

            Console.WriteLine("До побачення! 🍲");
        }

        private void Tick(int times)
        {
            for (int i = 0; i < times; i++)
            {
                _pot.Cook();
                Thread.Sleep(200);
            }
        }

        private void ShowPotStatus()
        {
            Console.ForegroundColor = _pot.State switch
            {
                PotState.Idle => ConsoleColor.Green,
                PotState.Cooking => ConsoleColor.Cyan,
                PotState.Overflowing => ConsoleColor.Yellow,
                PotState.Flooding => ConsoleColor.Red,
                _ => ConsoleColor.White
            };
            Console.WriteLine($"  ══ Стан горщика: [{_pot.GetStateDescription().ToUpper()}] | Каші: {_pot.PorridgeAmount} од. ══");
            Console.ResetColor();
        }

        private static void PrintHeader(string title)
        {
            Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.Magenta;
            Console.WriteLine("╔══════════════════════════════════════════╗");
            Console.WriteLine($"║  {title,-40}║");
            Console.WriteLine("╚══════════════════════════════════════════╝");
            Console.ResetColor();
        }

        private static void PrintScene(string scene)
        {
            Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.DarkCyan;
            Console.WriteLine($"┌─── {scene} ───");
            Console.ResetColor();
        }

        private static void WaitUser()
        {
            Console.ForegroundColor = ConsoleColor.DarkGray;
            Console.WriteLine("  [Натисни Enter щоб продовжити...]");
            Console.ResetColor();
            Console.ReadLine();
        }
    }

    // ==================== ТОЧКА ВХОДУ ====================
    class Program
    {
        static void Main(string[] args)
        {
            var village = new Village();
            village.RunScenario();
        }
    }
}
