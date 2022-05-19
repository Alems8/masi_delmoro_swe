package BusinessLogic;

public class PendingBookingException extends Exception{
    PendingBookingException(){
        super("Hai delle prenotazioni in sospeso");
    }
}
