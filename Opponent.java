import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Opponent {
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    private Random random = new Random();

    public int getMove(Board board, Difficulty difficulty) {
        List<Integer> available = board.getAvailableSpots();
        if (available.isEmpty()) return -1;

        if (difficulty == Difficulty.EASY) {
            return available.get(random.nextInt(available.size()));
        } else if (difficulty == Difficulty.MEDIUM) {
            return getMediumMove(board);
        } else {
            return getHardMove(board);
        }
    }

    private int getMediumMove(Board board) {
        Board boardCopy = board.copy();
        char computer = board.getComputer();
        char player = board.getPlayer();

        // 1. Can we win?
        int winMove = findWinPosition(boardCopy, computer);
        if (winMove != -1) return winMove;

        // 2. Can we block?
        int blockMove = findWinPosition(boardCopy, player);
        if (blockMove != -1) return blockMove;

        // 3. Take center
        if (boardCopy.spotAvailable(4)) return 4;

        // 4. Take random corner
        List<Integer> corners = new ArrayList<>(java.util.Arrays.asList(0, 2, 6, 8));
        Collections.shuffle(corners);
        for (int corner : corners) {
            if (boardCopy.spotAvailable(corner)) return corner;
        }

        // 5. Random
        List<Integer> available = board.getAvailableSpots();
        return available.get(random.nextInt(available.size()));
    }

    private int findWinPosition(Board board, char piece) {
        List<Integer> available = board.getAvailableSpots();
        Collections.shuffle(available); // Shuffle to prevent predictable blocks/wins if multiple exist
        for (int spot : available) {
            Board copy = board.copy();
            copy.newPiece(piece, spot);
            if (copy.isWinner(piece)) {
                return spot;
            }
        }
        return -1;
    }

    private int getHardMove(Board board) {
        List<Integer> available = board.getAvailableSpots();
        
        // If it's the very first move of the game, randomly pick optimal spots (center or corners)
        if (available.size() == 9) {
            int[] optimalStarts = {0, 2, 4, 6, 8};
            return optimalStarts[random.nextInt(optimalStarts.length)];
        }

        int bestScore = Integer.MIN_VALUE;
        List<Integer> bestMoves = new ArrayList<>();
        
        for (int spot : available) {
            Board copy = board.copy();
            copy.newPiece(board.getComputer(), spot);
            int score = minimax(copy, 0, false);
            
            if (score > bestScore) {
                bestScore = score;
                bestMoves.clear();
                bestMoves.add(spot);
            } else if (score == bestScore) {
                bestMoves.add(spot);
            }
        }
        
        if (!bestMoves.isEmpty()) {
            return bestMoves.get(random.nextInt(bestMoves.size()));
        }
        return -1;
    }

    private int minimax(Board board, int depth, boolean isMaximizing) {
        if (board.isWinner(board.getComputer())) return 10 - depth;
        if (board.isWinner(board.getPlayer())) return depth - 10;
        if (board.getAvailableSpots().isEmpty()) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int spot : board.getAvailableSpots()) {
                Board copy = board.copy();
                copy.newPiece(board.getComputer(), spot);
                int score = minimax(copy, depth + 1, false);
                bestScore = Math.max(score, bestScore);
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int spot : board.getAvailableSpots()) {
                Board copy = board.copy();
                copy.newPiece(board.getPlayer(), spot);
                int score = minimax(copy, depth + 1, true);
                bestScore = Math.min(score, bestScore);
            }
            return bestScore;
        }
    }
}
