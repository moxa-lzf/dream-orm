package com.dream.test.myfucntion.simple;

import com.dream.test.AbstractSqlTest;

/**
 * 负责测试，支持系统未识别的函数
 */
public class SimpleTest extends AbstractSqlTest {
    public static void main(String[] args) {
        SimpleTest simpleTest = new SimpleTest();
        simpleTest.testSqlForMany("select a(1,2,3),decode(a,1,0,2,1,3,2,4) from dual where (1+2)*(12-12)<125", new MySimpleFunctionFactory(), null);
    }
}