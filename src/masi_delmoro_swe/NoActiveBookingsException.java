package masi_delmoro_swe;

public class NoActiveBookingsException extends Exception{

    NoActiveBookingsException(){
        super("L'utente non ha prenotazioni attive");
    }
}
