# Tic-Tac-Toe AI

## 1. Project Overview

**Project Title:** Tic-Tac-Toe AI

**Project Type:** Java Desktop Application

**Technology:** Java Swing

Tic-Tac-Toe AI is a desktop game developed using Java and Swing. It allows a player to play Tic-Tac-Toe against a computer opponent with three difficulty levels. The application includes a graphical user interface, scoreboard, game history, and automatic win and draw detection.

The game uses a 3×3 board. The player uses X, while the computer uses O. The objective is to place three matching symbols in a horizontal row, vertical column, or diagonal.

## 2. Project Objectives

- Develop a functional Tic-Tac-Toe game using Java.
- Implement a graphical user interface using Java Swing.
- Demonstrate object-oriented programming concepts.
- Implement a computer opponent with multiple difficulty levels.
- Detect winning combinations and draw conditions.
- Maintain a scoreboard and game history during the current session.
- Provide a simple and user-friendly desktop application.

## 3. Features

### 3.1 Graphical User Interface
The application uses Java Swing to provide a desktop interface with a 3×3 game board, buttons, status messages, and game controls.

### 3.2 AI Difficulty Levels

- **Easy:** The computer selects a random available square.
- **Medium:** The computer attempts to win, blocks immediate player wins, prefers the centre and corners, and selects another available square when necessary.
- **Hard:** The computer uses the Minimax algorithm to evaluate possible moves and select an optimal move.

### 3.3 Scoreboard
The scoreboard tracks player wins, computer wins, and draws during the current application session.

### 3.4 Game History
The history section records completed games, including the game number, selected difficulty, result, and number of moves.

### 3.5 Game Controls
The application provides controls to start a new game, reset scores, and clear the displayed game history.

### 3.6 Win and Draw Detection
The application checks all rows, columns, and diagonals to determine whether the player or computer has won. It also detects draws when the board is full without a winner.

### 3.7 Winning-Line Highlight
When a winning combination is detected, the winning squares are highlighted in the interface.

## 4. Technologies Used

- **Java:** Application logic and game implementation.
- **Java Swing:** Graphical user interface components.
- **Java AWT:** Layouts, colours, fonts, and event handling.
- **Minimax Algorithm:** AI decision-making for Hard difficulty.
- **Java Collections:** Managing available board positions.

The project uses standard Java libraries and does not require an external database or third-party library.

## 5. Project Structure

The project contains the following files:

```text
TicTacToe-AI/
├── Board.java
├── Opponent.java
├── TicTacToeGUI.java
└── README.md
```

### 5.1 Board.java

This class manages the game board and its state. Its responsibilities include:

- Storing the nine board positions.
- Placing X and O symbols.
- Checking whether a square is available.
- Identifying available squares.
- Detecting winning combinations.
- Detecting a full board.
- Returning winning-line positions.
- Creating copies of the board for AI calculations.

### 5.2 Opponent.java

This class implements the computer opponent. It contains the logic for the Easy, Medium, and Hard difficulty levels. Hard mode uses the Minimax algorithm to evaluate possible moves.

### 5.3 TicTacToeGUI.java

This class creates the graphical interface and serves as the application's entry point. It manages the board buttons, difficulty selector, scoreboard, game history, user interactions, and game flow.

### 5.4 README.md

This file contains the project description, features, requirements, compilation instructions, execution commands, and troubleshooting information.

## 6. Requirements

Before running the application, ensure that the following requirements are met:

- Java Development Kit (JDK) version 8 or higher.
- Windows, macOS, or Linux with a graphical desktop environment.
- All three Java source files in the same project folder.
- A terminal or command prompt.

A code editor such as Visual Studio Code or IntelliJ IDEA is optional.

## 7. Checking the Java Installation

Open Windows PowerShell or Command Prompt and execute the following commands.

Check the Java runtime:

```powershell
java -version
```

Check the Java compiler:

```powershell
javac -version
```

Both commands should display version information. If either command is not recognised, install a JDK and configure its `bin` directory in the system PATH.

A JDK can be obtained from Oracle or Eclipse Adoptium.

## 8. Setting Up the Project

1. Create a folder named `TicTacToe-AI`.
2. Place `Board.java`, `Opponent.java`, and `TicTacToeGUI.java` inside the folder.
3. Save this documentation as `README.md` in the same folder.
4. Verify that the source filenames end in `.java`, not `.java.txt`.
5. Open the project folder in File Explorer.

All three Java source files must belong to the same version of the project.

## 9. Opening the Terminal

### Method 1: Open PowerShell from File Explorer

1. Open the project folder.
2. Click the address bar.
3. Type `powershell`.
4. Press Enter.

PowerShell should open in the project directory.

### Method 2: Navigate Using the `cd` Command

If the project folder is inside Downloads, use:

```powershell
cd "$HOME\Downloads\TicTacToe-AI"
```

This command uses the Windows home-directory variable and does not require a personal username.

If the folder is stored elsewhere, replace the example path with the actual project folder path.

To check the files in the current directory, run:

```powershell
dir
```

The output should include:

```text
Board.java
Opponent.java
TicTacToeGUI.java
README.md
```

## 10. Compiling the Project

Once the terminal is open in the project folder, execute:

```powershell
javac Board.java Opponent.java TicTacToeGUI.java
```

The Java compiler compiles the source files and generates the required `.class` files.

If compilation succeeds, PowerShell normally returns to the prompt without displaying an error.

If an error appears, check the filename and line number in the compiler message.

## 11. Running the Application

After successful compilation, execute:

```powershell
java TicTacToeGUI
```

The Tic-Tac-Toe AI application window should open.

### Compile and Run Using One Command Sequence

You can compile and launch the application using:

```powershell
javac Board.java Opponent.java TicTacToeGUI.java
if ($LASTEXITCODE -eq 0) { java TicTacToeGUI }
```

The application launches only if compilation succeeds.

## 12. How to Play

1. Launch the application.
2. Select the desired AI difficulty.
3. Click an empty square to place X.
4. The computer responds by placing O.
5. Continue playing until one side wins or the game ends in a draw.
6. Review the result in the status area, scoreboard, and history section.
7. Select **New Game** to begin another match.
8. Use **Reset Scores** to reset the scoreboard totals.
9. Use **Clear History** to remove the displayed game history.

The player uses X, and the computer uses O.

## 13. How the AI Works

### Easy Difficulty

The computer selects a random square from the available positions.

### Medium Difficulty

The computer follows a rule-based strategy. It attempts to win when possible, blocks the player's immediate winning move, prefers the centre and corners, and otherwise selects an available square.

### Hard Difficulty

The computer uses the Minimax algorithm to evaluate possible future game states. It chooses an optimal move, making it impossible for a player to force a win in standard Tic-Tac-Toe when the algorithm is implemented correctly.

With optimal play from both sides, the game can end in a draw.

## 14. Testing Checklist

The following tests should be performed before the project is submitted or shared:

- [ ] The source files compile without errors.
- [ ] The application window opens successfully.
- [ ] The player can place X in an empty square.
- [ ] The computer responds with O.
- [ ] All three difficulty levels work.
- [ ] Occupied squares cannot be played again.
- [ ] Player wins are detected correctly.
- [ ] Computer wins are detected correctly.
- [ ] Draws are detected correctly.
- [ ] No moves occur after the game ends.
- [ ] New Game resets the board correctly.
- [ ] Scoreboard totals update correctly.
- [ ] Completed games appear in the history section.
- [ ] Reset Scores resets the totals.
- [ ] Clear History removes the displayed history.

## 15. Troubleshooting

### Error: `javac is not recognized`

The JDK may not be installed, or its `bin` directory may not be included in the system PATH. Install a JDK, reopen the terminal, and check the installation again.

### Error: `file not found: Board.java`

The terminal may be open in the wrong directory. Run `dir` and navigate to the folder containing the Java source files.

### Error: `cannot find symbol`

Ensure that `Board.java`, `Opponent.java`, and `TicTacToeGUI.java` are from the same project version and that all required methods and classes are present.

### The application does not open

Verify that compilation completed successfully. Run `java TicTacToeGUI` from the same directory. A graphical desktop environment is required to display the Swing interface.

### Source changes do not appear

Save the modified Java files, close the old application window, recompile the project, and run it again:

```powershell
javac Board.java Opponent.java TicTacToeGUI.java
java TicTacToeGUI
```

## 16. Important Notes

- The game is a single-player desktop application against a computer opponent.
- The scoreboard and history are stored in memory and are not saved after the application closes.
- Generated `.class` files do not need to be included when sharing the source project.
- Share the three `.java` files and this README with anyone who needs to compile the application.
- If new Java source files are added, include them in the compilation command.
- Only features that are implemented in the final source code should be described as completed features.
- Before submitting the project, test the complete application and ensure the source code follows the college's project and originality requirements.

## 17. Conclusion

Tic-Tac-Toe AI demonstrates the development of a Java desktop application using Swing, object-oriented programming, game logic, and AI decision-making. The application combines a graphical interface, multiple difficulty levels, win and draw detection, a scoreboard, and session-based game history in a standalone project.
