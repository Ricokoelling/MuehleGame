# MuehleGame ♟️

Eine Java-Implementierung des klassischen Brettspiels **Mühle** – entwickelt im Rahmen des Moduls *Fortgeschrittenes Programmier Praktikum* (FPP) an der FSU Jena. Das Spiel unterstützt sowohl Online-Multiplayer als auch einen lokalen Offline-Modus.

> 🏆 Bewertet mit **1,3** – diese Version enthält zusätzlich kleinere Bugfixes gegenüber der Abgabe.

---

## Features

- 🌐 **Online-Multiplayer** – Zwei Spieler verbinden sich über ein Client-Server-Modell
- 🖥️ **Offline-Modus** – Lokales Spiel auf einer Maschine möglich
- 🧠 **Vollständige Spiellogik** – Alle Phasen inkl. Mühlen-Erkennung, Sprung-Phase und Gewinnbedingung
- 🎨 **Swing-GUI** – Grafisches Spielfeld mit Maus-Steuerung

---

## Spielregeln

Das Spiel läuft in mehreren Phasen ab:

1. **Setzphase** – Jeder Spieler platziert abwechselnd 9 Steine auf dem Feld
2. **Zugphase** – Steine werden entlang der Linien bewegt
3. **Sprungphase** – Ein Spieler mit genau 3 Steinen darf auf beliebige freie Felder springen

Bildet ein Spieler eine **Mühle** (3 Steine in einer Reihe), darf er einen Stein des Gegners entfernen.

**Gewonnen** hat, wer den Gegner auf unter 3 Steine reduziert oder alle seine Züge blockiert.

---

## Architektur

```
Clientside/     → GUI (Swing), Spielfeld, Server-Kommunikation, SwingWorker
Serverside/     → Server, Client-Handler
Logic/          → Spiellogik (Mühlen-Erkennung, Phasenverwaltung)
OfflineGame/    → Lokaler Spielmodus (BoardFrame, OfflineBoardPanel)
Data/           → Datenklassen (Stone, PlayData)
```

Der Server verwaltet die Spiellogik zentral und kommuniziert mit beiden Clients über Sockets auf Port `1337`.

---

## Requirements

- Java 18+
- Maven

---

## Build & Run

```bash
# Projekt bauen
mvn package
```

**Online-Modus:**
```bash
# 1. Server starten
java -cp target/Muehle-1.0-SNAPSHOT.jar Serverside.Server

# 2. Spieler 1 starten
java -cp target/Muehle-1.0-SNAPSHOT.jar Main

# 3. Spieler 2 starten
java -cp target/Muehle-1.0-SNAPSHOT.jar Main_two
```

> Der zuerst gestartete Client ist Spieler 1. Namen können direkt im Spiel geändert werden.

**Offline-Modus:**
```bash
java -cp target/Muehle-1.0-SNAPSHOT.jar OfflineGame.BoardFrame
```

---

## Stack

`Java 18` · `Maven` · `Swing` · `Sockets`
