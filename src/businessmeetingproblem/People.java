/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessmeetingproblem;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author willi
 */
public class People implements Runnable {

    private boolean inside = false;
    int id;
    String name;

    public People(int id) {
        this.id = id;
        name = "Thread " + this.id;
    }

    @Override
    public void run() {
        System.out.println("Executing " + name);
        Room r = Room.getInstance();
        enterRoom(r);
        try {
            Thread.sleep(4000);
            System.out.println(name + " Trying to leave the room");
            leaveRoom(r);
        } catch (InterruptedException ex) {
            Logger.getLogger(People.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void enterRoom(Room r) {

        try {
            System.out.println(name + " Trying to enter in the room");
            inside = r.tryEnterIntoRoom(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void leaveRoom(Room r) {
        r.leaveTheRoom(this);

    }

}
