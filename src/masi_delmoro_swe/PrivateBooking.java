/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author thomas
 */
public class PrivateBooking extends Booking {
    private ArrayList<User> users;

    public PrivateBooking(Club club, Field field, LocalDate date, Integer hour, ArrayList<User> users) {
        super(club, field, date, hour);
        this.users = users;
    }
}
