package game;

import java.awt.*;
/**
 *
 * @author Antoan
 * @param "Костенурката наследява стойностите на фигурата"
 */
public class Turtle extends Figure {
    public Color color;
    public Color color1;

    public Turtle(int row, int col, Color color, Color color1) {
        super();
        this.row = row;
        this.col=col;
        this.color=color;
        this.color1=color1;
    }
    /**
     *
     * @author Antoan
     * @param "Визуализиране на костенурките"
     */
    public void render(Graphics g) {
        int x = this.col * GameTile.TILE_SIZE;
        int y = this.row * GameTile.TILE_SIZE;
        g.setColor(this.color);
        g.fillOval(x+33, y+54, 54, 54);
        g.setColor(this.color1);
        g.fillOval(x+40, y+61, 40, 40);
    }

}

