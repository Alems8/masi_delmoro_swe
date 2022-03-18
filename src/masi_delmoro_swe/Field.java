/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
/**
 *
 * @author Alessio
 */
public class Field {
    private String name;
    public Map<Date, ArrayList<Integer>> timeTable = new HashMap<>();
    
    
    public Field(String name, ArrayList hours) { //MODIFICATO
        this.name = name;

        Date day = new Date();
        
        timeTable.put(day, hours);
    }
}
