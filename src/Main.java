/**
 * main program class, which run simulation
 */

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.*;
import java.awt.image.BufferedImage;
import org.apache.commons.cli.*;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static Options options = new Options();
    //private String[] args = null;
    static boolean PLOT_GRAPHICS = false;
    static int  LABEL_FEEDBACK = 1;
    static String logFile_name = "";
    static String setFile_name = "";


    public static void main(String[] args) {

//Integer.parseInt(args[4]);

        Option o = OptionBuilder.withArgName("output file").hasArg()
                .withDescription("use given file for writing output information")
                .create("o");
        Option i = OptionBuilder.withArgName("input file").hasArg()
                .withDescription("use given file for set initial values")
                .create("i");

        Option visual = new Option("v", "visual", false, "if you wont to run visual version");
        Option help = new Option("h", "help", false, "help message");
        Option mod =  OptionBuilder.withArgName("number").hasArg()
                .withDescription("write type of model")
                .create("mod");
        options.addOption(o);
        options.addOption(visual);
        options.addOption(i);
        options.addOption(mod);
        options.addOption(help);

        parseCMD(args);

        // create simple gut object
        Gut g1 = new Gut();

        //create initial objects in the gut & set its properties
        try {
            g1.SetProperty(logFile_name);//args[2]
            g1.logBacteriaProperty(Gut.BacteriaProperty1, Gut.BacteriaProperty2);
            g1.logBacteriaPropertyRes(Gut.BacteriaProperty1,Gut.BacteriaProperty1_Res);
            g1.logBacteriaPropertyRes(Gut.BacteriaProperty2,Gut.BacteriaProperty2_Res);
            g1.addEntity();
            }
        catch (IOException e) {
            e.printStackTrace();
        }

        // run GUI or Console
        if (PLOT_GRAPHICS) {
            runGui(g1,LABEL_FEEDBACK);
        } else {
            runConsole(g1, LABEL_FEEDBACK);
        }



    }


    public static void help() {
        //this prints out some help
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "InteractionsGutModel", options);
        System.exit(0);
    }

    public static void parseCMD(String[] args){
        CommandLineParser parser = new DefaultParser();

        try{
            CommandLine line = parser.parse( options, args);

            if (line.hasOption("h")){
                help();
            }


            if (line.hasOption("o")) {
                //File logFile = new File(line.getOptionValue("logfile"));
                logFile_name = line.getOptionValue("o");
            } else {
                log.log(Level.SEVERE, "Missing output file");
                help();
            }

            if (line.hasOption("v")){
                PLOT_GRAPHICS = true;
            }
            if (line.hasOption("i")) {
                setFile_name = line.getOptionValue("i");
                try{
                    Gut.props.load(new FileInputStream(setFile_name));}
                catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                log.log(Level.SEVERE, "Missing input file");
                help();
            }
            if (line.hasOption("mod")){
                LABEL_FEEDBACK = Integer.parseInt(line.getOptionValue("mod"));
            }else{
                log.log(Level.SEVERE, "Missing mod value");
                help();
            }


        } catch( ParseException exp){
            System.err.println("Parsing failed. Reason: " + exp.getMessage());
            help();

        }
    }

    //visual version
    private static void runGui(Gut gut, int LABEL_FEEDBACK){
        // Create jframe
        JFrame app = new JFrame("Gut");
        app.setIgnoreRepaint( true );
        app.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );


        // Create canvas for painting...
        Canvas canvas = new Canvas();
        canvas.setIgnoreRepaint( true );
        canvas.setSize(Gut.getDimX() , Gut.getDimY());

        // Add canvas to jframe
        app.add( canvas );
        app.pack();
        app.setVisible( true );

        // Create BackBuffer
        canvas.createBufferStrategy( 2 );
        BufferStrategy buffer = canvas.getBufferStrategy();

        // Get graphics configuration
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        // Create off-screen drawing surface
        BufferedImage bi = gc.createCompatibleImage( Gut.getDimX() , Gut.getDimY());

        // Objects needed for rendering
        Graphics graphics = null;
        Graphics2D g2d = null;

        // Variables for counting time

        while( true ) {

            try {

            //    if( Gut.ticks != Gut.tickslimit ) {

                    // clear back buffer...
                    g2d = bi.createGraphics();
                    g2d.fillRect( 0, 0, Gut.getDimX() , Gut.getDimY());

                    // move and draw object
                    gut.draw(g2d);
                    switch(LABEL_FEEDBACK) {
                        //only FB T-AT,Gut_Produce PS for all bacterial type
                        case 0: gut.action0();break;
                        ///PS is response on butyrate
                        case 1: gut.action1();break;
                        //PSGut is response on butyrate
                        case 4: gut.action2();break;
                        //T1 is response in b2 - b1
                        case 2: gut.action3();break;
                        //T1 is response on butyrate
                        case 3: gut.action4();break;

                    }

                    // Blit image and flip
                    graphics = buffer.getDrawGraphics();
                    graphics.drawImage( bi, 0, 0, null );
                    if( !buffer.contentsLost() )
                        buffer.show();
            //    }

                Thread.yield();

            } finally {
                // release resources
                if( graphics != null )
                    graphics.dispose();
                if( g2d != null )
                    g2d.dispose();
            }

        } //throw ()
    }


    //console version
    private static void runConsole(Gut gut,int LABEL_FEEDBACK) {

        while( true ) {
           //  if (Gut.ticks != Gut.tickslimit){
                 //run simulation
                 switch(LABEL_FEEDBACK) {
                     //only FB T-AT,Gut_Produce PS for all bacterial type
                     case 0: gut.action0();break;
                     ///PS is response on butyrate
                     case 1: gut.action1();break;
                     //PSGut is response on butyrate
                     case 4: gut.action2();break;
                     //T1 is response in b2 - b1
                     case 2: gut.action3();break;
                     //T1 is response on butyrate
                     case 3: gut.action4();break;

                 }
            Thread.yield();
          //   }
       }
    }


}