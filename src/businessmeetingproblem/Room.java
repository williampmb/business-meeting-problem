/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessmeetingproblem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author willi
 */
public class Room {

    private final int capacity;
    private static Room room;
    private List<Person> inside = new ArrayList<Person>();

    //locks
    private Object doorMan = new Object();
    Lock lock = new ReentrantLock();
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

    boolean tryEnterIntoRoom(Person guest) throws InterruptedException {
        //it avoids two threads trying to access the same variable with synch method.
        //if the room is full, it waits until it gets more space.
        synchronized (doorMan) {
            while (inside.size() >= capacity) {

                System.out.println("Room FULL " + guest.namePerson);
                doorMan.wait();
                System.out.println(guest.namePerson + " Trying again");

            }
            System.out.println(guest.namePerson + " Have Entered");
            lock.lock();
            try {
                inside.add(guest);
                whoIsThere();
            } finally {
                lock.unlock();
            }
            guest.setFree(true);
        }

        return true;
    }

    void leaveTheRoom(Person guest) {
        synchronized (doorMan) {
            lock.lock();
            try {
                inside.remove(guest);
                whoIsThere();
            } finally {
                lock.unlock();
            }
            doorMan.notifyAll();
        }

        System.out.println(guest.namePerson + " Left the ROOM. Opening a spot");
    }

    void LookForTradeCards(Person guest) {
        Person toTrade = null;
        boolean trade = false;
        // synchronized (checkDisponibility) {
        lock.lock();
        try {
            for (Person p : inside) {
                trade = guest.itIsPossibleTradeCardWith(p);
                if (trade) {
                    toTrade = p;
                    toTrade.setFree(false);
                    guest.setFree(false);
                    break;
                }
            }
            if (toTrade != null) {
                synchronized (toTrade) {
                    try {
                        guest.tradeCard(toTrade);
                        toTrade.tradeCard(guest);
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        toTrade.setFree(true);
                        guest.setFree(true);
                        toTrade.notifyAll();
                    }
                }

            }
            // }
        } finally {
            lock.unlock();
        }

    }

    private void whoIsThere() {
        System.out.print("| ");
        int wom = 0;
        int men = 0;
        for (Person p : inside) {
            System.out.print(p.namePerson + "|");
            if (p.getGender() == 'W') {
                wom++;
            } else {
                men++;
            }
        }
        System.out.println("");
        System.out.println("Woman: " + wom + " Men: " + men);
    }

}
