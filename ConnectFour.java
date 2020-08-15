import java.io.*;
import java.util.*;

class ConnectFour{

    final int gridWidth = 7;
    final int gridHeight = 6;
    final int connect = 4;
    final String p1Char = "X";
    final String p2Char = "O";
    private int[][] grid = new int[gridHeight][gridWidth];
    private int turn = 1;
    private int move = 0;
    private int mode = 0;
    private int comLv = 0;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException{

        ConnectFour c = new ConnectFour();
        c.start();
    }

    public void start() throws IOException{

        // welcome
        resetConsole();
        String symbol = "#";
        System.out.println(symbol.repeat(50));
        System.out.println(symbol.repeat(10) + " WELCOME TO CONNECT FOUR GAME " + symbol.repeat(10));
        System.out.println(symbol.repeat(50));
        // select mode
        System.out.println("Please select game mode:");
        System.out.println("[1] Single-Player");
        System.out.println("[2] Two-Player");
        while (mode<1 || mode>2){
            System.out.print("Input [1-2]: ");
            mode = Integer.parseInt(br.readLine());
        }
        // single-player mode
        if (mode == 1){
            System.out.println("Please select computer level:");
            System.out.println("[1] Easy");
            System.out.println("[2] Medium");
            while (comLv<1 || comLv>2){
                System.out.print("Input [1-2]: ");
                comLv = Integer.parseInt(br.readLine());
            }
            modeSingle(comLv);
        }
        // two-player mode
        else {
            modeTwo();
        }

    }

    private void modeTwo() throws IOException{
        int col;
        while(true){
            showGrid();
            System.out.print("Player(" + pChar(turn) + ")'s turn [1-7]: ");
            col = Integer.parseInt(br.readLine()) - 1;
            if (turn==1){
                if (isValid(col)){
                    putDisc(col, turn);
                    move += 1;
                    if (isEnd()) {
                        break;
                    }
                    turn += 1;
                }
            }
            else {
                if (isValid(col)){
                    putDisc(col, turn);
                    move += 1;
                    if (isEnd()) {
                        break;
                    }
                    turn -= 1;
                }
            }
        }
    }

    private void modeSingle(int lv) throws IOException{
        int col;
        while(true){
            showGrid();
            if (turn==1){
                System.out.print("Player(" + pChar(turn) + ")'s turn [1-7]: ");
                col = Integer.parseInt(br.readLine()) - 1;
                if (isValid(col)){
                    putDisc(col, turn);
                    move += 1;
                    if (isEnd()) {
                        break;
                    }
                    turn += 1;
                }
            }
            else {
                System.out.print("Computer(" + pChar(turn) + ")'s turn [1-7]: ");
                col = calCol(lv);
                System.out.println(col+1);
                if (isValid(col)){
                    putDisc(col, turn);
                    move += 1;
                    if (isEnd()) {
                        break;
                    }
                    turn -= 1;
                }
            }
        }
    }

    private int calCol(int lv){
        ArrayList<Integer> colList = new ArrayList<Integer>();
        int col = 0;

        for (int i=0; i<gridWidth; i++) {
            colList.add(i);
        }

        if (lv==1){
            col = colList.get(new Random().nextInt(colList.size()));
        }

        return col;
    }

    private boolean isEnd(){
        if (isWin(1) || isWin(2)){
            showGrid();
            if (mode==1 && turn==2){
                System.out.println("** COMPUTER(" + pChar(turn) + ") WIN **");
            }
            else{
                System.out.println("** PLAYER(" + pChar(turn) + ") WIN **");
            }
            return true;
        }
        else if (isFull()){
            showGrid();
            System.out.println("** DRAW **");
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isWin(int p){

        int cntHorizontal;
        int cntVertical;
        int cntDiagonalUp;
        int cntDiagonalDown;

        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid.length; j++){
                cntHorizontal = 0;
                cntVertical = 0;
                cntDiagonalUp = 0;
                cntDiagonalDown = 0;
                for(int k=0; k<connect; k++){
                    if (j+k<grid[i].length){
                        if (grid[i][j+k]==p){
                            cntHorizontal+=1;
                        }
                    }
                    if (i+k<grid.length){
                        if (grid[i+k][j]==p){
                            cntVertical+=1;
                        }
                    }
                    if (i+k<grid.length && j+k<grid[i].length){
                        if (grid[i+k][j+k]==p){
                            cntDiagonalUp+=1;
                        }
                    }
                    if (i-k>=0 && j+k<grid[i].length){
                        if (grid[i-k][j+k]==p){
                            cntDiagonalDown+=1;
                        }
                    }
                }
                if (
                    cntHorizontal>=connect || 
                    cntVertical>=connect ||
                    cntDiagonalUp>=connect ||
                    cntDiagonalDown>=connect){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isFull(){
        if (move >= gridWidth * gridHeight){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isValid(int j){
        if (j >= grid[0].length){
            return false;
        }
        int n = 0;
        for(int i=0; i<grid.length; i++){
            if (grid[i][j]>0){
                n+=1;
            }
        }
        if (n >= grid.length){
            return false;
        }
        else{
            return true;
        }
    }

    private void putDisc(int j, int p){
        int n = 0;
        for(int i=0; i<grid.length; i++){
            if (grid[i][j]>0){
                n+=1;
            }
        }
        grid[n][j] = p;
    }

    private void showGrid(){
        //resetConsole();

        // show grid (reverse i)
        for(int i=grid.length-1; i>=0; i--){
            for(int j=0; j<grid[i].length; j++){
                System.out.print("[" + pChar(grid[i][j]) + "]");
            }
            System.out.println();
        }

        // show column number
        for(int i=1; i<=grid[0].length; i++){
            System.out.print(" " + i + " ");
        }
        System.out.println();
    }

    private String pChar(int p){
        if (p==1){
            return p1Char;
        }
        if (p==2){
            return p2Char;
        }
        else{
            return " ";
        }
    }

    private void resetConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
