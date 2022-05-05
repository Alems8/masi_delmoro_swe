package BookingManager;

public class WrongNameException extends Exception {
    WrongNameException(){
        super("Il nome inserito non è coretto");
    }
}
