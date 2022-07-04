package BusinessLogic;

import DomainModel.Club;
import DomainModel.Field;
import DomainModel.UserClub;
import DAO.ClubDao;
import DAO.FakeClubDao;
import ObserverUtil.Observer;
import DomainModel.Sport;
import DomainModel.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class ClubController {
    private ClubDao clubDao;
    private UserClub currentClub;
    private Field currentField;
    private LocalDate currentDate;
    private int currentHour;


    public ClubController(){
        clubDao = FakeClubDao.getInstance();
    }


    LocalDate getCurrentDate() {
        return currentDate;
    }

    void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    int getCurrentHour() {
        return currentHour;
    }

    void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }

    void setCurrentClub(UserClub club) {
        currentClub = club;
    }

    void setCurrentClub(String clb) throws WrongNameException {
        UserClub club = clubDao.getClubByName(clb);
        if (club == null)
            throw new WrongNameException();
        currentClub = club;
    }

    UserClub getCurrentClub(){
        return currentClub;
    }

    void setCurrentField(Field field){
        currentField = field;
    }

    Field getCurrentField(){
        return currentField;
    }

    void findField(Sport sport, LocalDate date, int hour) throws NoFreeFieldException {
        int i = 0;
        boolean found = false;
        Club club = currentClub.getClub();
        Field field = null;
        while(!found && i < club.fields.size()){
            field = club.fields.get(i++);

            if(field.sport.equals(sport)){
                if(!(field.timeTable.containsKey(date))){
                    ArrayList<Integer> updatedTimes = new ArrayList<>(club.times);
                    field.timeTable.put(date, updatedTimes);
                }

                ArrayList<Integer> times = field.timeTable.get(date);
                if(times.contains(hour)){
                    found = true;
                }
            }
        }
        if(!found)
            throw new NoFreeFieldException();
        setCurrentField(field);
        setCurrentDate(date);
        setCurrentHour(hour);
    }

    void holdField() {
        ArrayList<Integer> times = currentField.timeTable.get(currentDate);
        times.remove((Integer) currentHour);
    }

    void releaseField() {
        currentField.timeTable.get(currentDate).add(currentHour);
    }

    private void checkClub(Club club) throws AlreadySubscribedException {
        if(clubDao.getClubByName(club.name) != null)
            throw new AlreadySubscribedException();
    }

    UserClub addClub(Club club, int joinClubPrice) {
        try{checkClub(club);}
        catch(AlreadySubscribedException e){
            System.out.println("Sei già registrato");
            return null;
        }
        UserClub userClub = new UserClub(club, joinClubPrice);
        Observer monitor = MembersMonitor.getInstance();
        club.addObserver(monitor);
        clubDao.addClub(userClub);
        return userClub;
    }

    void addClubMember(User user) throws AlreadySubscribedException {
        if(currentClub.isMember(user))
            throw new AlreadySubscribedException();
        currentClub.addMember(user);
    }
}
