/**
 * metabolite are involved in SCFA's metabolism
 */
import java.io.IOException;
import java.util.Random;


public abstract class Metabolite extends PolySaccharides{

    //Constructor
    public Metabolite (int x, int y, int z){
        super(x,y,z);
        setDx(Math.cos(new Random().nextInt(360)));
        setDy(Math.sin(new Random().nextInt(360)));
        setSizeX(8);
        setKm_MCT(Km_MCT);

    }

    //constant variable of rate of a reaction connecting MCT with metabolite
    private double Km_MCT;
    //set/get value of KM_MCT
    public double getКm_MCT(){
        return this.Km_MCT;
    }

    public void setKm_MCT( double km_mct){
        this.Km_MCT = km_mct;
    }

    //action for each program tick
    public void tick(java.util.List<Entity> PS, java.util.List<Entity> t1,java.util.List<Entity> t2,java.util.List<Entity> A,
                     java.util.List<Entity> b1, java.util.List<Entity> b2, Gut g)
            throws IOException {

        //calculate speed
        Speed_equations(Gut.Acet_count, Gut.But_count, Gut.Prop_count, getКm_MCT());
        //move with set speed
        MoveFlux(Speed_out, Speed_flux);
        //change coordinates
        SetNewCoordinate();
        //check metabolite position in the gut
        if (getY() > Gut.getD() - Gut.getD_mucus()- Gut.getStepMucus()*Gut.getTickY()  || getY() < Gut.getD_mucus()
                ||getX() > Gut.getL() - getSizeX()){
            setLive(false);
            CalculateCount();
        }
    }

}


class Propionate extends Metabolite{

    // private static double K_Prop = 4.64;      //   speed eat
    private static double Km_MCT = 15;       // transporter
    // private static double Km_Prop = 100;

    public Propionate (  int x, int y,int z) {
        super( x, y, z);
        setColor(18, 138, 134);
        setKm_MCT(Km_MCT);
    }

    public static double GetКm_MCT(){
        return Km_MCT;
    }

    public void CalculateCount(){
        Gut.Prop_count -=1;
    }

}

class Butyrate extends Metabolite {

    // private static double K_Acet_But = 0.058 ;
    private static double Km_MCT = 21.3;
    // private static double Km_Acet_But = 100;


    public Butyrate (int x, int y,int z) {
        super(x, y, z);
        setColor(127, 25, 100);
        setKm_MCT(Km_MCT);

    }


    public static double GetКm_MCT(){
        return Km_MCT;
    }

    public void CalculateCount(){
        Gut.But_count -=1;
    }

}


class Acetate extends Metabolite {


    private static double Km_Ac_MCT = 15;
    // private static double Km_Acet_B = 100, Km_Acet_F = 100;


    public Acetate (  int x, int y,int z) {
        super( x, y, z);
        setColor(180, 110, 10);
        setKm_MCT(Km_Ac_MCT);
    }

    public static double GetКm_MCT(){
        return Km_Ac_MCT;
    }

    public void CalculateCount(){
        Gut.Acet_count -=1;
    }

}