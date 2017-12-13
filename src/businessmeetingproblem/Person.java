/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessmeetingproblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author willi
 */
public class Person extends Thread {

    public boolean inside = false;
    int id;
    String namePerson;
    private boolean free;

    private char gender;
    private List<Card> cards;

    public Person(int id) {
        this.id = id;
        this.free = false;
        gender = genenrateGender();
        cards = new ArrayList<Card>();
        namePerson = this.gender + " " + this.id;
    }

    @Override
    public void run() {
        Room r = Room.getInstance();

        long startTime = System.currentTimeMillis();
        long endTime = 0;
        //it tries to enter in the room
        try {
            enterRoom(r);
        } catch (InterruptedException e) {
            //Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, e);
            String write = byebye(startTime, endTime);
            BusinessMeetingProblem.pw.append(write);
            BusinessMeetingProblem.pw.flush();
            return;
        }

        for (int times = 0; times < 3; times++) {
            r.LookForTradeCards(this);
        }

        synchronized (this) {
            while (cards.size() != 3) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    //Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
                    String write = byebye(startTime, endTime);
                    BusinessMeetingProblem.pw.append(write);
                    BusinessMeetingProblem.pw.flush();
                    return;
                }
            }
        }

        try {
            System.out.println(namePerson + " Trying to leave the room");
            leaveRoom(r);
            endTime = System.currentTimeMillis();
        } catch (Exception ex) {
            //Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
        }
        String write = byebye(startTime, endTime);
        BusinessMeetingProblem.pw.append(write);
        BusinessMeetingProblem.pw.flush();

    }

    public String byebye(long startTime, long endTime) {
        String time = calculateTime(startTime, endTime);
        String deck = printDeck();
        String write = time + " " + deck + "\n";
        return write;
    }

    public void enterRoom(Room r) throws InterruptedException {
        inside = r.tryEnterIntoRoom(this);
    }

    private void leaveRoom(Room r) {
        r.leaveTheRoom(this);
        inside = false;
    }

    private char genenrateGender() {
        Random rdm = new Random();
        double possibility = rdm.nextDouble();
        if (possibility > 0.50) {
            return 'M';
        }
        return 'W';
    }

    boolean itIsPossibleTradeCardWith(Person p) {
        if (this.id == p.id || !p.isFree() || p.cards.size() > 2 || cards.size() > 2) {
            return false;
        }
        if (Card.containsId(p.cards, this.id) || Card.containsId(this.cards, p.id)) {
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

    public String getNamePerson() {
        return namePerson;
    }

    public void setNamePerson(String name) {
        this.namePerson = name;
    }

    void tradeCard(Person toTrade) throws InterruptedException {
        cards.add(new Card(toTrade.id, toTrade.gender));
        System.out.println(printDeck());
    }

    private String printDeck() {
        String deck = "";
        for (Card c : cards) {
            deck += c.toString() + "|";
        }
        //System.out.print("\n" + name + " " + "Deck: |" + deck + "\n");
        return namePerson + " " + "Deck: |" + deck + "\n";
    }

    private String calculateTime(long start, long end) {
        if (end == 0) {
            return "Not Finished";
        }

        long time = (end - start) / 1000;
        return time + " seconds";
    }

}
