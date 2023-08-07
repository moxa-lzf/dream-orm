package com.dream.test;

import com.dream.base.mapper.UserMapper;
import com.dream.base.table.User;
import com.dream.base.view.UserView4;
import com.dream.template.mapper.TemplateMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootApplication.class)
public class UpdateTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TemplateMapper templateMapper;

    @Test
    public void updateId() {
        User user = new User();
        user.setId(1);
        user.setName("name");
        user.setAge(1);
        user.setEmail("email");
//            cityService.update(city);
        templateMapper.updateById(user);
    }

    @Test
    public void updateNonId() {
        User user = new User();
        user.setId(1);
        user.setEmail("country");
        templateMapper.updateNonById(user);
    }

    @Test
    public void updateNonId2() {
        User user = new User();
        user.setId(1);
        user.setName("hli");
        user.setEmail("");
        userMapper.updateNon(user);
    }

    @Test
    public void updateInject() {
        UserView4 user = new UserView4();
        user.setId(1);
        user.setName("name");
        user.setEmail("email");
//            cityService.update(city);
        templateMapper.updateNonById(user);
    }
}