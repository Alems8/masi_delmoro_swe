package DAO;

import Club.UserClub;
import java.util.ArrayList;

public class FakeClubDao implements ClubDao{
    private final ArrayList<UserClub> clubs;

    private static FakeClubDao instance = null;

    private FakeClubDao() {
        clubs = new ArrayList<>();
    }

    public static FakeClubDao getInstance(){
        if (instance == null){
            instance = new FakeClubDao();
        }
        return instance;
    }


    public void addClub(UserClub club){
        clubs.add(club);
    }

    public UserClub getClub(int id){
        return clubs.get(id);
    }

    public int getClubsSize(){
        return clubs.size();
    }

    public UserClub getClubByName(String clb){
        for(UserClub club : clubs){
            if (club.getClub().name.equals(clb))
                return club;
        }
        return null;
    }
}
