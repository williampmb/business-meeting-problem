/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessmeetingproblem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author willi
 */
public class Room {

    private final int capacity;
    private static Room room;
    private List<People> inside = new ArrayList<People>();

    //locks
    private Object doorMan = new Object();
    private Object checkDisponibility = new Object();

    static Room getInstance() {
        if (room == null) {
            room = new Room(5);
        }
        return room;
    }

    private Room(int cap) {
        this.capacity = cap;
    }

    boolean tryEnterIntoRoom(People guest) {
        //it avoids two threads trying to access the same variable with synch method.
        //if the room is full, it waits until it gets more space.
        synchronized (doorMan) {
            while (inside.size() >= capacity) {
                try {
                    System.out.println("Room FULL " + guest.name);
                    wait();
                    System.out.println(guest.name + " Trying again");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println(guest.name + " Have Entered in the ROOM");
            inside.add(guest);
        }

        return true;
    }

    void leaveTheRoom(People guest) {
        synchronized (doorMan) {
            inside.remove(guest);
            notifyAll();
        }
        System.out.println(guest.name + " Left the ROOM. Opening a spot");
    }

    void LookForTradeCards(People guest) {
        People toTrade = null;
        synchronized (checkDisponibility) {
            for (People p : inside) {
                boolean trade = guest.itIsPossibleTradeCardWith(p);
                if (trade) {
                    toTrade = p;
                    break;
                }
            }
            toTrade.setFree(false);
            guest.setFree(false);
        }

        if (toTrade != null) {
            try {
                guest.tradeCard(toTrade);
                toTrade.tradeCard(guest);
            } catch (InterruptedException ex) {
                Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                toTrade.setFree(true);
                guest.setFree(true);
            }

        }
    }

}
