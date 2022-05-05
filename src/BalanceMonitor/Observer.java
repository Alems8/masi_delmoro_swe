/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BalanceMonitor;

import BookingManager.User;

/**
 *
 * @author thomas
 */
public interface Observer {
    
    void attach(User u);
    void detach(User u);
    void update();
    
}
