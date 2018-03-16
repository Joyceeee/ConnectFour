package nl.joyce.connectfour.tudelft.ide.software.connectfour;

/**
 * Created by joyce on 16-2-2018.
 */

public class FourInARowGame {
    private int playerID=1,playerWhoWon=0,emptyColumnCells,column;
    private int Disc1Row=-1,Disc1Col=-1,Disc2Row=-1,Disc2Col=-1,Disc3Row=-1,Disc3Col=-1;
    private int [] number0fEmptyColumnCells = new int [] {6,6,6,6,6,6,6};
    private int [][] board = new int [6][7];

    private void ShowBoard(){
        System.out.println("Four in a row game board");
        for(int rowindex=0;rowindex<6;rowindex++){
            for (int columnindex=0;columnindex<7;columnindex++)
                System.out.print(String.valueOf(board[rowindex][columnindex]));
            System.out.println();
        }
    }

    private boolean fourInARowHorizontally(){
        int lefttofind=3, rowindex=emptyColumnCells-1; // check to the left
        int columnindex=column-2; // first column index to the left
        while (lefttofind>0 && columnindex>=0 && board[rowindex][columnindex]==playerID) {
            if (Disc1Row == -1){Disc1Col=columnindex+1;Disc1Row=rowindex+1;}
            else if (Disc2Row == -1){Disc2Col=columnindex+1;Disc2Row=rowindex+1;}
            else if (Disc3Row == -1){Disc3Col=columnindex+1;Disc3Row=rowindex+1;}
            columnindex--;
            lefttofind--;
        }
        if (lefttofind==0) return true;// check to the right
        columnindex=column; // first column index to the right
        while (lefttofind>0 && columnindex<=6 && board[rowindex][columnindex]==playerID) {
            if (Disc1Row == -1){Disc1Col=columnindex+1;Disc1Row=rowindex+1;}
            else if (Disc2Row == -1){Disc2Col=columnindex+1;Disc2Row=rowindex+1;}
            else if (Disc3Row == -1){Disc3Col=columnindex+1;Disc3Row=rowindex+1;}
            columnindex++;
            lefttofind--;
        }
        if (lefttofind!=0) {Disc1Row=-1;Disc1Col=-1;Disc2Col=-1;Disc2Row=-1;Disc3Col=-1;Disc3Row=-1;}
        return (lefttofind==0);
    }

    private boolean fourInARowVertically(){
        int lefttofind=3, columnindex=column-1;// check up
        int rowindex=emptyColumnCells-2;
        while (lefttofind>0 && rowindex>=0 && board[rowindex][columnindex]==playerID) {
            if (Disc1Row == -1){Disc1Col=columnindex+1;Disc1Row=rowindex+1;}
            else if (Disc2Row == -1){Disc2Col=columnindex+1;Disc2Row=rowindex+1;}
            else if (Disc3Row == -1){Disc3Col=columnindex+1;Disc3Row=rowindex+1;}
            rowindex--;
            lefttofind--;
        }
        if (lefttofind==0) return true;// check down
        rowindex=emptyColumnCells;
        while (lefttofind>0 && rowindex<=5 && board[rowindex][columnindex]==playerID) {
            if (Disc1Row == -1){Disc1Col=columnindex+1;Disc1Row=rowindex+1;}
            else if (Disc2Row == -1){Disc2Col=columnindex+1;Disc2Row=rowindex+1;}
            else if (Disc3Row == -1){Disc3Col=columnindex+1;Disc3Row=rowindex+1;}
            rowindex++;
            lefttofind--;
        }
        if (lefttofind!=0) {Disc1Row=-1;Disc1Col=-1;Disc2Col=-1;Disc2Row=-1;Disc3Col=-1;Disc3Row=-1;}
        return (lefttofind==0);
    }

    private boolean fourInARowReverseDiagonally(){
        int lefttofind=3, rowindex, columnindex;
        // check right and up
        rowindex=emptyColumnCells-2; columnindex=column;
        while (lefttofind>0 && rowindex>=0 && columnindex<=6 &&
                board[rowindex][columnindex]==playerID) {
            if (Disc1Row == -1){Disc1Col=columnindex+1;Disc1Row=rowindex+1;}
            else if (Disc2Row == -1){Disc2Col=columnindex+1;Disc2Row=rowindex+1;}
            else if (Disc3Row == -1){Disc3Col=columnindex+1;Disc3Row=rowindex+1;}
            rowindex--;
            columnindex++;
            lefttofind--;
        }
        if (lefttofind==0) return true;
        // check left and down
        rowindex=emptyColumnCells; columnindex=column-2;
        while (lefttofind>0 && rowindex<=5 && columnindex>=0 &&
                board[rowindex][columnindex]==playerID) {
            if (Disc1Row == -1){Disc1Col=columnindex+1;Disc1Row=rowindex+1;}
            else if (Disc2Row == -1){Disc2Col=columnindex+1;Disc2Row=rowindex+1;}
            else if (Disc3Row == -1){Disc3Col=columnindex+1;Disc3Row=rowindex+1;}
            rowindex++;
            columnindex--;
            lefttofind--;
        }
        if (lefttofind!=0) {Disc1Row=-1;Disc1Col=-1;Disc2Col=-1;Disc2Row=-1;Disc3Col=-1;Disc3Row=-1;}
        return (lefttofind==0);
    }


    private boolean fourInARowDiagonally(){
        int lefttofind=3, rowindex, columnindex;// check left and up
        rowindex=emptyColumnCells-2; columnindex=column-2;
        while (lefttofind>0 && rowindex>=0 && columnindex>=0 &&
                board[rowindex][columnindex]==playerID) {
            if (Disc1Row == -1){Disc1Col=columnindex;Disc1Row=rowindex;}
            else if (Disc2Row == -1){Disc2Col=columnindex;Disc2Row=rowindex;}
            else if (Disc3Row == -1){Disc3Col=columnindex;Disc3Row=rowindex;}
            rowindex--;
            columnindex--;
            lefttofind--;
        }
        if (lefttofind==0) return true;// check right and down
        rowindex=emptyColumnCells; columnindex=column;
        while (lefttofind>0 && rowindex<=5 && columnindex<=6 &&
                board[rowindex][columnindex]==playerID) {
            if (Disc1Row == -1){Disc1Col=columnindex;Disc1Row=rowindex;}
            else if (Disc2Row == -1){Disc2Col=columnindex;Disc2Row=rowindex;}
            else if (Disc3Row == -1){Disc3Col=columnindex;Disc3Row=rowindex;}
            rowindex++;
            columnindex++;
            lefttofind--;
        }
        if (lefttofind!=0) {Disc1Row=-1;Disc1Col=-1;Disc2Col=-1;Disc2Row=-1;Disc3Col=-1;Disc3Row=-1;}
        return (lefttofind==0);
    }

    private boolean currentPlayerWon(){
        if (fourInARowHorizontally()) return true;
        if (fourInARowVertically()) return true;
        if (fourInARowDiagonally()) return true;
        if (fourInARowReverseDiagonally()) return true;
        return false;
    }


    private void checkForEndOfGame(){
        if(currentPlayerWon()) playerWhoWon = playerID;
        else if (isBoardFull())playerWhoWon = 3;
    }

    private boolean isBoardFull(){
        int columnindex=6;
        while (columnindex>=0 && number0fEmptyColumnCells[columnindex]==0) columnindex--;
        return(columnindex<0);
    }

    public int getResult(){
        return playerWhoWon;
    }

    public int [] getDiscs(){
        int [] allDisc = {Disc1Col,Disc1Row,Disc2Col,Disc2Row,Disc3Col,Disc3Row};
        return allDisc;
    }


    public int dropDisc(int column){
        if (column>=1&&column<=7){
            this.column = column;
            emptyColumnCells=number0fEmptyColumnCells[column-1];
            board[emptyColumnCells-1][column-1]=playerID;
            checkForEndOfGame();
            ShowBoard();
            playerID=(playerID==1?2:1);
            if(emptyColumnCells>0)
                number0fEmptyColumnCells[column-1]=emptyColumnCells-1;
            return emptyColumnCells;
        }
        return 0;//some error
    }
}
