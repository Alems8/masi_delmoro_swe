package BusinessLogic;

public class AlreadySubscribedException extends Exception{

    AlreadySubscribedException(){
        super("Utente già registrato");
    }
}
