/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

import ObserverUtil.Observer;
import ObserverUtil.Subject;
import DomainLogic.User;

/**
 *
 * @author thomas
 */
public class BalanceMonitor implements Observer {
    private static BalanceMonitor instance = null;

    private BalanceMonitor() {}

    static BalanceMonitor getInstance(){
        if(instance == null)
            instance = new BalanceMonitor();
        return instance;
    }

    @Override
    public void update(Object userBalance, Subject user) {
        if((int) userBalance < 10)
            System.out.println(((User)user).username + ": il tuo credito si sta esaurendo");
    }
    
}
