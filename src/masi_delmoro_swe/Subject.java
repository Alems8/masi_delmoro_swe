/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

/**
 *
 * @author thomas
 */
public interface Subject {
    
    void subscribe();
    void unsubscribe();
    void notifyChanges();
    
}
