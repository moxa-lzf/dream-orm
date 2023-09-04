package com.dream.helloworld;

import com.dream.flex.def.DeleteDef;
import com.dream.flex.def.InsertDef;
import com.dream.flex.def.QueryDef;
import com.dream.flex.def.UpdateDef;
import com.dream.flex.mapper.FlexMapper;
import com.dream.helloworld.table.Account;
import com.dream.helloworld.view.AccountView;
import com.dream.helloworld.view.TreeAccountView;
import com.dream.system.config.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static com.dream.flex.def.FunctionDef.*;
import static com.dream.helloworld.table.table.AccountTableDef.account;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HelloWorldApplication.class)
public class HelloWorldFlexTest {
    @Autowired
    private FlexMapper flexMapper;

    /**
     * 测试主键查询
     */
    @Test
    public void testSelectById() {
        QueryDef queryDef = select(account.accountView).from(account).where(account.id.eq(1));
        AccountView accountView = flexMapper.selectOne(queryDef, AccountView.class);
        System.out.println("查询结果：" + accountView);
    }

    /**
     * 测试查询多条
     */
    @Test
    public void testSelectList() {
        QueryDef queryDef = select(account.accountView).from(account).where(account.id.gt(3));
        List<AccountView> accountViews = flexMapper.selectList(queryDef, AccountView.class);
        System.out.println("查询结果：" + accountViews);
    }

    /**
     * 测试分页查询
     */
    @Test
    public void testSelectPage() {
        QueryDef queryDef = select(account.accountView).from(account).where(account.id.gt(1));
        Page page = new Page(1, 2);
        page = flexMapper.selectPage(queryDef, AccountView.class, page);
        System.out.println("总数：" + page.getTotal() + "\n查询结果：" + page.getRows());
    }

    /**
     * 测试更新
     */
    @Test
    public void testUpdate() {
        UpdateDef updateDef = update(account).set(account.age, account.age.add(1)).set(account.name, "accountName").where(account.id.eq(1));
        flexMapper.update(updateDef);
    }

    /**
     * 测试插入
     */
    @Test
    public void testInsert() {
        InsertDef insertDef = insertInto(account).columns(account.name, account.age).values("accountName", 12);
        flexMapper.insert(insertDef);
    }

    /**
     * 批量插入，注意有些数据库不支持这种批量插入
     */
    @Test
    public void testInsert3() {
        List<Account> accountList = new ArrayList<>();
        for (int i = 10; i < 14; i++) {
            Account account = new Account();
            account.setId(i);
            account.setName("name" + i);
            account.setAge(i * 2);
            accountList.add(account);
        }
        InsertDef insertDef = insertInto(account).columns(account.id, account.name, account.age).valuesList(accountList, acc -> {
            Account account = (Account) acc;
            return new Object[]{account.getId(), account.getName(), account.getAge()};
        });
        flexMapper.insert(insertDef);
    }

    /**
     * 测试删除
     */
    @Test
    public void testDelete() {
        DeleteDef deleteDef = delete(account).where(account.id.eq(1));
        flexMapper.delete(deleteDef);
    }

    /**
     * 测试存在
     */
    @Test
    public void testExist() {
        QueryDef queryDef = select().from(account).where(account.id.gt(3));
        boolean exists = flexMapper.exists(queryDef);
        System.out.println("查询结果：" + exists);
    }

    /**
     * 测试树查询
     */
    @Test
    public void testTree() {
        QueryDef queryDef = select(account.treeAccountView).from(account);
        List<TreeAccountView> treeAccountViews = flexMapper.selectTree(queryDef, TreeAccountView.class);
        System.out.println("查询结果：" + treeAccountViews);
    }
}
