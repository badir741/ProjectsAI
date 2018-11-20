
import java.util.ArrayList;
import java.util.Scanner;

public class TicTacToe {
    byte[][] board;
    class Node{
        byte[][] board = new byte[3][3];
        ArrayList<Node> child = new ArrayList<>();
        Node(byte[][] pBoard){
            for(int i=0;i<3;i++)
                for(int j=0;j<3;j++)
                    this.board[i][j] = pBoard[i][j];
        }
    }
    TicTacToe(){
        board = new byte[][]{{1,2,3},
                            {4,5,6},
                            {7,8,9}};
    }
    void readHumanResponse(){
        Scanner in = new Scanner(System.in);
        for(int i=0;i<3;i++){
            System.out.print(" ");
            for(int j=0;j<3;j++){
                if(board[i][j] == -1)
                    System.out.print("X | ");
                else if(board[i][j] == -2)
                    System.out.print("O | ");
                else
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("---+---+---");
        }
        int response = in.nextInt();
        response--;
        board[response/3][response%3] = -1;
    }
    int valueOfNode(Node p){
        if(p.board[0][0] == -1 && p.board[0][1] == -1 && p.board[0][2] == -1)
            return 1;
        if(p.board[1][0] == -1 && p.board[1][1] == -1 && p.board[1][2] == -1)
            return 1;
        if(p.board[2][0] == -1 && p.board[2][1] == -1 && p.board[2][2] == -1)
            return 1;
        if(p.board[0][0] == -1 && p.board[1][0] == -1 && p.board[2][0] == -1)
            return 1;
        if(p.board[0][1] == -1 && p.board[1][1] == -1 && p.board[2][1] == -1)
            return 1;
        if(p.board[0][2] == -1 && p.board[1][2] == -1 && p.board[2][2] == -1)
            return 1;
        if(p.board[0][0] == -1 && p.board[1][1] == -1 && p.board[2][2] == -1)
            return 1;
        if(p.board[0][2] == -1 && p.board[1][1] == -1 && p.board[2][0] == -1)
            return 1;


        if(p.board[0][0] == -2 && p.board[0][1] == -2 && p.board[0][2] == -2)
            return -1;
        if(p.board[1][0] == -2 && p.board[1][1] == -2 && p.board[1][2] == -2)
            return -1;
        if(p.board[2][0] == -2 && p.board[2][1] == -2 && p.board[2][2] == -2)
            return -1;
        if(p.board[0][0] == -2 && p.board[1][0] == -2 && p.board[2][0] == -2)
            return -1;
        if(p.board[0][1] == -2 && p.board[1][1] == -2 && p.board[2][1] == -2)
            return -1;
        if(p.board[0][2] == -2 && p.board[1][2] == -2 && p.board[2][2] == -2)
            return -1;
        if(p.board[0][0] == -2 && p.board[1][1] == -2 && p.board[2][2] == -2)
            return -1;
        if(p.board[0][2] == -2 && p.board[1][1] == -2 && p.board[2][0] == -2)
            return -1;

        return 0;
    }
    int miniMax(Node p,int depth,Boolean turn){
        if(depth == 0 || p == null || valueOfNode(p) == -1){
            //System.out.println("I am returning " + valueOfNode(p));
            return valueOfNode(p);
        }
        if(turn){
            int value = Integer.MAX_VALUE;
            //int value = valueOfNode(p);
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(p.board[i][j] > 0 ){
                        Node child = new Node(p.board);
                        child.board[i][j] = -2;
                        p.child.add(child);
                }
                }
            }
            for(Node i:p.child){
                value = Math.min(value,miniMax(i,depth-1,false));
            }
            return value;
        }else{
            int value = Integer.MIN_VALUE;
            //int value = valueOfNode(p);
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(p.board[i][j] > 0 ){
                        Node child = new Node(p.board);
                        child.board[i][j] = -1;
                        p.child.add(child);
                    }
                }
            }
            for(Node i:p.child){
                value = Math.max(value,miniMax(i,depth-1,true));
            }
            return value;
        }
    }
    void computeMove(int d){
        int x = -1;
        int y = -1;
        int value = 2;
        //Node b = new Node(board);
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(board[i][j] > 0 ){
                    Node b = new Node(board);
                    b.board[i][j] = -2;
                    int temp = miniMax(b,d,false);
                    //System.out.println(temp + " : " + i +","+j);
                    if(temp < value){
                        x = i;
                        y = j;
                        value = temp;
                    }
                }
            }
        }
        //System.out.println("and: " + value);
        board[x][y] = -2;
    }
    boolean winner(){
        Node w = new Node(board);
        if(valueOfNode(w) == 1){
            System.out.println("You Win!!");
            return false;
        }
        if(valueOfNode(w) == -1){
            System.out.println("Mini-max  Wins!!");
            return false;
        }
        boolean m =false;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(w.board[i][j] > 0)
                    m = true;
            }
        }
        if(valueOfNode(w) == 0 && !m){
            System.out.println("Draw!!");
            return false;
        }

        return true;
    }
    void playTicTacToe(){
        int d = 8;
        System.out.printf("Hello Human! \n" +
                "Let's play some TicTacToe! You go First: \n");
        boolean humanTurn = true;
        while(winner()){
            if(humanTurn){
                readHumanResponse();
            }else{
                computeMove(d);
            }
            d--;
            humanTurn = !humanTurn;
        }
    }
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.playTicTacToe();
    }
}
