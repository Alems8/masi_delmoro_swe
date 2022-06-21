package BusinessLogic;

import Club.Club;
import Club.Field;
import Club.UserClub;
import DAO.ClubDao;
import DAO.FakeClubDao;
import ObserverUtil.Observer;
import Sport.Sport;
import User.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class ClubController {
    private ClubDao clubDao;
    private UserClub currentClub;
    private Field currentField;
    private LocalDate currentDate;
    private int currentHour;

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }

    public ClubController(){
        clubDao = FakeClubDao.getInstance();
    }

    void setCurrentClub(String clb) throws WrongNameException {
        currentClub = clubDao.getClubByName(clb);
        if (currentClub == null)
            throw new WrongNameException();
    }

    UserClub getCurrentClub(){
        return currentClub;
    }

    void setCurrentField(Sport sport, LocalDate date, int hour) throws NoFreeFieldException {
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
        currentField = field;
        setCurrentDate(date);
        setCurrentHour(hour);
    }

    Field getCurrentField(){
        return currentField;
    }

    public boolean isMember(User user){
        return currentClub.isMember(user);
    }

    public void holdField() {
        ArrayList<Integer> times = currentField.timeTable.get(currentDate);
        times.remove((Integer) currentHour);
    }

    private void checkClub(Club club) throws AlreadySubscribedException {
        if(clubDao.getClubByName(club.name) != null)
            throw new AlreadySubscribedException();
    }

    public UserClub addClub(Club club, int joinClubPrice) {
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

    public void addClubMember(User user) {
        currentClub.addMember(user);
    }
}
