package DAO;

import Club.Field;

import java.util.ArrayList;

public class FakeFieldDao implements FieldDao{
    private final ArrayList<Field> fields;
    private static FakeFieldDao instance = null;

    private FakeFieldDao() {
        fields = new ArrayList<>();
    }

    public static FakeFieldDao getInstance(){
        if (instance == null){
            instance = new FakeFieldDao();
        }
        return instance;
    }
}
