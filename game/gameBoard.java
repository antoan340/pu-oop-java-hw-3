package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ThreadLocalRandom;


/**
 *
 * @author Antoan
 * @param "проектиране на игралното поле"
 */
public class gameBoard extends JFrame implements MouseListener {
    public static final int TILE_SIDE_COUNT = 5;
    int rowStart, colStart;
    int randRowLeftFrog;
    int randRowRightFrog;
    int rowCount = rowStart, colCount = colStart;
    private Object[][] Guardians;
    private Leader [][] Leadershit;
    private Object[][] NinjaTurtle;
    private Object selectedGuard;
    private Leader selectedLeader;

    /**
     * @param "Създаване на игралното поле"
     * @author Antoan
     */
    public gameBoard() {
        guardSpawn();
        leaderSpawn();
        turtleSpawn();
        setTitle("Mythic war");
        this.setSize(600, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.addMouseListener(this);
    }
    /**
     * @param "Методите завършващи със Spawn са с цел разделяне на gameBoard"
     * @author Antoan
     */
    private void guardSpawn(){
        this.Guardians = new Guard[TILE_SIDE_COUNT][TILE_SIDE_COUNT];
        for (int i = 0; i < 4; i++) {
            this.Guardians[0][i] = (new Guard(0, i, Color.YELLOW, Color.GREEN));
        }
        for (int i = 4; i > 0; i--) {
            this.Guardians[4][i] = (new Guard(4, i, Color.GREEN, Color.YELLOW));
        }
    }
    /**
     * @param "Същото като предишния коментар"
     * @author Antoan
     */
    private void leaderSpawn(){
        this.Leadershit = new Leader[TILE_SIDE_COUNT][TILE_SIDE_COUNT];
        this.Leadershit[0][4] = (new Leader(0, 4, Color.GREEN));
        this.Leadershit[4][0] = (new Leader(4, 0, Color.YELLOW));
    }
    /**
     * @param "Същото като предишния коментар"
     * @author Antoan
     */
    private void turtleSpawn(){
        this.NinjaTurtle = new Turtle[TILE_SIDE_COUNT][TILE_SIDE_COUNT];
        do
        {
            randRowLeftFrog = ThreadLocalRandom.current().nextInt(0, 5);
            randRowRightFrog = ThreadLocalRandom.current().nextInt(0, 5);
        }while (randRowLeftFrog==randRowRightFrog);
        if(randRowLeftFrog==2){
            randRowLeftFrog--;
        }
        if(randRowRightFrog==2){
            randRowRightFrog--;
        }
        if(randRowRightFrog==randRowLeftFrog){
            randRowRightFrog+=2;
        }
        this.NinjaTurtle[2][randRowLeftFrog] = (new Turtle(2, randRowLeftFrog, Color.RED, Color.WHITE));
        this.NinjaTurtle[2][randRowRightFrog] = (new Turtle(2, randRowRightFrog, Color.RED, Color.WHITE));
    }
    /**
     * @param "Override метод във който се викат функцията на mouseClicked "
     * @author Antoan
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = this.getBoardDimentionBasedOnCoordinates(e.getY());
        int col = this.getBoardDimentionBasedOnCoordinates(e.getX());
        if (row < 5 && col < 5) {
            if (this.selectedGuard != null || this.selectedLeader != null) {
                if ((this.selectedGuard != this.Guardians[row][col] ) && (row - rowStart > -2 && row - rowStart < 2) && (col - colStart > -2 && col - colStart < 2)) {
                    if (this.hasGuardPiece(row, col) || this.hasLeaderPiece(row, col)) {
                        System.out.println("Невалиден ход");
                    } else {
                        guardMove(row, col);
                    }
                }
                if ((this.selectedLeader != this.Leadershit[row][col]) && (row == rowStart || col == colStart)) {
                    if (this.hasGuardPiece(row, col) || this.hasLeaderPiece(row, col)) {
                        System.out.println("Невалиден ход");
                    } else {
                        leaderMove(row, col);

                    }
                }

            } else {
                rowStart = row;
                colStart = col;
                if (this.hasGuardPiece(row, col)) {
                    this.selectedGuard = this.getGuardPiece(row, col);
                }
                if (this.hasLeaderPiece(row, col)) {
                    this.selectedLeader = this.getLeaderPiece(row, col);
                }
            }
        }
    }

    /**
     * @param "Движението на гарда"
     * @author Antoan
     */
    private void guardMove(int row, int col) {
        Guard p = (Guard) this.selectedGuard;
        p.move(row, col);
        this.Guardians[row][col] = this.Guardians[rowStart][colStart];
        this.Guardians[rowStart][colStart] = null;
        if(this.hasTurtlePiece(row,col)){
            this.Guardians[row][col]=null;
            this.NinjaTurtle[row][col]=null;
        }
        this.repaint();
        rowStart = row;
        colStart = col;
        this.selectedGuard = null;
    }

    /**
     * @param "Движението на лидера и проверка дали лидера и гарда не са на едно и също поле"
     * @author Antoan
     */

    private void leaderMove(int row, int col) {
        rowCount = rowStart;
        colCount = col;

        Leader p1 = (Leader) this.selectedLeader;
        leaderUpMove(row,col);
        leaderRightMove(row,col);
        leaderLeftMove(row,col);
        leaderDownMove(row,col);
        p1.move(rowCount, colCount);
        if(this.hasTurtlePiece(rowCount,colCount)){
            this.Leadershit[rowCount][colCount]=null;
            this.NinjaTurtle[rowCount][colCount]=null;
        }
        this.repaint();
        if(rowCount==2&&colCount==2)
        {
            if(Leadershit[rowCount][colCount].color==Color.GREEN)
                Modal.render(this, "Внимание", "Зелените ПОБЕДИХА!");
            if(Leadershit[rowCount][colCount].color==Color.YELLOW)
                Modal.render(this, "Внимание", "Жълтите ПОБЕДИХА!");
        }
        this.Leadershit[rowStart][colStart] = null;
        rowStart = row;
        colStart = col;
        this.selectedLeader = null;
    }
    /**
     * @param "Движението на лидера в всяка определена посока
     * с цел намаляването на линийте код и спазвайки правилото
     * ако метод става прекалено голям да бъде разделен на различни методи"
     * @author Antoan
     */
    private void leaderLeftMove(int row,int col){
        if (colStart > col) {
            do {
                colCount--;
            } while ((this.Guardians[row][colCount] == null && this.Leadershit[row][colCount] == null&&this.NinjaTurtle[row][colCount] == null) && colCount > 0);
            if (this.Guardians[row][colCount] != null || this.Leadershit[row][colCount] != null) colCount++;
            this.Leadershit[row][colCount] = this.selectedLeader;
            rowCount=row;
        }
    }
    /**
     * @param "Същото като предишния коментар"
     * @author Antoan
     */
    private void leaderRightMove(int row,int col){
        if (colStart < col) {
            do {
                colCount++;
            } while ((this.Guardians[row][colCount] == null && this.Leadershit[row][colCount] == null&&this.NinjaTurtle[rowCount][colCount] == null) && colCount < 4);
            if (this.Guardians[row][colCount] != null || this.Leadershit[row][colCount] != null) colCount--;
            this.Leadershit[row][colCount] = this.selectedLeader;
            rowCount=row;
        }
    }
    /**
     * @param "Същото като предишния коментар"
     * @author Antoan
     */
    private void leaderDownMove(int row,int  col){
        if (rowStart < row) {
            do {
                rowCount++;
            } while ((this.Guardians[rowCount][col] == null && this.Leadershit[rowCount][col] == null&&this.NinjaTurtle[rowCount][col] == null) && rowCount < 4);
            if (this.Guardians[rowCount][col] != null || this.Leadershit[rowCount][col] != null) rowCount--;
            this.Leadershit[rowCount][col] = this.selectedLeader;
            colCount=col;
        }
    }
    /**
     * @param "Същото като предишния коментар"
     * @author Antoan
     */
    private void leaderUpMove(int row,int col){
        if (rowStart > row) {
            do {
                rowCount--;
            } while ((this.Guardians[rowCount][col] == null && this.Leadershit[rowCount][col] == null&&this.NinjaTurtle[rowCount][col] == null) && rowCount > 0);
            if (this.Guardians[rowCount][col] != null || this.Leadershit[rowCount][col] != null) rowCount++;
            this.Leadershit[rowCount][col] = this.selectedLeader;
            colCount=col;
        }
    }
    @Override
    /**
     *
     * @author Antoan
     * @param "Пренаписване на вече написания родителски клас пеин"
     */
    public void paint(Graphics g) {

        super.paint(g);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                GameTile tile = new GameTile(row, col);
                tile.render(g);
            }
        }
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                renderGamePiece(g, row, col);
            }
        }

    }

    /**
     * @param "обекта "гард
     * @author Antoan
     */
    private Object getGuardPiece(int row, int col) {
        return this.Guardians[row][col];

    }

    /**
     * @param "проверка за дали гарда не е нулл"
     * @author Antoan
     */

    private boolean hasGuardPiece(int row, int col) {
        return this.getGuardPiece(row, col) != null;
    }

    /**
     * @param "обекта лидер"
     * @author Antoan
     */
    private Object getTurtlePiece(int row, int col) {
        return this.NinjaTurtle[row][col];

    }

    private boolean hasTurtlePiece(int row, int col) {
        return this.getTurtlePiece(row, col) != null;
    }

    private Leader getLeaderPiece(int row, int col) {
        return this.Leadershit[row][col];

    }

    /**
     * @param "проверка дали лидера не нулл"
     * @author Antoan
     */

    private boolean hasLeaderPiece(int row, int col) {
        return this.getLeaderPiece(row, col) != null;
    }

    /**
     * @param "проектирането на игралните фигури"
     * @author Antoan
     */
    private void renderGamePiece(Graphics g, int row, int col) {
        if (this.hasGuardPiece(row, col)) {
            Guard p = (Guard) this.getGuardPiece(row, col);
            p.render(g);

        }
        if (this.hasLeaderPiece(row, col)) {
            Leader p1 = (Leader) this.getLeaderPiece(row, col);
            p1.render(g);

        }
        if (this.hasTurtlePiece(row, col)) {
            Turtle p3 = (Turtle) this.getTurtlePiece(row, col);
            p3.render(g);

        }
    }



    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    /**
     *
     * @author Antoan
     * @param "координатите на играта"
     */
    private int getBoardDimentionBasedOnCoordinates(int coordinates) {
        return coordinates / GameTile.TILE_SIZE;
    }
}
