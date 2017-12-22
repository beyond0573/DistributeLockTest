package com.zlh.redistest.service.impl;

import com.zlh.redistest.dao.UserDAO;
import com.zlh.redistest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;


    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void addAge(){
        User user= userDAO.selectByIdForUpdate(1);
        int age=user.getAge();
        age=age+1;

//            User newUser=new User();
//            newUser.setAge(age);
//            newUser.setId(1);
        user.setAge(age);
        // userDAO.updateAge(user);
        userDAO.updateAge(user);
    }


}
