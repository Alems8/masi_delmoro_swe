package BusinessLogic;

import Club.Club;
import Club.UserClub;
import ObserverUtil.Observer;
import ObserverUtil.Subject;
import User.Person;
import User.User;

public class MembersMonitor implements Observer {
    private static MembersMonitor instance = null;
    private final BookingManager bm;

    private MembersMonitor(BookingManager bm){
        this.bm = bm;
    }
    static MembersMonitor getInstance(BookingManager bm) {
        if(instance == null)
            instance = new MembersMonitor(bm);
        return instance;
    }
    @Override
    public void update(Object clubMembers, Subject club) {
        int i = 0;
        boolean found = false;
        UserClub uc = null;
        while(!found && i < bm.getClubsSize()){
            uc = bm.getClub(i);
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
                while(!foundUser && k < bm.getUsersSize()){
                    u = bm.getUser(k);
                    if(u.getPerson().equals(person))
                        foundUser = true;
                    k++;
                }
                uc.addMember(u);
            }
        }
    }
}
