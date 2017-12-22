package com.zlh.redistest;


import com.zlh.redistest.dao.UserDAO;

import com.zlh.redistest.model.User;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
	@Autowired
	UserDAO userDAO;


	@Test
	public void contextLoads() {
		Random random = new Random();

		for (int i = 0; i < 11; ++i) {
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i+1));
			user.setPassword("");
			user.setSalt("");

			userDAO.addUser(user);

		}

		//Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
		//userDAO.deleteById(1);
		//Assert.assertNull(userDAO.selectById(1));
	}

//	@Test
//	public void testSensitive() {
//		String content = "question content <img src=\"https:\\/\\/baidu.com/ff.png\">色情赌博";
//		String result = sensitiveUtil.filter(content);
//		System.out.println(result);
//	}
}
