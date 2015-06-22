/**
 * metabolites, which can kill bacteria
 * toxins and antibiotics
 */

import java.awt.*;
import java.io.IOException;
import java.util.List;


public abstract class KillMetabolite extends PolySaccharides {


    public KillMetabolite ( int x, int y, int z ) {
        super( x, y,z);
        SetProperty(x,y);
        setSizeX(8);
        this.setSpeed(2000);
    }

    //set a list of values, that correspond to sensitive bacteria to KM
    public abstract java.util.List<Integer> getProperty();

    public void draw (Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new Color(getColor_r(), getColor_g(), getColor_b(), 190));
        g2d.fillOval((int) (getX() / Gut.getTickX()), (int) (getY() / Gut.getTickY()), getSizeX(), getSizeX());
    }

    //abstract function for calculate amount toxins or antibiotics in the bacteria
    public abstract void CalculateCount(Bacteria b);

    //toxin penetrates in bacteria if bacteria has the same coordinate
    public void KillBacteria(java.util.List<Entity> B){
        for (Entity b: B ) {
            Bacteria b1 = (Bacteria) b;
            //check distance between bacteria and KM
            if (b1.transaction(b1.getX(), b1.getY(), b1.getStepX(), b1.getStepY(),
                    getX(), getY(), getStepX(), getStepY())
                    || b1.CheckDistance(b1.getX(), getX(), b1.getY(), getY(), 100*getSizeX())) {
                //if true, then metabolite is in bacterium
                setLive(false);
                //calculate KMs count within bacteria
                CalculateCount(b1);
            }
        }
    }

    //move along the gut
    public void Move(){
        if (getStepY() > Gut.getD_save_zone() & getStepY() <  Gut.getD() - Gut.getD_save_zone() - Gut.getD_mucus()) {

        setStepX(getStepX() +Gut.getK_gut_out()*Gut.getL()/(Gut.getNorm())*Math.pow(1.0 - 2*(double)Math.abs(getY() + Gut.getD_mucus()/2 - Gut.getD()/2)/Gut.getD(),2));
        } else{
            setStepY(getStepY() - 3*Math.signum(getY() + Gut.getD_mucus()  - Gut.getD()/2)*getSizeX());
        }
        RandomMove();
    }

    //action for each program tick
    public void tick(java.util.List<Entity> PS, java.util.List<Entity> t1,java.util.List<Entity> t2,
                     java.util.List<Entity> A,
                     java.util.List<Entity> b1, java.util.List<Entity> b2, Gut g)
                    throws IOException{
        Move();
        RandomMove();
        for (int i= 0; i < getProperty().size(); i++){
            KillBacteria(b1);
            KillBacteria(b2);
        }
        //check KM position in a gut and live`s label
        if(getX() > Gut.getL() - getSizeX() || !getLive() ||
                getY() > Gut.getD() - Gut.getD_mucus()- Gut.getStepMucus()*Gut.getTickY()  ||
                getY() < Gut.getD_mucus()) {
            setLive(false);
           // CalculateCount();
        }
        //change current coordinate corresponding to action
        SetNewCoordinate();
    }

}

class Toxin1 extends KillMetabolite {

    public Toxin1 ( int x, int y, int z ){
        super( x, y,z);
        setColor(32, 2, 38);
    }

    public void CalculateCount(Bacteria b){

        b.setToxin1InB(b.getToxin1InB() + 1);
    }

    public void CalculateCount(){
     //   Gut.Toxin_count1 -= 1;
    }
    public List<Integer> getProperty(){
        return Gut.KM1;
    }

    public void tick(java.util.List<Entity> PS, java.util.List<Entity> t1,java.util.List<Entity> t2,
                     java.util.List<Entity> A,
                     java.util.List<Entity> b1, java.util.List<Entity> b2, Gut g)
            throws IOException{
        Move();
        //RandomMove();
            KillBacteria(b2);

        //check KM position in a gut and live`s label
        if(getX() > Gut.getL() - getSizeX() || !getLive() ||
                getY() > Gut.getD() - Gut.getD_mucus()- Gut.getStepMucus()*Gut.getTickY()  ||
                getY() < Gut.getD_mucus()) {
            setLive(false);
            // CalculateCount();
        }
        //change current coordinate corresponding to action
        SetNewCoordinate();
    }

}


class Toxin2 extends KillMetabolite {

   public Toxin2 ( int x, int y, int z ){
       super( x, y,z);
       setColor(162, 162, 162);
   }

   public void CalculateCount(Bacteria b){
       b.setToxin2InB (b.getToxin2InB() + 1);
   }

    public void CalculateCount(){
        //Gut.Toxin_count2 -= 1;
    }

    public List<Integer> getProperty(){
        return Gut.KM2;
    }
}

class Antibiotic extends KillMetabolite{

    public Antibiotic ( int x, int y, int z){
        super( x, y,z);
        setColor(90, 255, 0);
        setSizeX(10);
    }

    public void CalculateCount(Bacteria b){

        b.setAntibiotic();
    }
    public void CalculateCount(){
     //   Gut.Antibiotic_count -= 1;
    }

    public List<Integer> getProperty(){
        return Gut.KM3;
    }
}