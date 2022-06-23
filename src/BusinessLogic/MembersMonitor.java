package BusinessLogic;

import DomainModel.Club;
import DomainModel.UserClub;
import DAO.ClubDao;
import DAO.FakeClubDao;
import DAO.FakeUserDao;
import DAO.UserDao;
import ObserverUtil.Observer;
import ObserverUtil.Subject;
import DomainModel.Person;
import DomainModel.User;

public class MembersMonitor implements Observer {
    private static MembersMonitor instance = null;

    private ClubDao clubDao;
    private UserDao userDao;

    private MembersMonitor(){
        this.clubDao = FakeClubDao.getInstance();
        this.userDao = FakeUserDao.getInstance();
    }
    static MembersMonitor getInstance() {
        if(instance == null)
            instance = new MembersMonitor();
        return instance;
    }
    @Override
    public void update(Object newMember, Subject club) {
        UserClub uc = clubDao.getClubByName(((Club)club).name);
        User user = userDao.getUserByPerson((Person)newMember);
        if(!uc.isMember(user)){
            uc.addMember(user);
        }

    }
}
