package masi_delmoro_swe;

public class PendingBookingException extends Exception{
    PendingBookingException(){
        super("Hai delle prenotazioni in sospeso");
    }
}
