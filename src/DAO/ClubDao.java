package DAO;

import DomainLogic.UserClub;

public interface ClubDao {

    void addClub(UserClub club);

    UserClub getClub(int id);

    int getClubsSize();

    UserClub getClubByName(String clb);

}
