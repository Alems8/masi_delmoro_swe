package BusinessLogic;

import DomainLogic.Club;
import DomainLogic.Field;
import DomainLogic.UserClub;
import DAO.ClubDao;
import DAO.FakeClubDao;
import ObserverUtil.Observer;
import DomainLogic.Sport;
import DomainLogic.User;

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

    public void addClubMember(User user) throws AlreadySubscribedException {
        if(currentClub.isMember(user))
            throw new AlreadySubscribedException();
        currentClub.addMember(user);
    }
}
