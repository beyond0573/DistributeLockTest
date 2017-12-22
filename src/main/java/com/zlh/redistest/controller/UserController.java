package com.zlh.redistest.controller;

import com.zlh.redistest.dao.UserDAO;
import com.zlh.redistest.model.User;
import com.zlh.redistest.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

@Controller
public class UserController {

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserService userService;

    //static int count=0;

    @RequestMapping(path = {"/user"})
    @ResponseBody
    public String index(){
        //User user=userDAO.selectById(1);

        for(int i=0;i<20000;i++){
       //     User user=userDAO.selectById(1);
            User user= userDAO.selectByIdForUpdate(1);
            int age=user.getAge();
            age=age+1;
//
////            User newUser=new User();
////            newUser.setAge(age);
////            newUser.setId(1);
            user.setAge(age);
            userDAO.updateAge(user);
//            userDAO.updateUserVersion(user);
           // userService.addAge();

        }

        return "Hello Spring boot";
    }

}


