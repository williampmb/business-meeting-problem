/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessmeetingproblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author willi
 */
public class Person implements Runnable {

    private boolean inside = false;
    int id;
    String name;
    private boolean free;

    private char gender;
    private List<Card> cards;

    Lock lock = new ReentrantLock();

    public Person(int id) {
        this.id = id;
        name = "Thread " + this.id;
        this.free = false;
        gender = genenrateGender();
        cards = new ArrayList<Card>();
    }

    @Override
    public void run() {
        System.out.println("Executing " + name);
        Room r = Room.getInstance();

        //it tries to enter in the room
        enterRoom(r);

        for (int times = 0; times < 3; times++) {
            r.LookForTradeCards(this);
        }

        try {
            Thread.sleep(4000);
            System.out.println(name + " Trying to leave the room");
            leaveRoom(r);
        } catch (InterruptedException ex) {
            Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void enterRoom(Room r) {
        System.out.println(name + " Trying to enter in the room");
        inside = r.tryEnterIntoRoom(this);
    }

    private void leaveRoom(Room r) {
        r.leaveTheRoom(this);
    }

    private char genenrateGender() {
        Random rdm = new Random();
        double possibility = rdm.nextDouble();
        if (possibility > 50) {
            return 'M';
        }
        return 'W';
    }

    boolean itIsPossibleTradeCardWith(Person p) {
        if (this.id == p.id || !p.isFree()) {
            return false;
        }
        if(Card.containsId(p.cards, this.id) || Card.containsId(this.cards, p.id)){
            return false;
        }
        int thisNumbCardsOfGenderOfP = Card.checkNumberOfGender(cards, p.gender);
        int pNumbCardsOfGenderOfThis = Card.checkNumberOfGender(p.cards, this.gender);
        if (thisNumbCardsOfGenderOfP < 2 && pNumbCardsOfGenderOfThis < 2) {
            return true;
        }
        return false;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public boolean isInside() {
        return inside;
    }

    public void setInside(boolean inside) {
        this.inside = inside;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    void tradeCard(Person toTrade) throws InterruptedException {
        cards.add(new Card(toTrade.id, toTrade.gender));
        Thread.sleep(2000);
    }

}
