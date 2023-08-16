package com.dream.test;

import com.dream.system.config.Page;
import com.dream.template.mapper.TemplateMapper;
import com.dream.test.base.condition.UserCondition;
import com.dream.test.base.mapper.BlogMapper;
import com.dream.test.base.mapper.UserMapper;
import com.dream.test.base.table.Blog;
import com.dream.test.base.table.User;
import com.dream.test.base.view.UserView;
import com.dream.test.base.view.UserView2;
import com.dream.test.base.view.UserView3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootApplication.class)
public class QueryTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private BlogMapper blogMapper;


    @Test
    public void test() {
        Map map = userMapper.findByName("Jone");
    }

    @Test
    public void test2() {
        User user = userMapper.findByName2("'Jone'");
    }

    @Test
    public void test3() {
        List<User> userList = userMapper.findAll();
        userList.forEach(System.out::println);
    }

    @Test
    public void test4() {
        userMapper.findByAll();
    }

    @Test
    public void testAll4() {
        Object v = userMapper.findAll4();
        System.out.println(v);
    }

    @Test
    public void test5() {
        List<UserView> userViews = userMapper.selectAll();
//        userViews.forEach(System.out::println);
    }

    @Test
    public void test6() {
        List<Blog> blogs = blogMapper.selectBlogByUserId2(1);
        System.out.println(blogs);
    }

    @Test
    public void test7() {
        List<UserView2> userViews = userMapper.selectAll2();
    }

    @Test
    public void test8() {
        List<User> userList = userMapper.selectAll3();
    }

    @Test
    public void testPage() {
        Page page = new Page(1, 1);
        List<User> userList = userMapper.findByPage(page);
        page.setRows(userList);
        System.out.println("总数：" + page.getTotal());
    }

    @Test
    public void testPage2() {
        //h2不支持
        Page page = new Page(1, 1);
        List<UserView3> userList = userMapper.findByPage2(page);
        page.setRows(userList);
        System.out.println("总数：" + page.getTotal());
    }

    @Test
    public void testSelectById() {
        templateMapper.selectById(User.class, 1);
    }

    @Test
    public void testSelectById2() {
        UserView3 userView3 = templateMapper.selectById(UserView3.class, 1);
    }

    @Test
    public void testSelect3() {
        UserCondition userCondition = new UserCondition();
        userCondition.setName("ll");
        userCondition.setAge(Arrays.asList(1, 2, 3, 4));
        List<UserView3> userView3s = templateMapper.selectList(UserView3.class, userCondition);
    }

    @Test
    public void testSelect4() {
        UserCondition userCondition = new UserCondition();
        userCondition.setName("ll");
        userCondition.setAge(Arrays.asList(1, 2, 3, 4));
        Page<UserView> userView3s = templateMapper.selectPage(UserView.class, userCondition, new Page(1, 10));
    }

    @Test
    public void testSelectByIds() {
        templateMapper.selectByIds(User.class, Arrays.asList(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void existById() {
        boolean b = templateMapper.existById(User.class, 12);
    }

    @Test
    public void exist() {
        UserCondition userCondition = new UserCondition();
        userCondition.setName("ll");
        userCondition.setAge(Arrays.asList(1, 2, 3, 4));
        boolean b = templateMapper.exist(UserView.class, userCondition);
    }
}