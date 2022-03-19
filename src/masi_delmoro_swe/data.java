/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

/**
 *
 * @author Alessio
 */
public class data {
    private int day;
    private int month;
    
    public data(int i) {
        if(i<=31) {
            this.day = i;
            this.month = 1;
        }
        if(i>31 && i<=59) {
            this.day = i-31;
            this.month = 2;
        }
        //FIX ME
    }
}
