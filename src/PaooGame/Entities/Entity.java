package PaooGame.Entities;

import PaooGame.GameStates.Play;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    protected float x, y;
    protected int width,height;
    protected Rectangle2D.Float hitbox;
    protected Rectangle2D.Float attackBox;
    protected int maxHealth;
    protected int currentHealth;


    public Entity(float x, float y,int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width=width;
        this.height=height;
    }

    protected void drawHitbox(Graphics g){
        //for debugging thi hitboc
        g.setColor(Color.BLACK);
        g.drawRect((int)hitbox.x,(int)hitbox.y,(int) hitbox.width, (int)hitbox.height);
    }

    protected void initHitbox(float x,float y, int width, int height) {
        hitbox=new Rectangle2D.Float(x,y,width,height);
    }
  /*  protected void updateHitbox(){
        hitbox.x=(int)(x);
        hitbox.y=(int)(y);
    }*/
    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }
}
