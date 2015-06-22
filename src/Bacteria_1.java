/**
 * Bacteria type 1
 */

import java.awt.geom.Arc2D;
import java.io.IOException;
import java.util.List;
import java.util.Random;


class Bacteria_1 extends Bacteria {


    //Constructor
    public Bacteria_1 (int x, int y,int z) {
        super(x, y, z);
        setColor(34,35,178);
    }

    public List<Double> getResProperty() {return Gut.BacteriaProperty1_Res;}


   public void division(java.util.List <Entity> le1, java.util.List <Entity> le2) throws IOException{

        Bacteria_1 b =  new  Bacteria_1((int) (getX() + getSizeX()/2 * getDx()),(int) (getY() + getSizeX()/2*getDy()),0);

       if (getResistance()){
           b.SetProperty(Gut.BacteriaProperty1_Res);
           b.setResistance(true);
       }else{
           b.SetProperty(Gut.BacteriaProperty1);
           b.setResistance(false);
       }
       RandomDie();
       le1.add(b);
    }

    public void CountRes() {
        if (getResistance()){
            Gut.Bacteria_1_res_count += 1;
            //setColor(211, 211, 9);
        }
    }

}
