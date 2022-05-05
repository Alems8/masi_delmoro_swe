/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Club;

import Sport.Sport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
//import java.util.Date;
/**
 *
 * @author Alessio
 */
public class Field {
    public String name;
    public Sport sport;
    public Map<LocalDate, ArrayList<Integer>> timeTable = new HashMap<>();
    
    
    public Field(String name, Sport sport, ArrayList hours) { //MODIFICATO
        this.name = name;
        this.sport = sport;
        
        LocalDate day = LocalDate.now();
        
        timeTable.put(day, hours);
    }
    
    
}
