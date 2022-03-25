/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masi_delmoro_swe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author aleal
 */
public class Booking {
    protected Club club;
    protected Field field;
    protected LocalDate date;
    protected int hour;

    public Booking(Club club, Field field, LocalDate date, int hour) {
        this.club = club;
        this.field = field;
        this.date = date;
        this.hour = hour;
    }

    public Club getClub() {
        return club;
    }

    public Field getField() {
        return field;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }
}
