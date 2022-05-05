/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Booking;

import Club.Club;
import Club.Field;
import BookingManager.User;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author thomas
 */
public class PrivateBooking extends Booking {

    public PrivateBooking(Club club, Field field, LocalDate date, Integer hour, ArrayList<User> players) {
        super(club, field, date, hour, players);
    }
}
