/**
 * artificial gut
 */

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;


public class Gut {

    //Constructor
    public Gut() {
        super();
    }

    //create lists of objects
    private List<Entity> mucus = new ArrayList<Entity>();
    private List<Entity> bacterias1 = new ArrayList<Entity>();
    private List<Entity> bacterias2 = new ArrayList<Entity>();
    private List<Entity> PS = new ArrayList<Entity>();
  //  private List<Entity> KM = new ArrayList<Entity>();
     private List<Entity> T1 = new ArrayList<Entity>();
     private List<Entity> T2 = new ArrayList<Entity>();
     private List<Entity> ANT = new ArrayList<Entity>();
    private List<Number> ProdAmount = new ArrayList<Number>();
    static List<Double> BacteriaProperty1 = new ArrayList<Double>();
    static List<Double> BacteriaProperty2 = new ArrayList<Double>();
    static List<Double> BacteriaProperty1_Res = new ArrayList<Double>();
    static List<Double> BacteriaProperty2_Res = new ArrayList<Double>();
    static List<Integer> KM1 = new ArrayList<Integer>(2);
    static List<Integer> KM2 =  new ArrayList<Integer>(2);
    static List<Integer> KM3  =  new ArrayList<Integer>(2);

    //Variables:

    //metabolite count
    static int Amount_PS , PS_count;
    static int Acet_count;
    static int Prop_count;
    static int But_count;
    static int PSFromGut_count;
    //static int Toxin_count1,Toxin_count2,Antibiotic_count ;

    //index of class in list
    static int AcetateIndex, ButyrateIndex, PropionateIndex, PsFromGutIndex;

    //Bacteria count
    static int Bacteria_2_count, Bacteria_2_res_count;
    static int Bacteria_1_count, Bacteria_1_res_count;

    //Variable for counting time
    static int ticks;
    //Variable for stop program at this time
    static int tickslimit;

    //constants
    //PS mmol intake from environment per min [mmol/hour]
    private static double k_intake;
    //speed objects during the gut [1/hour]
    private static double k_gut_out;
    //rate of a intussuscept metabolites reaction in the gut [mmol/hour]
    private static double k_trans;
    //Antibiotics mmol intake from environment per min [mmol/hour]
    private static double k_ant_intake;

    //dimension
    //volume of the gut [l]
    private static double V = 0.8;
    //length of the artificial gut [px]
    private static int dimX = 1600;
    //average length of a real gut [2 meters = 2*10^6 mkm]
    private static int L = (int) (2*Math.pow(10, 6));
    //diameter of the artificial gut [px]
    private static int dimY = 320;
    //average diameter of a real gut [6 cm = 6*10^4 mkm]
    private static int D = (int) (6*Math.pow(10, 4));
    //number of mkm in 1 px [mkm/px]
    private static double tickX = L/dimX;
    private static double tickY = D/dimY;
    //size of layer [mkm]
    private static int d_mucus = D/15;
    //size of mucin layer [mkm]
    private static int d_save_zone = 3*d_mucus;
    //size of epithelial cells for visual version [px]
    static int stepMucus = 15;
    //number of minuets in 1 program's tick [min]
    private static int TickTime = 2;
    //number of tick in 1 hour [ticks]
    private static double norm = 60/TickTime;

    //Variable for eating
    //period of eating [hours]
    static int TickEat = (int) (0.45*norm);
    //period between eating [hours]
    static int eatPeriod;// = 8*norm;
    //time of last eating
    static int ticksEatTime = 1;
    //period of antibiotic getting
    static int AntibioticPeriod;
    static int ticksAntTime;
    //amount of days in which get antibiotic
    static int AntibioticsDay;
    //helpful counter: count antibiotics days
    static int t;
    //produce Ps for all or for one type bacteria
    //static boolean PsLabel;



    //Voids for get/set value of variable
    public static double getK_gut_out(){
        return k_gut_out;
    }

    public static double getK_trans(){
        return k_trans;
    }

    public static double getK_ant_intake() {return  k_ant_intake;}

    public static int getStepMucus(){
        return stepMucus;
    }

    public static double getNorm(){
        return norm;
    }

    public static double getV(){
        return V;
    }

    public static double getTickX(){
        return tickX;
    }

    public static double getTickY(){
        return tickY;
    }

    public static int getD_save_zone(){
        return d_save_zone;
    }

    public static int getD_mucus(){
        return d_mucus;
    }

    public static int getDimX(){
        return dimX;
    }

    public static int getDimY(){
        return dimY;
    }

    public static int getD(){
        return D;
    }

    public static int getL(){
        return L;
    }


    //Variable for reading value of variable from external file
    static Properties props = new Properties() ;

    //Variable for writing output in a file
    File logFile;
    PrintWriter writeFile = null;
    String ID = null;

     //read value of gut variable from external file
    public void SetProperty ( String fout){ //String fset

        k_gut_out = 0.06;//Double.valueOf(props.getProperty("k_gut_out", "1.0"));
        k_trans =  5.0;//Double.valueOf(props.getProperty("k_trans", "1.0"));
        k_intake = 70/norm;//Double.valueOf(props.getProperty("k_intake", "1.0"));
        k_ant_intake = Double.valueOf(props.getProperty("k_ant_intake", "1.0"))/norm;
        Mucus.setK_ps_mucus(10.0/norm);//Double.valueOf(Gut.props.getProperty("k_ps_mucus", "1.00")));
        Mucus.setPeriod((int) (1 / Mucus.getK_ps_mucus()));

        Bacteria_2_count = Integer.valueOf(props.getProperty("Bacteria_2_count", "1"));
        Bacteria_1_count = Integer.valueOf(props.getProperty("Bacteria_1_count", "1"));
        Amount_PS =  Integer.valueOf(props.getProperty("Amount_PS", "1"));
        PS_count = Amount_PS;
        Acet_count =  300;//Integer.valueOf(props.getProperty("Acet_count", "1"));
        But_count =  100;//Integer.valueOf(props.getProperty("But_count", "1"));
        Prop_count =  100;//Integer.valueOf(props.getProperty("Prop_count", "1"));
        AntibioticPeriod = Integer.valueOf(props.getProperty("AntibioticPeriod", "1"));

        tickslimit = Integer.valueOf(props.getProperty("tickslimit", "1"));
        TickEat = (int) (1*norm); //(int) (Double.valueOf(props.getProperty("TickEat", "1.0"))*norm);
        eatPeriod = (int) (Integer.valueOf(props.getProperty("eatPeriod", "1"))*norm); //7
        ID = fout;

        //create new output file
        logFile =  new File(fout);

       if (Double.valueOf(props.getProperty("k_intake", "1.0")) == 0.0) {
            k_intake = 0;
        }
       // PsLabel = Produce.equals("PS");

    }
    //read value of bacteria variable from external file and create two lists of variables for each bacterial type
    public void logBacteriaProperty(java.util.List<Double> l1, java.util.List<Double> l2){
        l1.add(Double.valueOf(props.getProperty("k_Ac_1", "1.00")));
        l2.add(Double.valueOf(props.getProperty("k_Ac_2", "1.00")));
        l1.add(Double.valueOf(props.getProperty("k_But_1", "1.00")));
        l2.add(Double.valueOf(props.getProperty("k_But_2", "1.00")));
        l1.add(Double.valueOf(props.getProperty("k_Prop_1", "1.00")));
        l2.add(Double.valueOf(props.getProperty("k_Prop_2", "1.00")));
        l1.add(Double.valueOf(props.getProperty("k_toxin1_1", "1.00")));
        l2.add(Double.valueOf(props.getProperty("k_toxin1_2", "1.00")));
        l1.add(Double.valueOf(props.getProperty("k_antitoxin1_1", "1.00")));
        l2.add(Double.valueOf(props.getProperty("k_antitoxin1_2", "1.00")));
        l1.add(Double.valueOf(props.getProperty("Sense1_1", "1.00")));
        l2.add(Double.valueOf(props.getProperty("Sense1_2", "1.00")));
        l1.add(Double.valueOf(props.getProperty("k_toxin2_1", "1.00")));
        l2.add(Double.valueOf(props.getProperty("k_toxin2_2", "1.00")));
        l1.add(Double.valueOf(props.getProperty("k_antitoxin2_1", "1.00")));
        l2.add(Double.valueOf(props.getProperty("k_antitoxin2_2", "1.00")));
        l1.add(Double.valueOf(props.getProperty("Sense2_1", "1.00")));
        l2.add(Double.valueOf(props.getProperty("Sense2_2", "1.00")));
        l1.add(Double.valueOf(props.getProperty("SenseA_1", "1.0")));
        l2.add(Double.valueOf(props.getProperty("SenseA_2", "1.0")));
        l1.add(0.16);//Double.valueOf(props.getProperty("k_PSFromGut_1", "1.00")));
        l2.add(0.0);//Double.valueOf(props.getProperty("k_PSFromGut_2", "1.00")));
        l1.add(5000.0);//Double.valueOf(props.getProperty("R", "1.0")));
        l1.add(2.0*eatPeriod);//Double.valueOf(props.getProperty("eat_rate", "1")));
        l1.add(10000.0);//Double.valueOf(props.getProperty("Speed", "1.0"))) ;
        l2.add(5000.0);//Double.valueOf(props.getProperty("R", "1.0")));
        l2.add(2.0*eatPeriod);//Double.valueOf(props.getProperty("eat_rate", "1")));
        l2.add(10000.0);//Double.valueOf(props.getProperty("Speed", "1.0"))) ;
        //Bacteria.k = Math.max(l1.get(0), l1.get())/Math.max(l.get(1), l.get(3));
        KM1.add(Integer.valueOf(props.getProperty("Sense1_1", "1")));
        KM1.add(Integer.valueOf(props.getProperty("Sense1_2", "1")));
        KM2.add(Integer.valueOf(props.getProperty("Sense2_1", "1")));
        KM2.add(Integer.valueOf(props.getProperty("Sense2_2", "1")));
        KM3.add(Integer.valueOf(props.getProperty("SenseA_1", "1")));
        KM3.add(Integer.valueOf(props.getProperty("SenseA_2", "1")));
     //   Bacteria.setB(Integer.valueOf(props.getProperty("b", "1")));
        Bacteria.setFitnessCost(Integer.valueOf(props.getProperty("FitnessCost", "1")));
    }

    public void logBacteriaPropertyRes(java.util.List<Double> lOld, java.util.List<Double> lNew){

        //lNew = lOld;
        lNew.add(lOld.get(0)/Bacteria.FitnessCost);
        lNew.add(lOld.get(1)/Bacteria.FitnessCost);
        lNew.add(lOld.get(2)/Bacteria.FitnessCost);
        lNew.add(lOld.get(3)/Bacteria.FitnessCost);
        lNew.add(lOld.get(4)/Bacteria.FitnessCost);
        lNew.add(lOld.get(5));
        lNew.add(lOld.get(6)/Bacteria.FitnessCost);
        lNew.add(lOld.get(7)/Bacteria.FitnessCost);
        lNew.add(lOld.get(8));
        lNew.add(lOld.get(9));
        lNew.add(lOld.get(10));
        lNew.add(lOld.get(11));
        lNew.add(lOld.get(12));
        lNew.add(lOld.get(13));

    }

    //add objects to the gut
    public void addEntity()  throws IOException {

        //add bacteria 1 and set its properties
        for(int i= 0; i < Bacteria_1_count/2; i++){
            Bacteria_1 b =  new Bacteria_1(new Random().nextInt(L),
                    new Random().nextInt(D - (int)1.25*d_mucus) +  (int)0.75*d_mucus, 0);
            b.SetProperty(Gut.BacteriaProperty1);
            b.setTimeEat(new Random().nextInt(eatPeriod));
           bacterias1.add(b);
        }
        for(int i= 0; i < Bacteria_1_count/2; i++){
            Bacteria_1 b =  new Bacteria_1(new Random().nextInt(L),
                    new Random().nextInt(D - (int)1.25*d_mucus) +  (int)0.75*d_mucus, 0);
            b.SetProperty(Gut.BacteriaProperty1);
            b.setTimeEat(- new Random().nextInt(eatPeriod));
            bacterias1.add(b);
        }

        //add bacteria 2 and set its properties
        for(int i= 0; i < Bacteria_2_count/2; i++){
            Bacteria_2 b =  new Bacteria_2(new Random().nextInt(L),
                    new Random().nextInt(D - (int)1.25*d_mucus) + +  (int)0.75*d_mucus, 0);
            b.SetProperty(Gut.BacteriaProperty2);
          //  b.setEat_rate(b.getEat_rate() + 4*eatPeriod/5);
            b.setTimeEat(new Random().nextInt(eatPeriod));//(int)(Math.signum(new Random().nextGaussian())*
           // b.setTypeOfProduceEat(new Random().nextInt(2) + 3);
            bacterias2.add(b);
        }
         for(int i= 0; i < Bacteria_2_count/2; i++){
            Bacteria_2 b =  new Bacteria_2(new Random().nextInt(L),
                    new Random().nextInt(D - (int)1.25*d_mucus) + +  (int)0.75*d_mucus, 0);
            b.SetProperty(Gut.BacteriaProperty2);
          //  b.setEat_rate(b.getEat_rate() + 4*eatPeriod/5);
            b.setTimeEat( - new Random().nextInt(eatPeriod));//(int)(Math.signum(new Random().nextGaussian())*
           // b.setTypeOfProduceEat(new Random().nextInt(2) + 3);
            bacterias2.add(b);
        }
        //add mucus
        for(int i = 0; i < L; i += stepMucus*tickX){
            for (int j = 0; j <= d_mucus + (int)(stepMucus*tickY); j += stepMucus*tickY) {
                mucus.add(new Mucus(i, D - j, 0));
            }
        }
        for(int i = 0; i < L; i += stepMucus*tickX){
            for (int j = 0; j < d_mucus; j += stepMucus*tickY ) {
                mucus.add(new Mucus(i, j, 0));
            }
        }

        //add metabolites
        for (int i = 0; i < Amount_PS; i++){
            PS.add (new PolySaccharides(new Random().nextInt(L),new Random().nextInt(D - 3*d_mucus) + d_mucus,
                    0));
        }
        for (int i = 0; i < Acet_count; i++){
            PS.add (new Acetate(new Random().nextInt(L),new Random().nextInt(D- 3*d_mucus) + d_mucus,
                    0));
        }

        for (int i = 0; i < Prop_count; i++){
            PS.add (new Propionate(new Random().nextInt(L),new Random().nextInt(D),0));
        }

        for (int i = 0; i < But_count; i++){
            PS.add (new Butyrate(new Random().nextInt(L),new Random().nextInt(D), 0));
        }


    }

    //add PS to gut during eating
    public void addPS(java.util.List<Entity> ps){
        if (k_intake >= 1){
            for (int i = 0; i < (k_intake); i++){
                ps.add(0,new PolySaccharides(new Random().nextInt(10),
                        new Random().nextInt(D - 2*d_save_zone- d_mucus) + d_save_zone, 0));
                PS_count+=1;

            }
        }else{
            if ( ticks % (int)(1/k_intake) == 0){
                ps.add(0,new PolySaccharides(new Random().nextInt(10),
                        new Random().nextInt(D- 2*d_save_zone - d_mucus) + d_save_zone, 0));
                PS_count+=1;
            }
        }
    }

    //add PSDifType
    public void addPSGut(java.util.List<Entity> ps){
        if (k_intake >= 1){
            for (int i = 0; i < (k_intake); i++){
                ps.add(PS_count,new PsFromGut(new Random().nextInt(10),
                        new Random().nextInt(D - 2*d_save_zone - d_mucus) + d_save_zone, 0));
                PSFromGut_count+=1;

            }
        }else{
            if ( ticks % (int)(1/k_intake) == 0){
                ps.add(PS_count,new PsFromGut(new Random().nextInt(10),
                        new Random().nextInt(D- 2*d_save_zone  - d_mucus) + d_save_zone, 0));
                PSFromGut_count+=1;
            }
        }
    }

    //write output data in a file
    public void createFile(List<Number> l, PrintWriter writeFile){
        String st = "";
        for (Number n: l) { st += n + " "; }
        st = st + ID;
        writeFile.println(st.trim());
        writeFile.flush();
    }


    //draw objects and environment
    public void draw(Graphics g){
        //gut
        g.drawRect(0,0,dimX,dimY);
        //bacteria and metabolites
        List<Entity> newList = new ArrayList<Entity>() { { addAll(mucus);  addAll(bacterias1);addAll(bacterias2);
            addAll(PS); addAll(T1);addAll(T2);addAll(ANT);} };
        for(Entity ent : newList) {
            ent.draw(g);
        }
        //mucin layer
        g.setColor(new Color(247, 156, 157, 104));
        g.fillRect(0, 0, dimX, (int) (d_save_zone/tickY));
        g.fillRect(0,dimY - (int)(d_save_zone/tickY + stepMucus), dimX, (int)(d_save_zone/tickY));
        //epithelium cells
        if (Gut.ticks % (24 * Gut.getNorm()) == 0){
            g.setColor(new Color(211, 133, 134, 246));
            g.fillRect(0,0,dimX,dimY);
        }

    }

    public void GutToxin2 (){
        //feedback2_1: toxin2 is response to propionate
        if (Gut.KM2.get(0) != 0 & Prop_count - T2.size() > 150) {
            //add toxin2 in bottom and down part of the 2D gut
            T2.add(new Toxin2(new Random().nextInt(L),
                    D - new Random().nextInt(d_save_zone - d_mucus) - 2 *  d_mucus, 0));
            T2.add(new Toxin2(new Random().nextInt(L),
                    new Random().nextInt(d_save_zone -  d_mucus) +  d_mucus, 0));
            //calculate toxin2 count
           // Toxin_count2 += 2;
        }
        //feedback2_2: toxin2 is response to butyrate
        if (Gut.KM2.get(1) != 0 & But_count - T2.size()  > 150) {
            //add toxin2 in bottom and down part of the 2D gut
            T2.add(new Toxin2(new Random().nextInt(L),
                    D - new Random().nextInt(d_save_zone - d_mucus) - 2 *  d_mucus, 0));
            T2.add(new Toxin2(new Random().nextInt(L),
                    new Random().nextInt(d_save_zone -  d_mucus) +  d_mucus, 0));
            //calculate toxins2 count
            //Toxin_count2 += 2;
        }
    }

    public void GutFeedback0(){
        if (PS_count < 7000) {
            Mucus.createPS(PS);
        }
        GutToxin2();
    }

      //feedback with the gut
      public void GutFeedback1 (){
          //all metabolites are added in mucin layer
          //feedback1: produce PS as response to butyrate concentration
          if (But_count < 200 & PS_count < 7000) {
              //type of PSs depended on program settings
                Mucus.createPS(PS);
         }
          GutToxin2();
      }

    public void GutFeedback2(){
      //  Mucus.setK_ps_mucus(15.0/norm);//Double.valueOf(Gut.props.getProperty("k_ps_mucus", "1.00")));
        Mucus.setPeriod((int) (1 / Mucus.getK_ps_mucus()));
        if (But_count < 300 & PSFromGut_count < 7000) {
            Mucus.createPSGut(PS);
        }
        GutToxin2();
    }

    public void GutFeedback3(){
        if (bacterias2.size() > bacterias1.size()) {
            T1.add(new Toxin1(new Random().nextInt(L),
                    D - new Random().nextInt(d_save_zone - d_mucus) - 2 *  d_mucus, 0));
            T1.add(new Toxin1(new Random().nextInt(L),
                    new Random().nextInt(d_save_zone -  d_mucus) +  d_mucus, 0));
        }
        Mucus.createPS(PS);
    }
    public void GutFeedback4(){
        if (But_count > 200) {
            T1.add(new Toxin1(new Random().nextInt(L),
                    D - new Random().nextInt(d_save_zone - d_mucus) - 2 *  d_mucus, 0));
            T1.add(new Toxin1(new Random().nextInt(L),
                    new Random().nextInt(d_save_zone -  d_mucus) +  d_mucus, 0));
        }
        Mucus.createPS(PS);
    }


    //add antibiotics from external environment in the gut
    public void GetAntibiotic () {

        for (int i = 0 ; i < k_ant_intake; i++){
            ANT.add(new Antibiotic(new Random().nextInt(10),

                    new Random().nextInt(D - 2 * d_save_zone  - d_mucus) + d_save_zone - d_mucus/8, 0));
        }
        //calculate antibiotics count
        //Antibiotic_count += k_ant_intake;
    }

    public void Treatment(){
        //get antibiotics in correspond time
        if ( ticks > 54999 & ticks % (AntibioticPeriod*eatPeriod) == 0 & t <= AntibioticsDay*(3/AntibioticPeriod)){//ticks > 24999 &
            ticksAntTime = ticks;
            t+=1;
        }
        if (ticks > 54999 & ticks - ticksAntTime <= TickEat){//ticks > 24999 &
            GetAntibiotic();
        }

        //choose time when get antibiotic
        switch(ticks){
            //case (10): t = 0; AntibioticsDay = 3; break;
            case 55000: t = 0; AntibioticsDay = 3;  break;
            case 95000: t = 0; AntibioticsDay = 5; break;
            case 135000: t = 0; AntibioticsDay = 10; break;
           //case 85000: t = 0; AntibioticsDay = 20; break;
            case 180000: t = 0; AntibioticsDay = 30; break;
            case 240000: t = 0; AntibioticsDay = 200; break;/*
            case 25000: t = 0; AntibioticsDay = 30; break;
            case 85000: t = 0; AntibioticsDay = 30; break;
            case 135000: t = 0; AntibioticsDay = 30; break;*/
        }
    }



    //perturbation: Change type of PS from external environment
    public void ChangeEat(List<Entity> ps){
        if (ticks > 25000){
            addPSGut(ps);
        }else{
            addPS(ps);
        }
    }

    //action for each component in a list
    public void Action(List<Entity> Ent) {
       for (int i = 0; i < Ent.size(); i++){
           Entity ent = Ent.get(i);
            try{
               ent.tick(PS,T1,T2, ANT,bacterias1,bacterias2,this);
            }catch(IOException e){
                e.printStackTrace();
            }
            if (! ent.getLive()){
                Ent.remove(i);
            }
        }

    }

    public void actionCore(){
        //create LogFile and 1 string
        if (ticks == 0){
            try {
                writeFile = new PrintWriter(logFile);
                String st = "Ticks AcetateCount ButyrateCount PropionateCount Toxin1Count Toxin2Count AntibioticCount PsGutCount" +
                        " PSCount Bacteria_1 Res1 Bacteria_2 Res2 MR ID";
                writeFile.println(st);
            } catch (IOException i) {
                i.printStackTrace();
            }
        }


        //change current time
        ticks += 1;
        //set mutation value
        Bacteria.setMutation_rate(ANT.size());
        //create a list with output data
        ProdAmount = new ArrayList<Number> ();
        ProdAmount.add(ticks);
        ProdAmount.add(Acet_count);
        ProdAmount.add(But_count);
        ProdAmount.add(Prop_count);
        ProdAmount.add(T1.size());
        ProdAmount.add(T2.size());
        ProdAmount.add(ANT.size());
        ProdAmount.add(PSFromGut_count);
        ProdAmount.add(PS_count);
        ProdAmount.add(bacterias1.size());
        ProdAmount.add(Bacteria_1_res_count);
        ProdAmount.add(bacterias2.size());
        ProdAmount.add(Bacteria_2_res_count);
        ProdAmount.add(Bacteria.getMutation_rate());

        //write these output data in a file
        createFile(ProdAmount, writeFile);

        Bacteria_1_res_count = 0;
        Bacteria_2_res_count = 0;


        //action for each objects list
        Action(bacterias1);
        Action(bacterias2);
        Action(PS);
        Action(T1);
        Action(T2);
        Action(ANT);

        //set least eating time
        if (ticks % eatPeriod == 0){
            ticksEatTime = ticks;
        }

        //add PS during eating
        if (ticks - ticksEatTime <= TickEat){
            addPS(PS);
            //ChangeEat(PS);
        }
    }

    public void action0 () {

             actionCore();

            //add PS from Gut in mucin layer
            GutFeedback0();

            //take antibiotics
            Treatment();
            //check current time & bacteria count
            if (ticks == tickslimit || Math.abs(bacterias1.size() - Gut.Bacteria_1_res_count) < 4 || Math.abs(bacterias2.size() - Gut.Bacteria_2_res_count) < 4) {
                System.exit(0);
                writeFile.close();
            }


    }

    public void action1 () {

        actionCore();

        //add PS from Gut in mucin layer
        GutFeedback1();

        //take antibiotics
        Treatment();

        //check current time & bacteria count
        if (ticks == tickslimit ||  Math.abs(bacterias1.size() - Gut.Bacteria_1_res_count) < 4 || Math.abs(bacterias2.size() - Gut.Bacteria_2_res_count) < 4){
            System.exit(0);
            writeFile.close();
        }


    }

    public void action2 () {

        actionCore();

        //add PS from Gut in mucin layer
        GutFeedback2();

        //take antibiotics
        Treatment();

        //check current time & bacteria count
        if (ticks == tickslimit || Math.abs(bacterias1.size() - Gut.Bacteria_1_res_count) < 4 || Math.abs(bacterias2.size() - Gut.Bacteria_2_res_count) < 4) {
            System.exit(0);
            writeFile.close();
        }



    }

    public void action3 () {

        actionCore();

        //add PS from Gut in mucin layer
        GutFeedback3();

        //take antibiotics
        Treatment();

        //check current time & bacteria count
        if (ticks == tickslimit || Math.abs(bacterias1.size() - Gut.Bacteria_1_res_count) < 4 || Math.abs(bacterias2.size() - Gut.Bacteria_2_res_count) < 4) {
            System.exit(0);
            writeFile.close();
        }



    }

    public void action4 () {

        actionCore();

        //add PS from Gut in mucin layer
        GutFeedback4();

        //take antibiotics
        Treatment();

        //check current time & bacteria count
        if (ticks == tickslimit || Math.abs(bacterias1.size() - Gut.Bacteria_1_res_count) < 4 || Math.abs(bacterias2.size() - Gut.Bacteria_2_res_count) < 4) {
            System.exit(0);
            writeFile.close();
        }



    }


}
