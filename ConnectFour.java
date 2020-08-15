import java.io.*;
import java.util.*;

class ConnectFour{

    // initial variable
    final int gridWidth = 7;
    final int gridHeight = 6;
    final int connect = 4;
    final String p1Char = "X";
    final String p2Char = "O";
    private int[][] gridMain = new int[gridHeight][gridWidth];
    private int[][] gridTest = new int[gridHeight][gridWidth];
    private int turn = 1;
    private int move = 0;
    private int mode = 0;
    private int comLv = 0;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException{
        ConnectFour c = new ConnectFour();
        c.start();
    }

    // start program with welcome screen to choose game mode
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
        // single-player mode and choose computer level
        if (mode == 1){
            System.out.println("Please select computer level:");
            System.out.println("[1] Easy");
            System.out.println("[2] Medium");
            System.out.println("[3] Hard");
            while (comLv<1 || comLv>3){
                System.out.print("Input [1-3]: ");
                comLv = Integer.parseInt(br.readLine());
            }
            modeSingle(comLv);
        }
        // two-player mode
        else {
            modeTwo();
        }

    }

    // single-player mode
    private void modeSingle(int lv) throws IOException{
        int col;
        while(true){
            showGrid();
            if (turn==1){
                System.out.print("Player(" + pChar(turn) + ")'s turn [1-7]: ");
                col = Integer.parseInt(br.readLine()) - 1;
                if (isValid(col, gridMain)){
                    putDisc(col, turn, gridMain);
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
                if (isValid(col, gridMain)){
                    putDisc(col, turn, gridMain);
                    move += 1;
                    if (isEnd()) {
                        break;
                    }
                    turn -= 1;
                }
            }
        }
    }

    // two-player mode
    private void modeTwo() throws IOException{
        int col;
        while(true){
            showGrid();
            System.out.print("Player(" + pChar(turn) + ")'s turn [1-7]: ");
            col = Integer.parseInt(br.readLine()) - 1;
            if (turn==1){
                if (isValid(col, gridMain)){
                    putDisc(col, turn, gridMain);
                    move += 1;
                    if (isEnd()) {
                        break;
                    }
                    turn += 1;
                }
            }
            else {
                if (isValid(col, gridMain)){
                    putDisc(col, turn, gridMain);
                    move += 1;
                    if (isEnd()) {
                        break;
                    }
                    turn -= 1;
                }
            }
        }
    }

    // calculate column for computer
    private int calCol(int lv){
        int col;

        // check column that ables to put disc
        ArrayList<Integer> colListValid = new ArrayList<Integer>();
        for (int j=0; j<gridMain[0].length; j++) {
            if (isValid(j, gridMain)){
                colListValid.add(j);
            }
        }

        // check column that can defeat competitor
        ArrayList<Integer> colListToWin = new ArrayList<Integer>();
        for (int j=0; j<colListValid.size(); j++){
            gridCopy();
            if (isValid(j, gridTest)){
                putDisc(j, 2, gridTest);
                if (isWin(2, gridTest)){
                    colListToWin.add(j);
                }
            }
        }

        // check column that can be defeated
        ArrayList<Integer> colListToDefend = new ArrayList<Integer>();
        for (int j=0; j<colListValid.size(); j++){
            gridCopy();
            if (isValid(j, gridTest)){
                putDisc(j, 1, gridTest);
                if (isWin(1, gridTest)){
                    colListToDefend.add(j);
                }
            }
        }

        // computer level2 check validate and winning column
        if (lv==2){
            if (colListToWin.size()>0){
                col = colListToWin.get(new Random().nextInt(colListToWin.size()));
            }
            else{
                col = colListValid.get(new Random().nextInt(colListValid.size()));
            }
        }
        // computer level3 check validate, winning and defensive column
        else if (lv==3){
            if (colListToWin.size()>0){
                col = colListToWin.get(new Random().nextInt(colListToWin.size()));
            }
            else if (colListToDefend.size()>0){
                col = colListToDefend.get(new Random().nextInt(colListToDefend.size()));
            }
            else{
                col = colListValid.get(new Random().nextInt(colListValid.size()));
            }
        }
        // computer level1 check validate column
        else{
            col = colListValid.get(new Random().nextInt(colListValid.size()));
        }

        return col;
    }

    // copy main grid to test grid
    private void gridCopy(){
        for(int i=0; i<gridMain.length; i++){
            for(int j=0; j<gridMain[i].length; j++){
                gridTest[i][j]=gridMain[i][j];
            }
        }
    }

    // check whether game ends
    private boolean isEnd(){
        if (isWin(1, gridMain) || isWin(2, gridMain)){
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

    // check whether player wins
    private boolean isWin(int p, int[][] g){

        // there are four direction to check
        int cntHorizontal;
        int cntVertical;
        int cntDiagonalUp;
        int cntDiagonalDown;

        // for loop inside grid
        for(int i=0; i<g.length; i++){
            for(int j=0; j<g.length; j++){
                cntHorizontal = 0;
                cntVertical = 0;
                cntDiagonalUp = 0;
                cntDiagonalDown = 0;
                for(int k=0; k<connect; k++){
                    if (j+k<g[i].length){
                        if (g[i][j+k]==p){
                            cntHorizontal+=1;
                        }
                    }
                    if (i+k<g.length){
                        if (g[i+k][j]==p){
                            cntVertical+=1;
                        }
                    }
                    if (i+k<g.length && j+k<g[i].length){
                        if (g[i+k][j+k]==p){
                            cntDiagonalUp+=1;
                        }
                    }
                    if (i-k>=0 && j+k<g[i].length){
                        if (g[i-k][j+k]==p){
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

    // check whether grid is full
    private boolean isFull(){
        if (move >= gridWidth * gridHeight){
            return true;
        }
        else{
            return false;
        }
    }

    // check whether column is validated to put disc
    private boolean isValid(int j, int[][] g){
        if (j >= g[0].length){
            return false;
        }
        int n = 0;
        for(int i=0; i<g.length; i++){
            if (g[i][j]>0){
                n+=1;
            }
        }
        if (n >= g.length){
            return false;
        }
        else{
            return true;
        }
    }

    // put disc to grid
    private void putDisc(int j, int p, int[][] g){
        int n = 0;
        for(int i=0; i<g.length; i++){
            if (g[i][j]>0){
                n+=1;
            }
        }
        g[n][j] = p;
    }

    // show main grid in console
    private void showGrid(){
        resetConsole();

        // show grid (reverse i)
        for(int i=gridMain.length-1; i>=0; i--){
            for(int j=0; j<gridMain[i].length; j++){
                System.out.print("[" + pChar(gridMain[i][j]) + "]");
            }
            System.out.println();
        }

        // show column number
        for(int i=1; i<=gridMain[0].length; i++){
            System.out.print(" " + i + " ");
        }
        System.out.println();
    }

    // print plater character
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

    // reset console
    private void resetConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
