package BusinessLogic;

import Club.Club;
import Club.UserClub;
import DAO.ClubDao;
import DAO.FakeClubDao;
import DAO.FakeUserDao;
import DAO.UserDao;
import ObserverUtil.Observer;
import ObserverUtil.Subject;
import User.Person;
import User.User;

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
    public void update(Object clubMembers, Subject club) {
        int i = 0;
        boolean found = false;
        UserClub uc = null;
        while(!found && i < clubDao.getClubsSize()){
            uc = clubDao.getClub(i);
            if(uc.getClub().equals(club))
                found = true;
            i++;
        }

        for(i=0; i<((Club)club).getMembersSize(); i++){
            Person person = ((Club)club).getMember(i);
            int j = 0;
            found = false;
            while(!found && j < uc.getMembersSize()){
                if(uc.getMember(j).getPerson().equals(person))
                    found = true;
                j++;
            }
            if(!found){
                int k = 0;
                boolean foundUser = false;
                User u = null;
                while(!foundUser && k < userDao.getUsersSize()){
                    u = userDao.getUserById(k);
                    if(u.getPerson().equals(person))
                        foundUser = true;
                    k++;
                }
                uc.addMember(u);
            }
        }
    }
}
