package com.meituan.sdet.api;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

// 引入 Hamcrest 断言库，这是 REST Assured 官方推荐的断言方式
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class FirstApiTest {

    // 体验断言
    @Test
    public void SecondApiTest() {
        System.out.println("第二个测试用例");

        Response response = (Response) RestAssured
                .given()
                .when()
                .get("https://jsonplaceholder.typicode.com/users") // 一个无爬虫的友好接口
                .then() // 验证阶段 即断言
                .log().all() // log().all() 会自动帮你把请求头、响应头、格式化好的 JSON 全打印出来，非常方便排错
                .statusCode(200) // 断言 1：验证 HTTP 状态码必须是 200
                .extract().response(); // 提取响应对象，方便后续使用


        // 获取数组的长度
        // 由于接口返回的是一个 JSON 数组，我们可以使用 jsonPath().getList("$") 来获取这个数组，并调用 size() 方法来获取它的长度
        int numcount = response.jsonPath().getList("$").size();
        System.out.println("用户数量: " + numcount);

        // 获取第一个用户的名字
        String nameOfFirstUser = response.jsonPath().getString("[0].name");
        System.out.println("第一个用户的名字: " + nameOfFirstUser);

        // 提取所有用户的名字放入一个list数组中
        List<String> userList = new ArrayList<>();
        for(int i = 0;i < numcount;i++) {
            userList.add(response.jsonPath().getString("[" + i + "].name"));
        }
        System.out.println("所有用户的名字: " + userList);


        // 断言集合中是否包含某个特定的名字
        // 只有抛出异常才算失败否则断言通过
        org.testng.Assert.assertTrue(userList.contains("Clementina DuBuque"), "用户列表中不包含 Clementina DuBuque");
        System.out.println("断言通过，用户列表中包含 Clementina DuBuque");


    }

}
