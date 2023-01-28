import java.util.*;

public class App {
    
public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(System.in);
    Board game = new Board();
    do{
        System.out.print("Enter i : ");
        int x = scan.nextInt();
        System.out.print("Enter j : ");
        int y = scan.nextInt();
        game.putMark(x, y);
        System.out.println("User's move : " );
        game.printBoard();
        game.calculateComputerMoov();
        double bestScore = Double.NEGATIVE_INFINITY;
        Board bestMove = null;
        
        for (Board child : game.children) {
            double currentScore = ratio(child);
            if(currentScore > bestScore){
                bestScore = currentScore;
                bestMove = child;
            }
            if(child.winner() == Board.O){
                bestMove = child;
                break;
            }
            
        }
        if(bestMove != null){
            game = bestMove;
            System.out.println("Computer's move : ");
            game.printBoard();
        }
        
    
    }while(game.winner()== 0 && game.children.size() > 0);
    }
    public static double ratio(Board board){
        if(board.countO == 0 && board.countX == 0){
            return 0;
        }
        return (double)board.countO/(board.countX + board.countO);
    }
}

class Board {
public static final int X = 1, O = -1, EMPTY = 0;
private int[][] boardArray = new int[3][3];
private int player;
public int countX = 0, countO = 0;
private Board parent;
public LinkedList<Board> children;

Board() {
    clearBoard();
    this.children = new LinkedList<>();
}

public void calculateComputerMoov() throws Exception{
    this.children.clear();
    for (int i = 0; i < boardArray.length; i++) {
        for (int j = 0; j < boardArray[0].length; j++) {
            if(boardArray[i][j] == EMPTY){
                Board clone = cloneBoard();
                clone.parent = this;
                clone.putMark(i, j);
                this.children.add(clone);
                int winnerPlayer = winner();
                    if(winnerPlayer == 0){
                        clone.calculateComputerMoov();
                    }
                    else{
                        Board temp = clone;
                        while(temp.parent != null){
                            temp = temp.parent;
                            if(temp.parent == null){
                                break;
                            }else if (winnerPlayer == X){
                                temp.countX++;
                            }else if(winnerPlayer == O){
                                temp.countO++;
                            }
                            
                        }
                    }

            }
        }
    }
}


public void printWinners(){
    System.out.println(
            "Possible moves : " + children.size()+
            " X : " + countX+
            " O : "+ countO);
            this.printBoard();
    for (Board child : children) {
        child.printWinners();
    }
}

public void printChildren(){
    printBoard();
    for (Board child : children) {
        child.printChildren();
    }
}

public Board cloneBoard() throws Exception{
    Board clone = new Board();
    for (int i = 0; i < boardArray.length; i++) {
        for (int j = 0; j < boardArray[0].length; j++) {
            clone.boardArray[i][j] = this.boardArray[i][j];
            
        }
    }
    clone.player = this.player;
    return clone;
}

public void clearBoard() {
    for (int i = 0; i < boardArray.length; i++) {
        for (int j = 0; j < boardArray[0].length; j++) {
            boardArray[i][j] = EMPTY;
        }
    }
    player = X;
}

public void printBoard() {
    for (int i = 0; i < boardArray.length; i++) {
        for (int j = 0; j < boardArray[0].length; j++) {
            if (boardArray[i][j] == 1)
                System.out.print("X  ");
            else if (boardArray[i][j] == -1)
                System.out.print("O  ");
            else
                System.out.print("-  ");
        }
        System.out.println();
    }
    System.out.println();
}

public boolean isWin(int a) {
    if (boardArray[0][0] + boardArray[0][1] + boardArray[0][2] == 3 * a) {
        return true;
    } else if (boardArray[1][0] + boardArray[1][1] + boardArray[1][2] == 3 * a) {
        return true;
    } else if (boardArray[2][0] + boardArray[2][1] + boardArray[2][2] == 3 * a) {
        return true;
    } else if (boardArray[0][0] + boardArray[1][0] + boardArray[2][0] == 3 * a) {
        return true;
    } else if (boardArray[0][1] + boardArray[1][1] + boardArray[2][1] == 3 * a) {
        return true;
    } else if (boardArray[0][2] + boardArray[1][2] + boardArray[2][2] == 3 * a) {
        return true;
    } else if (boardArray[0][0] + boardArray[1][1] + boardArray[2][2] == 3 * a) {
        return true;
    } else if (boardArray[2][0] + boardArray[1][1] + boardArray[0][2] == 3 * a) {
        return true;
    } else
        return false;

}

public void putMark(int i, int j) throws IllegalAccessException {
    if (i < 0 || i > 2 || j < 0 || j > 2)
        throw new IllegalArgumentException("Invalid board position!");
    if (boardArray[i][j] != EMPTY)
        throw new IllegalArgumentException("Board position occupied!");
    boardArray[i][j] = player;
    player = -player;
}

public int winner() {
    if (isWin(X))
        return (X);
    else if (isWin(O))
        return (O);
    else
        return (0);

    }
}


