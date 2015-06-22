import java.awt.*;
import java.io.IOException;
import java.util.*;


class Mucus extends Entity {

    //rate of reaction producing PS from Gut in mucin layer [mmol/hour]
    private static double k_ps_mucus;
    private static int period;

    //Constructor
    public Mucus (int x, int y,int z) throws IOException {
        super(x, y, z);
        setSizeX(10);
    }

    public static double getK_ps_mucus(){
        return k_ps_mucus;
    }

    public static void setK_ps_mucus(double k){
        k_ps_mucus = k;
    }

    public static void setPeriod(int k){
        period = k;
    }


    public void draw(Graphics g){

        GradientPaint gp = new GradientPaint((int)(getX()/Gut.getTickX()), (int) (getY()/Gut.getTickY()), new Color(178, 108, 114),
                (int) (getX()/Gut.getTickX()), (int) (getY()/Gut.getTickY()) + getSizeX(), new Color(225,185, 244, 77));
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(gp);
        g2d.fillRect((int)(getX()/Gut.getTickX()), (int) (getY()/Gut.getTickY()), getSizeX(), getSizeX());
    }

    //add ps in mucin layer
    public static void createPSGut(java.util.List<Entity> PS){
         if (k_ps_mucus >= 2){
            for (int i =0; i < (k_ps_mucus)/2; i++){
                PS.add(Gut.PS_count,new PsFromGut(new Random().nextInt(Gut.getL()),
                            new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) + Gut.getD_mucus(), 0));
                PS.add(Gut.PS_count,new PsFromGut(new Random().nextInt(Gut.getL()),
                            Gut.getD() - new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) - 2 * Gut.getD_mucus(), 0));
                Gut.PSFromGut_count += 2;
            }
        }else{
            if (k_ps_mucus >= 1){
                PS.add(Gut.PS_count,new PsFromGut(new Random().nextInt(Gut.getL()),
                        new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) + Gut.getD_mucus(), 0));
                PS.add(Gut.PS_count,new PsFromGut(new Random().nextInt(Gut.getL()),
                        Gut.getD() - new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) - 2 * Gut.getD_mucus(), 0));
                Gut.PSFromGut_count += 2;
            }else {
                if (Gut.ticks % (period) == 0) {
                    PS.add(Gut.PS_count,new PsFromGut(new Random().nextInt(Gut.getL()),
                            new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) + Gut.getD_mucus(), 0));
                    PS.add(Gut.PS_count,new PsFromGut(new Random().nextInt(Gut.getL()),
                            Gut.getD() - new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) - 2 * Gut.getD_mucus(), 0));
                    Gut.PSFromGut_count += 2;

                }
            }
        }
    }

    //add another type of ps in mucin layer
    public static void createPS(java.util.List<Entity> PS){
        if (k_ps_mucus >= 2){
            for (int i =0; i < (k_ps_mucus/2); i++){
                PS.add(0,new PolySaccharides (new Random().nextInt(Gut.getL()),
                        new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) + Gut.getD_mucus(), 0));
                PS.add(0,new PolySaccharides(new Random().nextInt(Gut.getL()),
                        Gut.getD() - new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) - 2 * Gut.getD_mucus(), 0));
                Gut.PS_count += 2;
            }
        }else{
            if (k_ps_mucus >= 1){
                PS.add(0,new PolySaccharides (new Random().nextInt(Gut.getL()),
                        new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) + Gut.getD_mucus(), 0));
                PS.add(0,new PolySaccharides(new Random().nextInt(Gut.getL()),
                        Gut.getD() - new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) - 2 * Gut.getD_mucus(), 0));
                Gut.PS_count += 2;
            }else {
                if (Gut.ticks % (period) == 0) {
                    PS.add(0,new PolySaccharides (new Random().nextInt(Gut.getL()),
                            new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) + Gut.getD_mucus(), 0));
                    PS.add(0,new PolySaccharides(new Random().nextInt(Gut.getL()),
                            Gut.getD() - new Random().nextInt(Gut.getD_save_zone() - Gut.getD_mucus()) - 2 * Gut.getD_mucus(), 0));
                    Gut.PS_count += 2;

                }
            }
        }
    }


 public void tick(java.util.List<Entity> PS, java.util.List<Entity> t1,java.util.List<Entity> t2,java.util.List<Entity> A,
                     java.util.List<Entity> b1, java.util.List<Entity> b2, Gut g)
                    throws IOException{}

}
