import java.util.Scanner;

// Base class for all chess pieces
abstract class Piece {
    boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public abstract boolean isValidMove(ChessBoard board, int startX, int startY, int endX, int endY);

    @Override
    public String toString() {
        return isWhite ? "W" : "B";
    }
}

// Pawn class
class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessBoard board, int startX, int startY, int endX, int endY) {
        int direction = isWhite ? -1 : 1; // White moves up, Black moves down
        Piece target = board.getPiece(endX, endY);
        
        // Normal move forward
        if (startY == endY && board.getPiece(endX, endY) == null) {
            if ((startX + direction == endX)) {
                return true; // Single move forward
            }
            if ((startX + 2 * direction == endX) && (startX == (isWhite ? 6 : 1))) {
                return true; // Double move forward
            }
        }
        // Capture
        if (Math.abs(startY - endY) == 1 && startX + direction == endX && target != null && target.isWhite != isWhite) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "P";
    }
}

// Rook class
class Rook extends Piece {
    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessBoard board, int startX, int startY, int endX, int endY) {
        if (startX != endX && startY != endY) return false; // Must move in straight line

        if (startX == endX) { // Horizontal move
            for (int i = Math.min(startY, endY) + 1; i < Math.max(startY, endY); i++) {
                if (board.getPiece(startX, i) != null) return false; // Path is blocked
            }
        } else { // Vertical move
            for (int i = Math.min(startX, endX) + 1; i < Math.max(startX, endX); i++) {
                if (board.getPiece(i, startY) != null) return false; // Path is blocked
            }
        }
        return board.getPiece(endX, endY) == null || board.getPiece(endX, endY).isWhite != this.isWhite;
    }

    @Override
    public String toString() {
        return super.toString() + "R";
    }
}

// Knight class
class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessBoard board, int startX, int startY, int endX, int endY) {
        int xDiff = Math.abs(startX - endX);
        int yDiff = Math.abs(startY - endY);
        if (xDiff == 2 && yDiff == 1 || xDiff == 1 && yDiff == 2) {
            Piece target = board.getPiece(endX, endY);
            return target == null || target.isWhite != this.isWhite;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "N";
    }
}

// Bishop class
class Bishop extends Piece {
    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessBoard board, int startX, int startY, int endX, int endY) {
        if (Math.abs(startX - endX) != Math.abs(startY - endY)) return false; // Must move diagonally

        int xDirection = (endX - startX) > 0 ? 1 : -1;
        int yDirection = (endY - startY) > 0 ? 1 : -1;

        for (int i = 1; i < Math.abs(startX - endX); i++) {
            if (board.getPiece(startX + i * xDirection, startY + i * yDirection) != null) {
                return false; // Path is blocked
            }
        }
        return board.getPiece(endX, endY) == null || board.getPiece(endX, endY).isWhite != this.isWhite;
    }

    @Override
    public String toString() {
        return super.toString() + "B";
    }
}

// Queen class
class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessBoard board, int startX, int startY, int endX, int endY) {
        // Queen is a combination of Rook and Bishop
        return new Rook(isWhite).isValidMove(board, startX, startY, endX, endY) ||
               new Bishop(isWhite).isValidMove(board, startX, startY, endX, endY);
    }

    @Override
    public String toString() {
        return super.toString() + "Q";
    }
}

// King class
class King extends Piece {
    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(ChessBoard board, int startX, int startY, int endX, int endY) {
        int xDiff = Math.abs(startX - endX);
        int yDiff = Math.abs(startY - endY);

        // King moves one square in any direction
        if (xDiff <= 1 && yDiff <= 1) {
            return board.getPiece(endX, endY) == null || board.getPiece(endX, endY).isWhite != this.isWhite;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "K";
    }
}

// ChessBoard class to hold the pieces
class ChessBoard {
    private Piece[][] board = new Piece[8][8];

    public ChessBoard() {
        // Initialize the board with pieces
        setupPieces(true);  // White pieces
        setupPieces(false); // Black pieces
    }

    private void setupPieces(boolean isWhite) {
        int row = isWhite ? 7 : 0;
        int pawnRow = isWhite ? 6 : 1;

        board[row][0] = new Rook(isWhite);
        board[row][1] = new Knight(isWhite);
        board[row][2] = new Bishop(isWhite);
        board[row][3] = new Queen(isWhite);
        board[row][4] = new King(isWhite);
        board[row][5] = new Bishop(isWhite);
        board[row][6] = new Knight(isWhite);
        board[row][7] = new Rook(isWhite);

        for (int i = 0; i < 8; i++) {
            board[pawnRow][i] = new Pawn(isWhite);
        }
    }

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public void movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = board[startX][startY];
        board[endX][endY] = piece;
        board[startX][startY] = null;
    }

    public void displayBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    System.out.print(board[i][j] + " ");
                } else {
                    System.out.print("-- ");
                }
            }
            System.out.println();
        }
    }

    public boolean isValidMove(int startX, int startY, int endX, int endY, boolean isWhiteTurn) {
        Piece piece = getPiece(startX, startY);
        if (piece == null || piece.isWhite != isWhiteTurn) {
            return false;
        }
        return piece.isValidMove(this, startX, startY, endX, endY);
    }
}

// Main class to handle the game loop
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private boolean isWhiteTurn = true;

    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            board.displayBoard();
            System.out.println((isWhiteTurn ? "White" : "Black") + "'s turn");
            System.out.println("Enter your move (e.g., e2 e4): ");
            String move = scanner.nextLine();
            String[] positions = move.split(" ");

            int startX = 8 - Character.getNumericValue(positions[0].charAt(1));
            int startY = positions[0].charAt(0) - 'a';
            int endX = 8 - Character.getNumericValue(positions[1].charAt(1));
            int endY = positions[1].charAt(0) - 'a';

            if (board.isValidMove(startX, startY, endX, endY, isWhiteTurn)) {
                board.movePiece(startX, startY, endX, endY);
                isWhiteTurn = !isWhiteTurn;  // Switch turns
            } else {
                System.out.println("Invalid move, try again.");
            }
        }
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        game.playGame();
    }
}
