/*
* this class describes same functions to all other subclasses
* */

import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.List;


public abstract class Entity {


    //Constructor
    public Entity (int x, int y,int z) {
        this.x = x; this.y = y; this.z = z;
        this.StepX = x;
        this.StepY = y;

}
    //coordinate[mkm]
    private int x, y, z;
    //direction vectors [mkm]
    protected double dx, dy;//,dz;
    //helpful coordinate [mkm]]
    private double StepX, StepY;
    //speed [mkm/hour]
    private int Speed;
    //size : SizeX = SizeY = SizeZ
    private int SizeX;

    //color variable; visual version
    int color_r, color_g, color_b;
    // live`s label
    private boolean live = true;
    //create panel for painting for visual version

    //voids to get/set value of variable
    protected void setX (int x){
        this.x = x ;
    }
    protected int getX (){
        return this.x;
    }
    protected void setY (int y){
        this.y = y ;
    }
    protected int getY(){
        return this.y ;
    }

    protected void setDx (double dx){
        this.dx = dx ;
    }
    protected double getDx(){
        return this.dx ;
    }

    protected void setDy (double dy){
        this.dy = dy ;
    }
    protected double getDy(){
        return this.dy ;
    }

    protected void setColor (int r, int g, int b){
        this.color_r = r;
        this.color_g = g;
        this.color_b = b;
    }

    protected int getColor_r (){ return  this.color_r;}
    protected int getColor_g () {return  this.color_g; }
    protected int getColor_b () { return this.color_b; }

    protected void setLive (boolean live){
        this.live = live;
    }
    protected boolean getLive(){
        return this.live;
    }

    protected void setSpeed(double s){
        this.Speed = (int) s;
    }
    protected double getSpeed(){
        return  Speed;
    }

    public int getSizeX(){
        return  SizeX;
    }
    public void setSizeX(int s) {
        this.SizeX = s;
    }

    protected void setStepX(double x){
        this.StepX = x;
    }
    protected double getStepX(){
        return StepX;
    }

    protected double getStepY(){
        return StepY;
    }
    protected void setStepY(double y){
        this.StepY = y;
    }

    //action for each program step
    public abstract void tick(List<Entity> PS, List<Entity> T1,List<Entity> T2,List<Entity> ANT, List<Entity> b1, List<Entity> b2, Gut g)
            throws IOException ;
    //draw object; visual version
    public abstract void draw(Graphics g);// {  }

    public void SetProperty (int x, int y){
        setStepX(x);
        setStepY(y);
    }

    public void SetNewCoordinate(){
        setX((int)Math.round(getStepX()));
        setY((int)Math.round(getStepY()));
    }



    //gut perturbation
    public void ChangePosition (int n){ ///1/m - sigma 1/n - average
        if (getY() > Gut.getD_save_zone() & getY() < Gut.getD() - Gut.getD_save_zone()){
                 setStepX(getStepX() + (int)(new Random().nextGaussian()*Gut.getL()/n));
        }
    }

    //fluctuation moving (random)
    public void RandomMove(){
        setDx(Math.cos(new Random().nextInt(360)));
        setDy(Math.sin(new Random().nextInt(360)));

        setStepX(getStepX() - getSpeed()/Gut.getNorm() * getDx() );

        setStepY(getStepY() + getSpeed()/Gut.getNorm() * getDy());
    }


}

