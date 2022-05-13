/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookingManager;

import Observer.Observer;
import Observer.Subject;

/**
 *
 * @author thomas
 */
public class BalanceMonitor implements Observer {

    @Override
    public void update(Object userBalance, Subject user) {
        if((int) userBalance < 10)
            System.out.println(((User)user).username + ": il tuo credito si sta esaurendo");
    }
    
}
