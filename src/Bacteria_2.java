/**
 * Bacteria type 2
 * eat: PS, AC
 * produce: Ac, But, Toxin
 */

import java.io.IOException;
import java.util.List;
import java.util.Random;


class Bacteria_2 extends Bacteria{


    //Constructor
    public Bacteria_2(int x, int y,int z) {
        super( x, y, z);
        setColor(208, 11, 26);

    }

    public List<Double> getResProperty() {return Gut.BacteriaProperty1_Res;}


    public void division(java.util.List <Entity> le1, java.util.List <Entity> le2)  throws IOException{

        Bacteria_2 b = new Bacteria_2((int) (getX() + getSizeX()/2 * getDx()),(int) (getY() + getSizeX()/2*getDy()),0);
        if (getResistance()){
            b.SetProperty(Gut.BacteriaProperty2_Res);
            b.setResistance(true);
        }else{
            b.SetProperty(Gut.BacteriaProperty2);
            b.setResistance(false);
        }
        RandomDie();
        le2.add(b);
    }

    public void CountRes() {
        if (getResistance()){
            Gut.Bacteria_2_res_count += 1;
            //setColor(211, 211, 9);
        }
    }

}

