package masi_delmoro_swe;

public class WrongNameException extends Exception {
    WrongNameException(){
        super("Il nome inserito non è coretto");
    }
}
