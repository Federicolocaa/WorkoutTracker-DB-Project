# Progetto corso Basi di Dati
# Istruzioni sull'avvio dell'applicativo

## Requisiti:
- Java 21+
- MySQL 8.0+

## Contenuto:
- `sql/dbWorkout.sql`: script per la creazione del database e delle tabelle.
- `sql/allQueries.sql`: script per l'inserimento dei dati iniziali.
- `app/`: cartella contenente il codice sorgente Java.

## Creazione e popolamento del database MySQL
1. Avviare MySQL in locale (localhost).
2. Eseguire lo script `sql/dbWorkout.sql` per creare lo schema e le tabelle.
3. Eseguire lo script `sql/allQueries.sql` per inserire i dati di partenza.
4. L'applicazione è configurata per connettersi come utente `root`, senza password.

## Avvio dell'applicazione:
Da terminale, all'interno della cartella `app/`, eseguire:
- Windows: `.\gradlew.bat clean run`
- Linux/Mac: `./gradlew clean run`

Se Gradle non dovesse partire, controllare i permessi di esecuzione (`chmod +x gradlew` su Linux/Mac)