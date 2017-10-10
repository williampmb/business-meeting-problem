/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessmeetingproblem;

import java.util.List;

/**
 *
 * @author willi
 */
public class Card {

    private int idPeople;
    private char gender;

    public int getIdPeople() {
        return idPeople;
    }

    public void setIdPeople(int idPeople) {
        this.idPeople = idPeople;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public Card(int id, char gender) {
        this.idPeople = id;
        this.gender = gender;
    }

    public static int checkNumberOfGender(List<Card> cards, char gender) {
        int numb = 0;
        for (Card card : cards) {
            if (card.gender == gender) {
                numb++;
            }
        }
        return numb;
    }

    public static boolean containsId(List<Card> cards, int id) {
        for (Card card : cards) {
            if (card.idPeople == id) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString(){
        return "["+gender+""+idPeople+"]";
    }

}
