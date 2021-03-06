package DAO;

import DomainModel.UserClub;

public interface ClubDao {

    void addClub(UserClub club);

    boolean containsClub(UserClub club);

    UserClub getClub(int id);

    int getClubsSize();

    UserClub getClubByName(String clb);

}
