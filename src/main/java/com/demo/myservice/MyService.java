package com.demo.myservice;

import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Replace this with an overview of the application that answers the questions
 * "what is this?" and "why would I want to use it?".
 */
public class MyService implements Runnable {
    static final Logger LOGGER = Logger.getLogger(MyService.class.getName());
    
    static private final String USAGE = "replace this with stuff that will appear after 'usage:'";
    static private final String HEADER = "replace this with a brief summary describing what the application does.\nOptions are:";
    static private final String FOOTER = "\n"
            +"Replace this with a longer description of the application "
            +"that answers 'what does this do?' and 'why should I use it?'.\n"
            +"\n"
            +"I like to use a blank line to separate paragraphs.\n"
            +"\n";
    static private final Options OPTIONS;
    static {
        OPTIONS = new Options();
        OPTIONS.addOption("h","help",false,"Print this message.");
	OPTIONS.addOption("v","verbose",false,"Turn on verbose output.");
        OPTIONS.addOption(null,"debug",false,"force log4j's level to DEBUG");
        OPTIONS.addOption(null,"l4jconfig",true,"Path to the log4j configuration file [./l4j.lcf]");
    }
    
    static private MyService application;
    
    /**
     * Run as application or Service.
     * @param args Run with --help to see usage message.
     */
    static public void main(String[] args) { // 
        try {
            CommandLine cmdline = (new DefaultParser()).parse(OPTIONS,args);
            if (cmdline.hasOption("help")) {
                (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
                System.exit(1);
            }
            configureLog4j(cmdline.getOptionValue("l4jconfig","l4j.lcf"),cmdline.hasOption("debug"));
        
            application = new MyService(cmdline.hasOption("verbose"));
            application.run();
        } catch (ParseException ex) {
            // can't use logger; it's not configured
            System.err.println(ex.getMessage());
            (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
        }
    }
    
    /**
     * Stop the application or Service.
     * @param args Not used
     */
    static public void stop(String[] args) {
        application.stop();
    }
    
    /**
     * Configures log4j.
     * @param l4jconfig null or path to the configuration file.
     * @param debug if true, forces Level.DEBUG.
     */
    static void configureLog4j(String l4jconfig,boolean debug) {
        if (l4jconfig != null && (new File(l4jconfig)).canRead()) {
            if (l4jconfig.matches(".*\\.xml$")) {
                DOMConfigurator.configureAndWatch(l4jconfig);
            } else {
                PropertyConfigurator.configureAndWatch(l4jconfig);
            }
        } else {
            BasicConfigurator.configure();
        }
        if (debug) LOGGER.setLevel(Level.DEBUG);
    }

    private final boolean verbose;
    private Thread runThread;
    private boolean running;
    
    public MyService(boolean verbose) {
        this.verbose = verbose;
    }
    
    public void stop() {
        running = false;
        if (runThread != null && runThread.isAlive()) {
            runThread.interrupt();
        }
    }

    @Override
    public void run() {
	LOGGER.info("run()");
        runThread = Thread.currentThread();
        running = true;
        while (running) {
            try {
                LOGGER.info("do something");
                if (running) Thread.sleep(1000L);
            } catch (InterruptedException ignore) {
                LOGGER.info("interrupted!");
            }
        }
    }
}
