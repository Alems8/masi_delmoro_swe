package Club;

import User.User;
import java.util.ArrayList;

public class UserClub {
    private final Club club;
    private final ArrayList<User> members = new ArrayList<>();
    public int joinClubPrice;

    public UserClub(Club club, int joinClubPrice) {
        this.club = club;
        this.joinClubPrice = joinClubPrice;
    }

    public Club getClub() {
        return club;
    }

    public User getMember(int id){
        return members.get(id);
    }

    public int getMembersSize(){
        return members.size();
    }

    public void addMember(User user){
        members.add(user);
        club.addMember(user.getPerson());
    }

    public boolean isMember(User user){
        for (User member : members) {
            if(member == user)
                return true;
        }
        return false;
    }

}
