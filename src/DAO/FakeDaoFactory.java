package DAO;

public class FakeDaoFactory extends DaoFactory{


    @Override
    public UserDao getUserDao() {
        return FakeUserDao.getInstance();
    }

    @Override
    public FieldDao getFieldDao() {
        return FakeFieldDao.getInstance();
    }

    @Override
    public ClubDao getClubDao() {
        return FakeClubDao.getInstance();
    }

    @Override
    public BookingDao getBookingDao() {
        return FakeBookingDao.getInstance();
    }
}
