/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

import java.util.ArrayList;

/**
 *
 * @author thomas
 */
public class BalanceMonitor implements Observer {
    Subject subject;

    private ArrayList<User> users = new ArrayList<>();
    
    @Override
    public void attach(User u) {
        users.add(u);
    }

    @Override
    public void detach(User u) {
        users.remove(u);
    }

    @Override
    public void update() {
        for(User u : users) {
            if(u.getBalance() < 10) {
                System.out.println(u.username + " il tuo credito è quasi esaurito");
            }
        }
    }
    
}
