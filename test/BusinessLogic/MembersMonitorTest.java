package BusinessLogic;

import DomainModel.Club;
import DomainModel.Person;
import DomainModel.User;
import DomainModel.UserClub;
import ObserverUtil.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MembersMonitorTest {
    RequestManager rm;
    User user20;
    Person person12;
    Club club7;
    UserClub userClub7;

    @BeforeEach
    void setup(){
        this.rm = RequestManager.getInstance();
        this.person12 = new Person("Mattia", "Franchi", "mattifra@gmail.com");
        this.user20 = person12.subscribe(rm, "manchi");
        this.club7 = new Club("Peretola", 10,18,25);
        this.userClub7 = club7.subscribe(rm, 90);
    }
    @Test
    void update() {
        Observer obs = MembersMonitor.getInstance();
        obs.update(person12, club7);
        assertTrue(userClub7.isMember(user20));
    }
}