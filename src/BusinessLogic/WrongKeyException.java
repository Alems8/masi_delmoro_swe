package BusinessLogic;

public class WrongKeyException extends Exception {

    WrongKeyException() {
        super("La chiave della prenotazione è sbagliata");
    }
}
