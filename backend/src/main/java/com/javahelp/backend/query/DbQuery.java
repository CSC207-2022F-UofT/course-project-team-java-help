package com.javahelp.backend.query;

import com.javahelp.model.user.User;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class DbQuery implements IDbQuery{
    private String tableName;

    public DbQuery(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public List<User> query(HashMap<String, ArrayList<Integer>> constraint) {
        return null;
    }
}
