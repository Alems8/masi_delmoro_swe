package BookingManager;

import Club.Club;

import java.util.ArrayList;

public class UserClub {
    private Club club;
    private BookingManager bm;
    private ArrayList<User> members = new ArrayList<>();
    public int price;
    public int memberPrice;
    public int joinClubPrice;

    public UserClub(Club club, BookingManager bm, int memberPrice, int joinClubPrice) {
        this.club = club;
        this.bm = bm;
        this.price = club.price;
        this.joinClubPrice = joinClubPrice;
        this.memberPrice = memberPrice;
    }

    public Club getClub() {
        return club;
    }

    public void addMember(User user){
        members.add(user);
    }

    public boolean isMember(User user) {
        for (User member : members) {
            if(member == user)
                return true;
        }
        return false;
    }

}
