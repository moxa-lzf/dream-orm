package com.dream.test.base.service;

import com.dream.template.mapper.TemplateMapper;
import com.dream.test.base.mapper.UserMapper;
import com.dream.test.base.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TemplateMapper templateMapper;

    public Map findByName(String state) {
        Map map = userMapper.findByName(state);
        return map;
    }

    public User findByName2(String state) {
        User user = userMapper.findByName2(state);
        return user;
    }

    public Integer update(User user) {
        return userMapper.update(user);
    }

    public List<Object> insertBatch(List<User> userList) {
        return templateMapper.batchInsert(userList);
    }

    public void insertBatch2(List<User> userList) {
    }
}