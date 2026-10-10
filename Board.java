import java.util.ArrayList;
import java.util.List;

public class Board {
    private char[] board = new char[9];
    private char player;
    private char computer;

    public Board(char player, char computer) {
        this.player = player;
        this.computer = computer;
        reset();
    }

    public char getPlayer() { return player; }
    public char getComputer() { return computer; }
    public char[] getBoard() { return board; }
    
    public void reset() {
        for (int i = 0; i < board.length; i++) {
            board[i] = ' ';
        }
    }

    public void newPiece(char piece, int position) {
        if (position >= 0 && position < 9) {
            board[position] = piece;
        }
    }

    public boolean spotAvailable(int position) {
        return position >= 0 && position < 9 && board[position] == ' ';
    }

    public boolean isFull() {
        for (char c : board) {
            if (c == ' ') return false;
        }
        return true;
    }

    public boolean isWinner(char piece) {
        // Rows
        for (int i = 0; i < 9; i += 3) {
            if (board[i] == piece && board[i+1] == piece && board[i+2] == piece) return true;
        }
        // Columns
        for (int i = 0; i < 3; i++) {
            if (board[i] == piece && board[i+3] == piece && board[i+6] == piece) return true;
        }
        // Diagonals
        if (board[0] == piece && board[4] == piece && board[8] == piece) return true;
        if (board[2] == piece && board[4] == piece && board[6] == piece) return true;

        return false;
    }

    public List<Integer> getAvailableSpots() {
        List<Integer> spots = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                spots.add(i);
            }
        }
        return spots;
    }

    public Board copy() {
        Board newBoard = new Board(this.player, this.computer);
        System.arraycopy(this.board, 0, newBoard.board, 0, this.board.length);
        return newBoard;
    }

    public int[] getWinningLine(char piece) {
        for (int i = 0; i < 9; i += 3) {
            if (board[i] == piece && board[i+1] == piece && board[i+2] == piece) return new int[]{i, i+1, i+2};
        }
        for (int i = 0; i < 3; i++) {
            if (board[i] == piece && board[i+3] == piece && board[i+6] == piece) return new int[]{i, i+3, i+6};
        }
        if (board[0] == piece && board[4] == piece && board[8] == piece) return new int[]{0, 4, 8};
        if (board[2] == piece && board[4] == piece && board[6] == piece) return new int[]{2, 4, 6};
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < board.length; i++) {
            sb.append(" ").append(board[i]);
            if (i % 3 == 2 && i != board.length - 1) {
                sb.append("\n---|---|---\n");
            } else if (i != board.length - 1) {
                sb.append(" |");
            }
        }
        return sb.toString();
    }
}
