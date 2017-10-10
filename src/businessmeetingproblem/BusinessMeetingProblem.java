/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessmeetingproblem;

/**
 *
 * @author willi
 */
public class BusinessMeetingProblem {

    /**
     * @param args the command line arguments
     */
    //Throw an interrupt exception when all threads have entered but they cannot
    //leave the room because of cards conditions.
    public static void main(String[] args) {
        Room r = Room.getInstance();
        for (int i = 1; i < 21; i++) {
            Person p = new Person(i);
            new Thread(p).start();
        }

    }

}
