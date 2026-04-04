# Smart Parking System (Java OOP Mini Project)

A college-level mini project using Java Swing/AWT for GUI and JDBC with SQLite for persistent storage.

## 1) Project Structure

```text
oop mini project/
├── src/
│   └── com/
│       └── smartparking/
│           ├── Main.java
│           ├── model/
│           │   ├── AbstractVehicle.java
│           │   ├── DashboardStats.java
│           │   ├── ParkingSlot.java
│           │   ├── Vehicle.java
│           │   └── VehicleRecord.java
│           ├── db/
│           │   ├── DatabaseConnection.java
│           │   ├── ParkingSlotDAO.java
│           │   └── VehicleDAO.java
│           ├── service/
│           │   ├── ParkingManager.java
│           │   ├── ParkingResult.java
│           │   ├── RemoveResult.java
│           │   └── ParkingService.java
│           └── gui/
│               └── DashboardFrame.java
├── sql/
│   └── schema.sql
└── README.md
```

## 2) OOP Concepts Used

- Encapsulation: private fields with getters/setters in model classes.
- Inheritance: `Vehicle` extends `AbstractVehicle`; `VehicleRecord` extends `Vehicle`.
- Polymorphism: `getVehicleType()` is abstract in base class and overridden in child classes.
- Abstraction: `ParkingManager` interface hides implementation details from GUI.

## 3) Features Included

- Dashboard shows total, available, occupied slots.
- Park vehicle using vehicle number + owner name.
- Automatic next available slot assignment.
- Prevent duplicate active parking for same vehicle.
- Remove vehicle by vehicle number.
- Slot is freed and exit time updated.
- Billing on exit: INR 20 per hour (minimum 1 hour).
- All records shown in JTable with entry, exit, slot, and fee.
- Data persists in local SQLite DB file: `smart_parking.db`.

## 4) Dependencies

- Java 8+
- SQLite JDBC driver JAR (Xerial): `sqlite-jdbc-<version>.jar`

Download from Maven repository and place jar in a local `lib/` folder.

Example:
- `lib/sqlite-jdbc-3.45.3.0.jar`

## 5) Compile and Run (Windows PowerShell)

From project root:

```powershell
New-Item -ItemType Directory -Force lib
# Put sqlite-jdbc jar inside lib folder manually

New-Item -ItemType Directory -Force out
javac -cp "lib/*" -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })
java -cp "out;lib/*" com.smartparking.Main
```

## 6) How It Works (Brief)

1. `Main` starts app and calls `DatabaseConnection.initializeDatabase(20)`.
2. Database tables are created if missing; default parking slots are inserted.
3. GUI (`DashboardFrame`) calls service methods on button clicks.
4. `ParkingService` applies rules and DAO calls:
   - Finds next free slot
   - Saves vehicle entry
   - Marks slot occupied
   - On removal, calculates bill, updates exit time, frees slot
5. JTable refreshes to show latest database records.

## 7) Notes for Viva

- Project uses layered architecture: GUI -> Service -> DAO -> Database.
- SQL and Java are separated for readability.
- App is intentionally simple and clean for mini project evaluation.
