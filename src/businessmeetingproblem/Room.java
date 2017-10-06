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
public class Room {

    private final int capacity;
    private int peopleInside;
    private static Room room;

    static Room getInstance() {
        if (room == null) {
            room = new Room(5);
        }
        return room;
    }

    private Room(int cap) {
        this.capacity = cap;
        this.peopleInside = 0;
    }

    boolean tryEnterIntoRoom(People guest) {
        synchronized (this) {
            while (peopleInside >= capacity) {
                try {
                    System.out.println("Room FULL " + guest.name);
                    wait();
                    System.out.println(guest.name + " Trying again");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println(guest.name + " Have Entered in the ROOM");
            peopleInside++;
        }

        return true;
    }

    void leaveTheRoom(People guest) {
        synchronized (this) {
            peopleInside--;
            notifyAll();
        }
        System.out.println(guest.name + " Left the ROOM. Opening a spot");
    }

}
