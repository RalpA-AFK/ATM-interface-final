# ATM Interface — Java Capstone Project

The final project will be the implementation of the UI and functionality of an ATM machine as shown in Web-based Chapters 33-34 (here).

This project is a capstone project that makes use of all topics you have learned in Java since CS-111, including object-oriented programming, inheritance, polymorphism, database connectivity, gui and more.

Most of this project is a self-study, so, in addition to the given requirements, you need to implement one more feature (not discussed in the book).

---

## Project Intent

This implementation builds a fully interactive ATM machine simulator using Java Swing (JFrame). The interface mimics the look and feel of a real physical ATM:

- A **welcome screen** displays a drawn ATM machine image. A **"Scan Card"** button in the top-right corner starts the session.
- After scanning, the user enters their **account number**, then their **PIN** on an on-screen numeric keypad.
- The main **ATM interface** has a center screen flanked by physical-style **arrow buttons on both sides** (outside the screen area), matching the layout of a real ATM where side buttons correspond to on-screen menu labels.
- From the menu, the user can **Withdraw**, **Deposit**, **Check Balance**, or **View Transaction History**.
- Withdrawals display a **$2.25 vendor fee warning** before proceeding.

### User Accounts

Three hardcoded user profiles are available for testing:

| Account Number | PIN  | Starting Balance |
|----------------|------|-----------------|
| 1001           | 1234 | $1,000.00       |
| 1002           | 5678 | $750.00         |
| 1003           | 9012 | $2,500.00       |

### Extra Feature

**Transaction History** — a "View History" option on the main menu displays a timestamped log of all deposits, withdrawals, and balance checks performed during the session.

### Source Files

```
src/atm/
  ATMApp.java           - entry point
  Account.java          - user data model (account number, PIN, balance, history)
  AccountDatabase.java  - stores all user profiles; handles account lookup
  ATMFrame.java         - main JFrame, layout, side button management
  WelcomePanel.java     - welcome screen with drawn ATM + Scan Card button
  AccountPanel.java     - account number entry screen
  PINPanel.java         - PIN entry with on-screen numeric keypad
  MenuPanel.java        - main menu wired to physical side buttons
  TransactionPanel.java - handles Withdraw, Deposit, Balance flows
  HistoryPanel.java     - scrollable timestamped transaction log
```

### How to Run

```
javac -d out src/atm/*.java
java -cp out atm.ATMApp
```

---

## How to Submit

As before, you will make a walkthrough YouTube video where you describe what you worked on, the new feature(s) you implemented, and how the code runs.

Also, you need to upload only the file(s) that are responsible for the new feature as a TXT file. Alternatively, you can push all your files to GitHub and submit a link to the repo.
