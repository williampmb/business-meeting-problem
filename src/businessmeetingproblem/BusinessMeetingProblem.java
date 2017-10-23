/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessmeetingproblem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author willi
 */
public class BusinessMeetingProblem {

    static PrintWriter pw;

    /**
     * @param args the command line arguments
     */
    //Throw an interrupt exception when all threads have entered but they cannot
    //leave the room because of cards conditions.
    public static void main(String[] args) {
        writeReport();

        Room r = Room.getInstance();
        for (int i = 1; i < 21; i++) {
            Person p = new Person(i);
            p.start();
            //new Thread(p).start();
        }

        try {
            Thread.sleep(120000);
        } catch (InterruptedException ex) {
            Logger.getLogger(BusinessMeetingProblem.class.getName()).log(Level.SEVERE, null, ex);
        }

        Thread[] people = new Thread[21];
        Thread.enumerate(people);

        for (Thread p : people) {
            p.interrupt();
        }

    }

    private static void writeReport() {
        File log = new File("log.txt");

        try {
            if (!log.exists()) {
                log.createNewFile();
            }
            pw = new PrintWriter(new FileOutputStream(log, true));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(BusinessMeetingProblem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BusinessMeetingProblem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
