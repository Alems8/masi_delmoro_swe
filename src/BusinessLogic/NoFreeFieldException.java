package BusinessLogic;

public class NoFreeFieldException extends Exception {

    NoFreeFieldException(){
        super("Non ci sono campi disponibili");
    }
}
