package DAO;

public abstract class DaoFactory {
    public static final int FAKE = 1;
    private static FakeDaoFactory instance = null;


    public abstract UserDao getUserDao();
    public abstract ClubDao getClubDao();
    public abstract BookingDao getBookingDao();

    public static DaoFactory getDaoFactory(int nFactory){
        switch (nFactory){
            case FAKE :
            {
                if (instance == null)
                    instance = new FakeDaoFactory();
                return instance;
            }
            default:
                return null;
        }
    }
}
