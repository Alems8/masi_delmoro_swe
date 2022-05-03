package masi_delmoro_swe;

public class LowBalanceException extends Exception {
    LowBalanceException(){
        super("Non hai abbastanza fondi per una prenotazione");
    }
}
