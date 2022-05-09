package BookingManager;

public class NoMemberException extends Exception {

    NoMemberException(){
        super("L'utente non è membro del club");
    }
}
