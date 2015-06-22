import java.awt.*;
import java.io.IOException;


public class PolySaccharides extends Entity {

    /// Nutrition = PS
   // private static double Km_PS = 100;
  // private static double Km_MCT = 1;//, K_Met;          // transporter, speed eat by bacteria        // to environment

    // /Speed variable
    //OY - to gut boundary
    double Speed_flux;
    //OX - out from gut
    double Speed_out;

    //Constructor
    public PolySaccharides (int x, int y,int z) {
        super(x, y, z);
        setSizeX(10);
        this.setSpeed(2000);
        setColor(48, 106, 178);
    }

    //chose direction of oy speed
    public void MoveFlux (double Speed_out,double Speed_trans){

        setStepX(getStepX() + Speed_out);
        if (getStepY() > Gut.getD()/2){
            setStepY(getStepY() + Speed_trans);
        }
        else {
            setStepY(getStepY() - Speed_trans);
        }
    }

    //draw PS in the artificial gut
    public void draw(Graphics g){

        GradientPaint gp = new GradientPaint((int)(getX()/Gut.getTickX()), (int) (getY()/Gut.getTickY()), new Color(getColor_r(),getColor_g(),getColor_b(), 180),
                (int)(getX()/Gut.getTickX()), (int) (getY()/Gut.getTickY()) + getSizeX(), new Color(getColor_r() + 50,getColor_g() + 100,getColor_b()+50, 180));
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(gp);
        g2d.fillOval((int)(getX()/Gut.getTickX()), (int) (getY()/Gut.getTickY()), getSizeX(),getSizeX());
    }

    //calculate speed OX(moving out) & OY(moving to gut boundary)
    public void Speed_equations(int acetate, int butyrate, int propionate, double k) {
        // from diff equations
        double D = 1 + (acetate / Gut.getV()) / Acetate.GetКm_MCT() + (butyrate / Gut.getV()) / Butyrate.GetКm_MCT() + (propionate / Gut.getV()) / Propionate.GetКm_MCT();
        //System.out.println(Speed_out);//  (k_gut_out*metabolite)*Gut.getDimX()/metabolite     // выводиться из кишечника
        this.Speed_flux = (Gut.getK_trans() / (k * D * Gut.getV())) * (Gut.getD() - 2 * Gut.getD_mucus()) / Gut.getNorm(); //всасывается в кишечник   (упрощение) //- 2 * Gut.getD_mucus())
        //metabolite cannot move if they are in mucin layer
        if (getStepY() > Gut.getD_save_zone() & getStepY() <  Gut.getD() - Gut.getD_save_zone() - Gut.getD_mucus()) {
            //calculate Speed out from gut
            this.Speed_out = Gut.getK_gut_out() * Gut.getL() / (Gut.getNorm()) * Math.pow(1.0 - 2 * (double) Math.abs(getY() + Gut.getD_mucus()/2 - Gut.getD() / 2) / Gut.getD(), 2);

        }else{
            //in mucin layer PSs don't move
            Speed_out = 0;
        }

    }

    //use this function to reduce PS amount,when PS gone out or was eaten by bacteria
    public void CalculateCount() {
        Gut.PS_count -= 1;
    }

    //action for each program tick
    public void tick(java.util.List<Entity> PS, java.util.List<Entity> t1,java.util.List<Entity> t2,java.util.List<Entity> A,
                     java.util.List<Entity> b1, java.util.List<Entity> b2, Gut g)
            throws IOException {
        //calculate new coordinates and move here
        Speed_equations(Gut.Acet_count, Gut.But_count, Gut.Prop_count, 1);
        MoveFlux(Speed_out, 0);
        SetNewCoordinate();
        //check place of PS and calculate count
        if(getX() > Gut.getL() - getSizeX() & getLive()) {
            setLive(false);
            CalculateCount();
        }

    }


}

/**
 * PS, which are produced from gut and locate in mucin layer
 */

class PsFromGut extends PolySaccharides {

    //constructor
    public PsFromGut (int x, int y,int z){
        super(x, y, z);
        setColor(190, 27, 178);
    }

    //use this function to reduce PS amount,when PS gone out or was eaten by bacteria
    public void CalculateCount() {
        Gut.PSFromGut_count -= 1;
    }

}
