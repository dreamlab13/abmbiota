
import org.omg.CORBA.portable.ValueOutputStream;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public abstract class Bacteria extends Entity {

    //Constructor
    public Bacteria (int x, int y,int z){
        super(x, y, z);
        setStepX(x); //double
        setStepY(y); //double

    }

    //variable
    //time to degradable one metabolite and produce another [ticks]
    private double degradableEat;
    //time, when bacteria eats some metabolite [ticks]
    private int TimeEat;
    //bacteria can sensitive something at this distance [mkm]
    double R = 3500;
    //period of time,during which bacteria can live without eat
    private double eat_rate;
    //random period of time,during which bacteria can live without eat;
    //this value has chosen depended on Gaussian distribution witch mean value equals eat_rate and sigma = 1/3*eat_rate
    private double eat_range;
    //sensitive to toxin
    private int Sensitive1, Sensitive2;
    //sensitive to antibiotic
    private int SensitiveA;
    //mutation rate
    private static int Mutation_rate;
    private static double ALPHA = 0.009, BETTA = 5, b = 5000;
    static double FitnessCost = 4;
    private boolean Resistance = false;
    //rate of a reaction of degradation one metabolites to another[mmol/hour]
    private double k_Ac, k_But, k_Prop, k_toxin1, k_antiToxin1,k_antiToxin2, k_toxin2;           //mmol/hour
    private double k_PsFromGut;
    //variable of count toxins/antibiotics within bacteria
    private int Toxin1InB,Toxin2InB, Antibiotic;
    //variable to set type of metabolite,which bacteria will produce
    private int TypeOfProduceEat; // 1 - AC, 2 - Prop, 3-But, 4 - Toxin, 5 - Antitoxin


    abstract public List<Double> getResProperty();

    //voids for get/set value of variable
    public  double getK_Ac(){
        return this.k_Ac;
    }
    public  double getK_But(){
        return this.k_But;
    }
    public  double getK_Prop(){
        return this.k_Prop;
    }
    public  double getK_PsFromGut(){
        return this.k_PsFromGut;
    }

    public double getK_toxin1(){
        return this.k_toxin1;
    }
    public double getK_antiToxin1(){
        return this.k_antiToxin1;
    }

    public double getK_toxin2(){
        return this.k_toxin2;
    }
    public double getK_antiToxin2(){
        return this.k_antiToxin2;
    }

    public void setK_But(double k){
        this.k_But = k;
    }

    public void setK_Ac(double k) {
        this.k_Ac = k;
    }
    public void setK_Prop(double k) {
        this.k_Prop = k;
    }
    public void setK_PsFromGut(double k) {this.k_PsFromGut = k;}

    public void setK_toxin2(double k){
        this.k_toxin2 = k;
    }
    public void setK_antiToxin2(double k){
        this.k_antiToxin2 = k;
    }

    public void setK_toxin1(double k){
        this.k_toxin1 = k;
    }
    public void setK_antiToxin1(double k){
        this.k_antiToxin1 = k;
    }

    public double getEat_rate(){
        return this.eat_rate;
    }
    public void setEat_rate(double r) {
        this.eat_rate = r;
    }
    public double getEat_range(){
        return this.eat_range;
    }

    public double getDegradableEat(){
        return this.degradableEat;
    }

    public void setDegradableEat(double d){
        this.degradableEat = d;
    }

    public void setTypeOfProduceEat (int t) { this.TypeOfProduceEat = t;}
    public int getTypeOfProduceEat (){return this.TypeOfProduceEat;}

    public int getTimeEat(){
        return this.TimeEat;
    }

    public void setTimeEat(int t){
        this.TimeEat = t;
    }

    public void setEat_range(double d){
        this.eat_range = d;
    }

    public void setToxin1InB (int t) { this.Toxin1InB = t; }

    public int getToxin1InB () { return this.Toxin1InB; }

    public void setToxin2InB (int t) { this.Toxin2InB = t; }

    public int getToxin2InB () { return this.Toxin2InB; }

    public void setAntibiotic () {this.Antibiotic += 1;}
    public int getAntibiotic () {return this.Antibiotic;}



    public void setSensitive1(double s){
        this.Sensitive1 = (int) s;
    }

    public int getSensitive1(){
        return this.Sensitive1;
    }
    public void setSensitive2(double s){
        this.Sensitive2 = (int) s;
    }

    public int getSensitive2(){
        return this.Sensitive2;
    }
    public void setSensitiveA(double s){
        this.SensitiveA = (int) s;
    }

    public int getSensitiveA(){
        return this.SensitiveA;
    }
    //variables for mutation function
    static void setB (int n) {b = n;}
    static void setFitnessCost(int n) {FitnessCost = n;}

    public void setResistance(boolean f){this.Resistance = f;}

    public boolean getResistance(){return this.Resistance;}

    static int getMutation_rate(){ return Mutation_rate;}
    //sigma function
    static void setMutation_rate(double m) {Mutation_rate = (int) (b/(1 + Math.exp(-ALPHA * m + BETTA))/FitnessCost/Gut.getK_ant_intake());}



    //set value of bacteria variable
    public void SetProperty(java.util.List<Double> l) {
        setSizeX(5);
        setK_Ac(l.get(0));
        setK_But(l.get(1));
        setK_Prop(l.get(2));
        setK_toxin1(l.get(3));
        setK_antiToxin1(l.get(4));
        setSensitive1(l.get(5));
        setSensitive2(l.get(8));
        setSensitiveA(l.get(9));
        setK_toxin2(l.get(6));
        setK_antiToxin2(l.get(7));
        setK_PsFromGut(l.get(10));
        this.R = l.get(11);
        setEat_range(l.get(12));
        setSpeed(l.get(13));
        RandomDie();
        setTimeEat(Gut.ticks - 1);
        setToxin1InB(0);
        setToxin2InB(0);
    }


    //draw bacteria in the artificial gut
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new Color(getColor_r(), getColor_g(), getColor_b()));
        g2d.fillOval((int) (getX() / Gut.getTickX()), (int) (getY() / Gut.getTickY()), 2*getSizeX(), 2*getSizeX());
    }


    //find in r-distance the nearest peace of eat for this bacteria
    public int scan(java.util.List<Entity> PS, java.util.List<Entity> KM, double R ) {
        double distance_min = R*R;
        int index_min = -1;


        if (this.getK_Ac() != 0.0 || this.getK_Prop() != 0.0){
            for (int i = 0; i < Gut.PS_count; i++) {
                Entity ps = PS.get(i);
                double distance  = Math.pow((ps.getX() - getX()),2) + Math.pow((ps.getY() - getY()), 2);
                if (distance <= distance_min){
                        distance_min = distance;
                        index_min = PS.indexOf(ps);
                    }
            }
        }



        if (this.getK_PsFromGut()!=0.0){
            for (int i = Gut.PS_count; i < Gut.PsFromGutIndex; i++) {
                Entity ps = PS.get(i);
                double distance  = Math.pow((ps.getX() - getX()),2) + Math.pow((ps.getY() - getY()), 2);
                if (distance <= 2*distance_min){
                        distance_min = distance;
                        index_min = PS.indexOf(ps);
                }

            }
        }


        if (this.getK_But()!= 0.0){
            for (int i = Gut.PsFromGutIndex; i < Gut.AcetateIndex; i++) {
                Entity a = PS.get(i);
                double distance  = Math.pow(a.getX() - getX(), 2) + Math.pow(a.getY() - getY(), 2);
                if (distance <= 2*distance_min){
                        distance_min = distance;
                        index_min = PS.indexOf(a);
                }
            }
        }
        if (this.getK_antiToxin1()!= 0.0 & getSensitive1() == 0.0){ //b1 eat toxin1
            for (int i = 0; i < KM.size(); i++) {
                Entity ps = KM.get(i);
                double distance = Math.pow((ps.getX() - getX()),2) + Math.pow((ps.getY() - getY()), 2);
                if (distance <= distance_min ){
                    distance_min = distance;
                    //index_min = KM.indexOf(ps);
                    index_min = -2;
                    KM.remove(ps);
                    setDegradableEat(Gut.getNorm() / this.getK_antiToxin1());
                    setTimeEat(Gut.ticks + (int)getDegradableEat());
                }

            }
        }
      /*  if (this.getK_antiToxin2()!=0.0 ){ //b1 eat toxin2
            for (int i = Gut.Toxin_count1; i < Gut.Toxin2Index; i++) {
                // PolySaccharides ps = PS.get(i);
                Entity ps = KM.get(i);
                double distance = Math.pow((ps.getX() - getX()),2) + Math.pow((ps.getY() - getY()), 2);
                if (distance <= distance_min ){
                    distance_min = distance;
                    index_min = PS.indexOf(ps);
                }

            }
        }*/

        return (index_min);

    }

    //calculate direct vector to eat with index i
    public void direct(List<Entity> l, int i){
        //direct vector
        double a_x ;
        double a_y ;
            a_x = getX() - l.get(i).getX();
            a_y = getY() - l.get(i).getY();

            double a_mod = Math.sqrt(a_x*a_x + a_y*a_y);
            if (a_mod!=0){
                setDx(- a_x / a_mod);
                setDy(- a_y / a_mod);
            }
    }

    //bacteria change position
    public void Move(){

        setStepX(getStepX() + getSpeed()/Gut.getNorm() * getDx());
        setStepY(getStepY() + getSpeed()/Gut.getNorm() * getDy());
    }

    //void to calculate  distance between two objects and compare it with set distance
    public boolean CheckDistance(int x1, int x2, int y1, int y2, double d){

          double distance  = Math.sqrt(Math.pow((x1 - x2),2) + Math.pow((y1 - y2), 2));

          return (distance <= d) ;

    }

    //check transaction trajectories of two object (bacteria and eat);
    // return true if trajectories transaction
    boolean transaction (double ax1, double ay1, double ax2, double ay2,
                         double bx1, double by1, double bx2, double by2)
    {
        double v1 = (bx2-bx1) * (ay1-by1) - (by2-by1) * (ax1-bx1);
        double v2 = (bx2-bx1) * (ay2-by1) - (by2-by1) * (ax2-bx1);
        double v3 = (ax2-ax1) * (by1-ay1) - (ay2-ay1) * (bx1-ax1);
        double v4 = (ax2-ax1) * (by2-ay1) - (ay2-ay1) * (bx2-ax1);
        return ((v1 * v2 <= 0) && (v3 * v4 <= 0));
    }

    //bacteria chose what it will produce
    public void eat(List<Entity> ps,List<Entity> KM, int i){
        //Ac->But
        Entity e = ps.get(i);
        //setTimeEat(Gut.ticks);
        setStepX(e.getX());
        setStepY(e.getY());
        String s = String.valueOf(e.getClass().getSimpleName());
        int r = new Random().nextInt(100);
       if (s.equals("Acetate")) {
           if (r > 40 & getK_toxin1() != 0.0) {
                setDegradableEat(Gut.getNorm()/(this.getK_toxin1())) ;
                setTypeOfProduceEat(4);
            }else{
               setDegradableEat(Gut.getNorm()/(this.getK_But()));
               setTypeOfProduceEat(3);
           }
           Gut.Acet_count-=1;
           ps.remove(i);
           setTimeEat(Gut.ticks + (int)getDegradableEat());

        }
        //PS-> Acetate/Propionate/toxin
        if (s.equals("PolySaccharides")) {
            //Gut.PS_count-=1;

            if(r <= 60){
                setDegradableEat(Gut.getNorm()/(this.getK_Ac())) ;
                setTypeOfProduceEat(1);
            }else{
                  if (this.getK_Prop() != 0.0 ) {
                      setDegradableEat(Gut.getNorm() / this.getK_Prop());
                      setTypeOfProduceEat(2);
                    }else{
                        setDegradableEat(Gut.getNorm()/this.getK_Ac()) ;
                        setTypeOfProduceEat(1);
                    }
           }
            e.setSizeX(e.getSizeX() - 2);
            if (e.getSizeX() <= 5){ ps.remove(i); Gut.PS_count-=1;}
            setTimeEat(Gut.ticks + (int)getDegradableEat());
        }
        /*if (s.equals("Toxin1")){
                setDegradableEat(Gut.getNorm() / this.getK_antiToxin1());
                setTypeOfProduceEat(6);
                //Gut.Toxin_count1 -= 1;
                KM.remove(i);
            setTimeEat(Gut.ticks + (int)getDegradableEat());

        }*/

        if (s.equals("PsFromGut")){
            setDegradableEat(Gut.getNorm() / this.getK_Ac());
            setTypeOfProduceEat(1);
            e.setSizeX(e.getSizeX() - 2);
            if (e.getSizeX() <= 5){ ps.remove(i); Gut.PSFromGut_count -= 1;}
            setTimeEat(Gut.ticks + (int)getDegradableEat());
        }

    }

    //bactria produce metabolite
    public void produce(java.util.List<Entity> PS, java.util.List<Entity> KM){
        switch (getTypeOfProduceEat()){
            case 1:
                PS.add(Gut.AcetateIndex,new Acetate( this.getX() + (int)(Math.signum(Gut.getD()/2 - getY())*(this.R + 2)),
                        this.getY() + (int)(Math.signum(Gut.getD()/2 - getY())*(this.R + 2)), 0)) ;
                Gut.Acet_count += 1;
                break;
            case 2:
                PS.add(Gut.PropionateIndex,new Propionate( this.getX() + 2, this.getY() + 2, 0)) ;
                Gut.Prop_count += 1;
                break;
            case 3:
                PS.add(new Butyrate(this.getX() + 2,this.getY() + 2, 0));
                Gut.But_count += 1;
                break;
            case 4:
                KM.add(new Toxin1(this.getX() + (int)this.R + 2, this.getY(), 0));
               // Gut.Toxin_count1 += 1;
                break;
           /* case 5:
                KM.add(Gut.Toxin_count1,new Toxin2(this.getX() + 2 ,this.getY() +2, 0));
                Gut.Toxin_count2 += 1;
                break;*/
            case 6: break;
        }
    }

    //check intersect with gut border
    public void intersect(){
        //left border
        if (getStepX() + getSizeX() < 0) { setDx(-getDx()); setStepX(4*getSizeX()); }
        //right border: bacteria go out
        if (getStepX() + getSizeX() > Gut.getL() - getSizeX()) { this.setLive(false);}
        //epithelium cells:
        //down
        if (getStepY() + getSizeX() > Gut.getD() - Gut.getD_mucus() - Gut.getStepMucus()*Gut.getTickY()) {
             setDy(-getDy());
             setStepY(Gut.getD()-2*getSizeX()- Gut.getD_mucus() - (int) (Gut.getStepMucus()*Gut.getTickY()));
            }
        //bottom
        if (getStepY() - getSizeX()  < Gut.getD_mucus())  {
              setDy(-getDy());
              setStepY(2*getSizeX() + Gut.getD_mucus());
            }

    }


    //division (add new Bacteria)
    public abstract void division(java.util.List <Entity> le1, java.util.List <Entity> le2) throws IOException ;

    //set period when bacteria must eat
    public void RandomDie() {

        setEat_rate(new Random().nextInt((int) (2 * getEat_range())));

    }



    //set live false if bacteria have enough toxin or antibiotics within
    public void CheckToxin(List<Entity> t1, List<Entity> t2, List<Entity> a){
        if ( getSensitive1()!= 0 & getToxin1InB() >= getSensitive1()) {
            //bacteria can't live
            setLive(false);
            //add toxins 1 from bacteria to environment
            /*for (int i=0; i < getToxin1InB(); i++) {
                    t1.add( new Toxin1( this.getX(), this.getY(), 0));
            }*/
        }
        if (getSensitive2()!= 0 & getToxin2InB() >= getSensitive2()) {
            //bacteria can't live
            setLive(false);
            //add toxins 2 from bacteria to environmentch
            /*for (int i=0; i < getToxin2InB(); i++) {
                    t2.add( new Toxin2( this.getX(), this.getY(), 0));
               }*/
        }
        if (!getResistance() & getSensitiveA()!= 0 & getAntibiotic() > getSensitiveA()){
            //bacteria can't live
            setLive(false);
            //add antibiotics from bacteria to environment
            //for (int i=0; i < 2; i++){
                a.add(new Antibiotic(this.getX(),this.getY(), 0));
            //}
        }
    }

    //set live false if bacteria didn't eat during set period
    public void CheckTime(){
            setLive (Gut.ticks < (int) (getTimeEat() + getEat_rate() ));
    }

    //bactria is moved by gut (laminar current)
    public void GoOut() {
        if (getStepY() > Gut.getD_save_zone() & getStepY() < Gut.getD() - Gut.getD_save_zone() - Gut.getStepMucus()*Gut.getTickY())  {
            setStepX(getStepX() + Gut.getK_gut_out()*Gut.getL()/Gut.getNorm()*Math.pow(1.0 - 2*(double)Math.abs(getY() + Gut.getD_mucus() - Gut.getD()/2)/Gut.getD(),2.5));
        }
    }

    //antibiotic resistance
    public void Mutation(){
        if (!Resistance & new Random().nextInt(10000) < Mutation_rate){
            SetProperty(getResProperty());
            setResistance(true);

        }

    }

    abstract void CountRes();


    //action for each tick of program
    public void tick(List<Entity> PS, java.util.List<Entity> t1,java.util.List<Entity> t2,java.util.List<Entity> A,
                     List<Entity> b1, List<Entity> b2, Gut g)
            throws IOException{
        //calculate positions of metabolites in a PS list
        Gut.PsFromGutIndex = Gut.PS_count + Gut.PSFromGut_count;
        Gut.AcetateIndex = Gut.PS_count + Gut.PSFromGut_count + Gut.Acet_count;
        Gut.PropionateIndex = Gut.PS_count + Gut.PSFromGut_count + Gut.Acet_count + Gut.Prop_count;
        Gut.ButyrateIndex = Gut.PS_count + Gut.PSFromGut_count + Gut.Acet_count + Gut.Prop_count + Gut.But_count;
        //Gut.Toxin2Index = Gut.Toxin_count1 + Gut.Toxin_count2;
        //if bacteria isn't degradable eat now
        if (getTimeEat() < Gut.ticks ){
            //then bacteria finds eat in r-distance
            int index = scan(PS,t1, R);
            //if bacteria has benn found eat
            if (index != -1 & index != -2) {
                //then bacteria chose direct to eat
                direct(PS,index);
                PolySaccharides ps = (PolySaccharides) PS.get(index);
                //and move to it
                Move();
                //if bacteria haa the same coordinate as a eat
                if (transaction(ps.getX(),ps.getY(), ps.getX()+ ps.Speed_out, ps.getY(),
                        getX(), getY(),getStepX(), getStepY())
                        || CheckDistance(ps.getX(), getX(), ps.getY(), getY(),
                        (getX()/Gut.getTickX()/16 + 16*Gut.getTickX()/getX())*ps.getSizeX())) {
                    //then bacteria eats
                    eat(PS,t1,index);

                }
            //if bacteria hasn't find eat
            } else {
                //then bacteria do random moving
                RandomMove();
                setStepY(getStepY() + Math.signum(getY() + Gut.getD_mucus() - Gut.getD()/2)*getSizeX());
            }
        }else {
            //if the period to degradable eat ends
            if( getTimeEat() == Gut.ticks ){
                //then bacteria produces metabolite
                produce(PS, t1);
                //and division
                division(b1,b2);
            }else {
                //if bacteria is degrading eat
                //then bacteria does random moving
                RandomMove();
                setStepY(getStepY() + 2*Math.signum(getY() + Gut.getD_mucus()  - Gut.getD()/2)*getSizeX());

            }
        }

        //bacteria are moved by gut forces depended on bacteria`s location in the gut
        if  (Math.abs(getY()/Gut.getTickY() + Gut.getStepMucus() - Gut.getDimY()/2) < Math.abs(new Random().nextGaussian()*Gut.getDimY()/8)){ //
            GoOut();

        }
        //bacteria want to be in the beginning of the gut
        setStepX((int)getStepX() - 2*getSizeX());
        //bacteria are moved by perturbation in the gut
        if (Gut.ticks % (Gut.eatPeriod *Gut.getNorm()) == 0) {
            ChangePosition(50);
        }
        //check intersect with boundary
        intersect();
        //check how long bacteria didn't eat
        CheckTime();
        //check how many killBacteria in bacteria
        CheckToxin(t1,t2,A);
        //set new coordinates depended on bacteria decided
        SetNewCoordinate();
        //if bacteria died then reduce the amount of them
        Mutation();
        CountRes();
    }



}




