# Fitness Tracker — Progetto del corso di Basi di Dati (A.A. 2025/2026)

Applicazione desktop per la gestione delle routine di allenamento e il
monitoraggio delle performance atletiche. Realizzata in Java con interfaccia
grafica Swing secondo il pattern **MVC + DAO**, su database **MySQL**.

## Requisiti
- Java 21+
- MySQL 8.0+
- Gradle (incluso tramite *wrapper*, non serve installarlo)

## Struttura del progetto
```
DB2025-WorkOut/
├── sql/
│   ├── schema.sql       # creazione dello schema e delle tabelle
│   ├── query.sql        # trigger (AggiornaVolumeTotaleSessione)
│   └── population.sql    # dati di esempio (utenti, esercizi, schede, sessioni)
├── src/
│   ├── main/java/it/unibo/workout/   # codice sorgente (model, controller, view)
│   └── test/java/it/unibo/workout/   # test di integrazione JUnit 5
├── doc/                 # relazione (sorgente LaTeX, PDF e immagini)
├── build.gradle.kts     # configurazione Gradle (Kotlin DSL)
└── gradlew / gradlew.bat
```

## Creazione e popolamento del database
1. Avviare MySQL in locale (default `localhost:3306`).
2. Eseguire i tre script **in quest'ordine** (lo schema, poi il trigger, poi i dati):
   ```bash
   mysql -u root -p < sql/schema.sql
   mysql -u root -p < sql/query.sql
   mysql -u root -p < sql/population.sql
   ```
   > Su macOS, se il client `mysql` non è nel PATH, usare il percorso completo,
   > ad es. `/usr/local/mysql/bin/mysql -u root -p < sql/schema.sql`.

3. Lo script crea e popola il database **`WorkoutDB`**.

## Configurazione della connessione
L'applicazione si connette al database con i parametri definiti in:
```
src/main/java/it/unibo/workout/model/DatabaseConnection.java
```
Per impostazione predefinita si collega come utente `root` su
`localhost:3306/WorkoutDB`. Se la propria installazione MySQL usa credenziali
diverse (utente o password), modificare di conseguenza i valori in quel file.

## Avvio dell'applicazione
Da terminale, nella cartella radice del progetto:
- **Linux / macOS:** `./gradlew clean run`
- **Windows:** `.\gradlew.bat clean run`

Se su Linux/macOS Gradle non parte per i permessi, rendere eseguibile il wrapper:
```bash
chmod +x gradlew
```

## Test
La suite JUnit è composta da **test di integrazione**: richiede MySQL attivo e
il database già creato e popolato (vedere i passi sopra). Per eseguirli:
```bash
./gradlew build        # compila ed esegue i test
```

## Credenziali di esempio
Dopo il popolamento sono disponibili questi account (password in chiaro solo a
scopo dimostrativo):

| Ruolo  | Email                          | Password   |
|--------|--------------------------------|------------|
| Coach  | `mario.rossi@unibo.it`         | `coach123` |
| Coach  | `giulia.ferrari@unibo.it`      | `coach456` |
| Atleta | `federico.locatelli@unibo.it`  | `fede123`  |
| Atleta | `luca.bianchi@unibo.it`        | `luca456`  |

All'avvio si seleziona il ruolo (Atleta o Coach) e si accede con email e
password. La registrazione di nuovi profili è disponibile dalla schermata di
login.
