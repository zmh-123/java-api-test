package com.meituan.sdet.api;

import io.restassured.RestAssured;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class DataDrivenTest {

    /**
     * 1. 这里就是“数据源”（Data Provider）
     * 它必须返回一个二维数组 Object[][]
     * 每一行代表一个测试用例，每一列代表传给用例的参数
     */
    @DataProvider(name = "userTestData")
    public Object[][] provideData() {
        return new Object[][] {
                // 参数1: 要查询的用户ID | 参数2: 预期的用户姓名
                {1, "Leanne Graham"},
                {2, "Ervin Howell"},
                {3, "Clementine Bauch"}
        };
    }

    /**
     * 2. 这里是“测试逻辑”
     * 通过 dataProvider = "userTestData" 将两者绑定
     * 注意：数据源里有几列，这个方法就必须接收几个参数！
     */
    @Test(dataProvider = "userTestData")
    public void testUserWithData(int userId, String expectedName) {
        System.out.println("\n====== 开始测试用户 ID: " + userId + " ======");

        RestAssured
                .given()
                .when()
                // 动态把 userId 拼接到 URL 里面
                .get("https://jsonplaceholder.typicode.com/users/" + userId)
                .then()
                .statusCode(200)
                // 动态验证返回的名字是否与预期相符
                .body("name", equalTo(expectedName));

        System.out.println("验证成功！用户 ID: " + userId + " 的名字确实是：" + expectedName);
    }
}