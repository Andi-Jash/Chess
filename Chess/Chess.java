import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Chess extends JFrame {
    private Container contents;
    private JButton[][] squares = new JButton[8][8];
    private Color colorLight = new Color(245, 245, 220);
    private Color colorDark = new Color(139, 69, 19);
    private int selectedRow = -1;
    private int selectedCol = -1;
    private String currentPlayer = "white"; 
    private ImageIcon whiteKnight;
    private ImageIcon blackKnight;
    private ImageIcon whiteBishop;
    private ImageIcon blackBishop;
    private ImageIcon blackPawn;
    private ImageIcon whitePawn;
    private ImageIcon whiteRook;
    private ImageIcon blackRook;
    private ImageIcon whiteQueen;
    private ImageIcon blackQueen;
    private ImageIcon whiteKing;
    private ImageIcon blackKing;

    private Piece[][] board = new Piece[8][8];
    private Black blackPieces = new Black();
    private White whitePieces = new White();

    public Chess() {
        contents = getContentPane();
        contents.setLayout(new GridLayout(8, 8));

        Image icon = Toolkit.getDefaultToolkit().getImage("images/chess.png");
        setIconImage(icon);

        
        whiteKnight = loadAndScaleIcon("images/white_knight.png");
        blackKnight = loadAndScaleIcon("images/black_knight.png");
        whiteBishop = loadAndScaleIcon("images/white_bishop.png");
        blackBishop = loadAndScaleIcon("images/black_bishop.png");
        blackPawn = loadAndScaleIcon("images/black_pawn.png");
        whitePawn = loadAndScaleIcon("images/white_pawn.png");
        whiteRook = loadAndScaleIcon("images/white_rook.png");
        blackRook = loadAndScaleIcon("images/black_rook.png");
        whiteQueen = loadAndScaleIcon("images/white_queen.png");
        blackQueen = loadAndScaleIcon("images/black_queen.png");
        whiteKing = loadAndScaleIcon("images/white_king.png");
        blackKing = loadAndScaleIcon("images/black_king.png");

        initializeBoard();

        ButtonHandler buttonHandler = new ButtonHandler();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new JButton();
                if ((i + j) % 2 == 0) {
                    squares[i][j].setBackground(colorLight);
                } else {
                    squares[i][j].setBackground(colorDark);
                }
                squares[i][j].setPreferredSize(new Dimension(60, 60));
                contents.add(squares[i][j]);
                squares[i][j].addActionListener(buttonHandler);
            }
        }

        updateBoardDisplay();

        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private ImageIcon loadAndScaleIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        return scaleImageIcon(icon, 60, 60);
    }

    private ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
        blackPieces.initialize(board);
        whitePieces.initialize(board);
    }

    private void updateBoardDisplay() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    Piece piece = board[i][j];
                    switch (piece.getType()) {
                        case "knight":
                            squares[i][j].setIcon(piece.getColor().equals("black") ? blackKnight : whiteKnight);
                            break;
                        case "bishop":
                            squares[i][j].setIcon(piece.getColor().equals("black") ? blackBishop : whiteBishop);
                            break;
                        case "pawn":
                            squares[i][j].setIcon(piece.getColor().equals("black") ? blackPawn : whitePawn);
                            break;
                        case "rook":
                            squares[i][j].setIcon(piece.getColor().equals("black") ? blackRook : whiteRook);
                            break;
                        case "queen":
                            squares[i][j].setIcon(piece.getColor().equals("black") ? blackQueen : whiteQueen);
                            break;
                        case "king":
                            squares[i][j].setIcon(piece.getColor().equals("black") ? blackKing : whiteKing);
                            break;
                    }
                } else {
                    squares[i][j].setIcon(null);
                }
                squares[i][j].setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }

    private boolean isValidMove(int i, int j) {
        if (selectedRow == -1 || selectedCol == -1) {
            return false;
        }
    
        Piece selectedPiece = board[selectedRow][selectedCol];
        switch (selectedPiece.getType()) {
            case "knight":
                return selectedPiece.getColor().equals("white") ? whitePieces.isValidKnightMove(i, j, selectedRow, selectedCol)
                                                                : blackPieces.isValidKnightMove(i, j, selectedRow, selectedCol);
            case "bishop":
                return selectedPiece.getColor().equals("white") ? whitePieces.isValidBishopMove(i, j, selectedRow, selectedCol)
                                                                : blackPieces.isValidBishopMove(i, j, selectedRow, selectedCol);
            case "pawn":
                return selectedPiece.getColor().equals("white") ? whitePieces.isValidPawnMove(i, j, selectedRow, selectedCol, board)
                                                                : blackPieces.isValidPawnMove(i, j, selectedRow, selectedCol, board);
            case "rook":
                return selectedPiece.getColor().equals("white") ? whitePieces.isValidRookMove(i, j, selectedRow, selectedCol, board)
                                                                : blackPieces.isValidRookMove(i, j, selectedRow, selectedCol, board);
            case "queen":
                return selectedPiece.getColor().equals("white") ? whitePieces.isValidQueenMove(i, j, selectedRow, selectedCol, board)
                                                                : blackPieces.isValidQueenMove(i, j, selectedRow, selectedCol, board);
            case "king":
                return selectedPiece.getColor().equals("white") ? whitePieces.isValidKingMove(i, j, selectedRow, selectedCol, board)
                                                                : blackPieces.isValidKingMove(i, j, selectedRow, selectedCol, board);
            default:
                return false;
        }
    }
    

    private boolean isKingInCheck() {
        int kingRow = -1;
        int kingCol = -1;
    
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.getType().equals("king") && piece.getColor().equals(currentPlayer)) {
                    kingRow = i;
                    kingCol = j;
                    break;
                }
            }
            if (kingRow != -1) break;
        }
    
        if (kingRow == -1) {
            throw new RuntimeException("King not found for player " + currentPlayer);
        }
    
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece attackingPiece = board[i][j];
                if (attackingPiece != null && !attackingPiece.getColor().equals(currentPlayer)) {
                    Player opponent = attackingPiece.getColor().equals("black") ? blackPieces : whitePieces;
    
                    switch (attackingPiece.getType()) {
                        case "knight":
                            if (opponent.isValidKnightMove(kingRow, kingCol, i, j)) return true;
                            break;
                        case "bishop":
                            if (opponent.isValidBishopMove(kingRow, kingCol, i, j)) return true;
                            break;
                        case "rook":
                            if (opponent.isValidRookMove(kingRow, kingCol, i, j, board)) return true;
                            break;
                        case "queen":
                            if (opponent.isValidQueenMove(kingRow, kingCol, i, j, board)) return true;
                            break;
                        case "pawn":
                            if (opponent.isValidPawnMove(kingRow, kingCol, i, j, board)) return true;
                            break;
                    }
                }
            }
        }
    
        return false;
    }
    


    private boolean canKingMoveToSafety() {
        int kingRow = -1;
        int kingCol = -1;
    
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.getType().equals("king") && piece.getColor().equals(currentPlayer)) {
                    kingRow = i;
                    kingCol = j;
                    break;
                }
            }
            if (kingRow != -1) break;
        }
    
        if (kingRow == -1) {
            throw new RuntimeException("King not found for player " + currentPlayer);
        }
    
        
        for (int i = kingRow - 1; i <= kingRow + 1; i++) {
            for (int j = kingCol - 1; j <= kingCol + 1; j++) {
                if (i >= 0 && i < 8 && j >= 0 && j < 8 && (i != kingRow || j != kingCol)) {
                    Piece targetPiece = board[i][j];
                    if (targetPiece == null || !targetPiece.getColor().equals(currentPlayer)) {
                        
                        Piece originalPiece = board[i][j];
                        board[i][j] = board[kingRow][kingCol];
                        board[kingRow][kingCol] = null;
    
                        
                        boolean isInCheck = isKingInCheck();
    
                        
                        board[kingRow][kingCol] = board[i][j];
                        board[i][j] = originalPiece;
    
                        if (!isInCheck) {
                            return true;
                        }
                    }
                }
            }
        }
    
        return false;
    }
    
    
    
    
    private void processClick(int i, int j) {
        if (selectedRow == i && selectedCol == j) {
            squares[selectedRow][selectedCol].setBorder(BorderFactory.createEmptyBorder());
            selectedRow = -1;
            selectedCol = -1;
            return;
        }
    
        if (selectedRow == -1 || selectedCol == -1) {
            if (board[i][j] != null) {
                Piece piece = board[i][j];
                if (piece.getColor().equals(currentPlayer)) {
                    if (selectedRow != -1 && selectedCol != -1) {
                        squares[selectedRow][selectedCol].setBorder(BorderFactory.createEmptyBorder());
                    }
    
                    selectedRow = i;
                    selectedCol = j;
                    squares[i][j].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                    return;
                }
            }
        } else {
            if (!isValidMove(i, j)) {
                return;
            }
    
            Piece movingPiece = board[selectedRow][selectedCol];
            Piece targetPiece = board[i][j];
    
            // Perform the move
            board[i][j] = movingPiece;
            board[selectedRow][selectedCol] = null;
    
            // Check if the king is in check after the move
            boolean isInCheckAfterMove = isKingInCheck();
            if (isInCheckAfterMove) {
                // Undo the move
                board[selectedRow][selectedCol] = movingPiece;
                board[i][j] = targetPiece;
    
                JOptionPane.showMessageDialog(this, "Invalid move: King is in check.");
                selectedRow = -1;
                selectedCol = -1;
                updateBoardDisplay();
                return;
            }
    
            // Finalize the move
            board[i][j] = movingPiece;
            board[selectedRow][selectedCol] = null;
            selectedRow = -1;
            selectedCol = -1;
    
            updateBoardDisplay();
    
            if (isKingInCheck()) {
                if (canKingMoveToSafety()) {
                    JOptionPane.showMessageDialog(this, "Your king is in check, but can move to safety.");
                } else {
                    JOptionPane.showMessageDialog(this, "Checkmate! Your king is in check and cannot move to safety.");
                    return;
                }
            } else {
                switchPlayer();
            }
        }
    }
        
    

    private void switchPlayer() {
        currentPlayer = currentPlayer.equals("white") ? "black" : "white";
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (source == squares[i][j]) {
                        processClick(i, j);
                        return;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Chess::new);
    }

    private class Piece {
        private String type;
        private String color;

        public Piece(String type, String color) {
            this.type = type;
            this.color = color;
        }

        public String getType() {
            return type;
        }

        public String getColor() {
            return color;
        }
    }

    private abstract class Player {
        public abstract void initialize(Piece[][] board);

        public abstract boolean isValidKnightMove(int i, int j, int startRow, int startCol);

        public abstract boolean isValidBishopMove(int i, int j, int startRow, int startCol);

        public abstract boolean isValidPawnMove(int i, int j, int startRow, int startCol, Piece[][] board);

        public abstract boolean isValidRookMove(int i, int j, int startRow, int startCol, Piece[][] board);

        public abstract boolean isValidQueenMove(int i, int j, int startRow, int startCol, Piece[][] board);

        public abstract boolean isValidKingMove(int i, int j, int startRow, int startCol, Piece[][] board);
    }

    private class Black extends Player {
        public void initialize(Piece[][] board) {
            
            for (int j = 0; j < 8; j++) {
                board[1][j] = new Piece("pawn", "black");
            }
            
            board[0][0] = new Piece("rook", "black");
            board[0][1] = new Piece("knight", "black");
            board[0][2] = new Piece("bishop", "black");
            board[0][3] = new Piece("queen", "black");
            board[0][4] = new Piece("king", "black");
            board[0][5] = new Piece("bishop", "black");
            board[0][6] = new Piece("knight", "black");
            board[0][7] = new Piece("rook", "black");
        }

        public boolean isValidKnightMove(int i, int j, int startRow, int startCol) {
            int rowDelta = Math.abs(i - startRow);
            int colDelta = Math.abs(j - startCol);
            boolean isValidMove = (rowDelta == 1 && colDelta == 2) || (rowDelta == 2 && colDelta == 1);
            return isValidMove && (board[i][j] == null || !board[i][j].getColor().equals(board[startRow][startCol].getColor()));
        }        
        

        public boolean isValidBishopMove(int i, int j, int startRow, int startCol) {
            if (Math.abs(i - startRow) == Math.abs(j - startCol)) {
                int RowDirection = (i - startRow) > 0 ? 1 : -1;
                int ColDirection = (j - startCol) > 0 ? 1 : -1;
                int row = startRow + RowDirection;
                int col = startCol + ColDirection;
                while (row != i && col != j) {
                    if (board[row][col] != null) {
                        return false;
                    }
                    row += RowDirection;
                    col += ColDirection;
                }
                
                return board[i][j] == null || !board[i][j].getColor().equals(board[startRow][startCol].getColor());
            }
            return false;
        }
        

        public boolean isValidPawnMove(int i, int j, int startRow, int startCol, Piece[][] board) {
            int direction = 1; 
            Piece pawn = board[startRow][startCol];

            if (j == startCol && board[i][j] == null) {
                if ((i == startRow + direction && board[i][j] == null) ||
                    (i == startRow + 2 * direction && startRow == 1 && board[i][j] == null)) {
                    return true;
                }
            }

            if (Math.abs(j - startCol) == 1 && i == startRow + direction && board[i][j] != null &&
                !board[i][j].getColor().equals(pawn.getColor())) {
                return true;
            }

            return false;
        }

        public boolean isValidRookMove(int i, int j, int startRow, int startCol, Piece[][] board) {
            if (i == startRow || j == startCol) {
                int rowDirection = Integer.signum(i - startRow);
                int colDirection = Integer.signum(j - startCol);
                int row = startRow + rowDirection;
                int col = startCol + colDirection;
        
                while (row != i || col != j) {
                    if (board[row][col] != null) {
                        return false; // Blocked by another piece
                    }
                    row += rowDirection;
                    col += colDirection;
                }
        
                return board[i][j] == null || !board[i][j].getColor().equals(board[startRow][startCol].getColor());
            }
            return false;
        }
        
        public boolean isValidQueenMove(int i, int j, int startRow, int startCol, Piece[][] board) {
            return isValidRookMove(i, j, startRow, startCol, board) || isValidBishopMove(i, j, startRow, startCol);
        }
        

        public boolean isValidKingMove(int i, int j, int startRow, int startCol, Piece[][] board) {

            boolean isMoveInRange = Math.abs(i - startRow) <= 1 && Math.abs(j - startCol) <= 1;
            

            boolean isTargetSquareValid = board[i][j] == null || !board[i][j].getColor().equals(board[startRow][startCol].getColor());
            
            return isMoveInRange && isTargetSquareValid;
        }        
        
    }

    private class White extends Player {
        public void initialize(Piece[][] board) {
            
            for (int j = 0; j < 8; j++) {
                board[6][j] = new Piece("pawn", "white");
            }
            
            board[7][0] = new Piece("rook", "white");
            board[7][1] = new Piece("knight", "white");
            board[7][2] = new Piece("bishop", "white");
            board[7][3] = new Piece("queen", "white");
            board[7][4] = new Piece("king", "white");
            board[7][5] = new Piece("bishop", "white");
            board[7][6] = new Piece("knight", "white");
            board[7][7] = new Piece("rook", "white");
        }

        public boolean isValidKnightMove(int i, int j, int startRow, int startCol) {
            int rowDelta = Math.abs(i - startRow);
            int colDelta = Math.abs(j - startCol);
            boolean isValidMove = (rowDelta == 1 && colDelta == 2) || (rowDelta == 2 && colDelta == 1);
            return isValidMove && (board[i][j] == null || !board[i][j].getColor().equals(board[startRow][startCol].getColor()));
        }
        

        public boolean isValidBishopMove(int i, int j, int startRow, int startCol) {
            if (Math.abs(i - startRow) == Math.abs(j - startCol)) {
                int RowDirection = (i - startRow) > 0 ? 1 : -1;
                int ColDirection = (j - startCol) > 0 ? 1 : -1;
                int row = startRow + RowDirection;
                int col = startCol + ColDirection;
                while (row != i && col != j) {
                    if (board[row][col] != null) {
                        return false;
                    }
                    row += RowDirection;
                    col += ColDirection;
                }
                
                return board[i][j] == null || !board[i][j].getColor().equals(board[startRow][startCol].getColor());
            }
            return false;
        }
        

        public boolean isValidPawnMove(int i, int j, int startRow, int startCol, Piece[][] board) {
            int direction = -1; 
            Piece pawn = board[startRow][startCol];

            if (j == startCol && board[i][j] == null) {
                if ((i == startRow + direction && board[i][j] == null) ||
                    (i == startRow + 2 * direction && startRow == 6 && board[i][j] == null)) {
                    return true;
                }
            }

            if (Math.abs(j - startCol) == 1 && i == startRow + direction && board[i][j] != null &&
                !board[i][j].getColor().equals(pawn.getColor())) {
                return true;
            }

            return false;
        }

        public boolean isValidRookMove(int i, int j, int startRow, int startCol, Piece[][] board) {
            if (i == startRow || j == startCol) {
                int rowDirection = Integer.signum(i - startRow);
                int colDirection = Integer.signum(j - startCol);
                int row = startRow + rowDirection;
                int col = startCol + colDirection;
        
                while (row != i || col != j) {
                    if (board[row][col] != null) {
                        return false; // Blocked by another piece
                    }
                    row += rowDirection;
                    col += colDirection;
                }
        
                return board[i][j] == null || !board[i][j].getColor().equals(board[startRow][startCol].getColor());
            }
            return false;
        }
        
        public boolean isValidQueenMove(int i, int j, int startRow, int startCol, Piece[][] board) {
            return isValidRookMove(i, j, startRow, startCol, board) || isValidBishopMove(i, j, startRow, startCol);
        }
        

        public boolean isValidKingMove(int i, int j, int startRow, int startCol, Piece[][] board) {

            boolean isMoveInRange = Math.abs(i - startRow) <= 1 && Math.abs(j - startCol) <= 1;
            
            boolean isTargetSquareValid = board[i][j] == null || !board[i][j].getColor().equals(board[startRow][startCol].getColor());
            
            return isMoveInRange && isTargetSquareValid;
        }        
        
    }
}
