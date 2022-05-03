package masi_delmoro_swe;

public class NoFreeFieldException extends Exception {

    NoFreeFieldException(){
        super("Non ci sono campi disponibili");
    }
}
