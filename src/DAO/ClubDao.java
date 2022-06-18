package DAO;

import Club.UserClub;

public interface ClubDao {

    public void addClub(UserClub club);

    public UserClub getClub(int id);

    public int getClubsSize();
}
