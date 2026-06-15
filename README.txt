============================================================
 FITNESS TRACKER - Progetto Basi di Dati A.A. 2025/2026
 Gruppo 2954 - Federico Locatelli (matr. 0001160887)
============================================================

DESCRIZIONE
-----------
Applicazione desktop Java (Swing) per la gestione delle routine di
allenamento e il monitoraggio delle performance atletiche.
Architettura MVC + DAO, database MySQL.

REQUISITI
---------
- Java 21 o superiore
- MySQL 8.0 o superiore
- Gradle: NON serve installarlo, e' incluso tramite wrapper (gradlew)

STRUTTURA
---------
  sql/schema.sql       -> crea schema e tabelle
  sql/query.sql        -> crea il trigger (AggiornaVolumeTotaleSessione)
  sql/population.sql   -> inserisce i dati di esempio
  src/main/java/...    -> codice sorgente (model, controller, view)
  src/test/java/...    -> test di integrazione JUnit 5
  build.gradle.kts     -> configurazione Gradle
  gradlew / gradlew.bat-> wrapper per l'avvio

1) CREAZIONE E POPOLAMENTO DEL DATABASE
---------------------------------------
Avviare MySQL in locale (default localhost:3306), poi eseguire i tre
script NELL'ORDINE indicato:

  mysql -u root -p < sql/schema.sql
  mysql -u root -p < sql/query.sql
  mysql -u root -p < sql/population.sql

(su macOS, se "mysql" non e' nel PATH, usare il percorso completo, es.
 /usr/local/mysql/bin/mysql -u root -p < sql/schema.sql)

Gli script creano e popolano il database "WorkoutDB".

2) CONFIGURAZIONE DELLA CONNESSIONE
-----------------------------------
I parametri di connessione si trovano in:
  src/main/java/it/unibo/workout/model/DatabaseConnection.java

Di default l'app si collega come utente "root" su
localhost:3306/WorkoutDB. Se la propria installazione MySQL usa
credenziali diverse (utente o password), modificarle in quel file.

3) AVVIO DELL'APPLICAZIONE
--------------------------
Da terminale, nella cartella radice del progetto:

  Linux/macOS:  ./gradlew clean run
  Windows:      .\gradlew.bat clean run

Se su Linux/macOS Gradle non parte per i permessi:
  chmod +x gradlew

4) TEST (opzionale)
-------------------
I test sono di integrazione: richiedono MySQL attivo e il database gia'
creato e popolato. Per eseguirli:
  ./gradlew build

CREDENZIALI DI ESEMPIO
----------------------
(password in chiaro solo a scopo dimostrativo)

  Ruolo   Email                          Password
  ------  -----------------------------  ---------
  Coach   mario.rossi@unibo.it           coach123
  Coach   giulia.ferrari@unibo.it        coach456
  Atleta  federico.locatelli@unibo.it    fede123
  Atleta  luca.bianchi@unibo.it          luca456

All'avvio si seleziona il ruolo (Atleta o Coach) e si accede con email
e password. E' possibile registrare nuovi profili dalla schermata di
login che si aggiungeranno man mano in automatico al DB.

CODICE SEGRETO COACH
--------------------
Per accedere o registrarsi come Coach e' richiesto il codice: 020405
============================================================
