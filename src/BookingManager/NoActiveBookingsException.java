package BookingManager;

public class NoActiveBookingsException extends Exception{

    NoActiveBookingsException(){
        super("L'utente non ha prenotazioni attive");
    }
}
