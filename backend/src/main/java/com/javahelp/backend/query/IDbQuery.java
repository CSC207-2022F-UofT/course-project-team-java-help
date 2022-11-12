package com.javahelp.backend.query;

import com.javahelp.model.user.User;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public interface IDbQuery {
    List<User> query(HashMap<String, ArrayList<Integer>> constraint);
}
