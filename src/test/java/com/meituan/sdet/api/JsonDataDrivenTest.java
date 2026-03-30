package com.meituan.sdet.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

@Epic("美团外卖 C 端接口自动化")
@Feature("用户中心模块")
public class JsonDataDrivenTest {

    /**
     * 1. 核心引擎：读取 JSON 文件并转换为 DataProvider 需要的二维数组
     */
    @DataProvider(name = "jsonTestData")
    public Object[][] provideDataFromJson() throws IOException {
        // 找到我们刚才建的 json 文件
        File jsonFile = new File("src/test/resources/testdata/users.json");

        // 使用 Jackson 库来解析 JSON
        ObjectMapper mapper = new ObjectMapper();

        // 将 JSON 数组读取为 Java 里的 List<Map> 集合
        List<Map<String, Object>> dataList = mapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});

        // 创建一个二维数组，行数就是 JSON 里的数据条数，列数是 2（因为我们有 userId 和 expectedName 两个参数）
        Object[][] result = new Object[dataList.size()][2];

        // 遍历 List，把数据塞进二维数组里
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> row = dataList.get(i);
            result[i][0] = row.get("userId");       // 第 1 列放 ID
            result[i][1] = row.get("expectedName"); // 第 2 列放预期名字
        }

        return result;
    }

    /**
     * 2. 测试逻辑：你看，这里的代码和之前一模一样！没有任何改变。
     * 它只管从 "jsonTestData" 里拿数据，根本不在乎数据是写死的还是从文件读的。
     */
    @Test(dataProvider = "jsonTestData")
    @Story("查询用户详情-正常场景") // 重点在这里！
    public void testUserWithJsonData(int userId, String expectedName) {
        System.out.println("\n====== [JSON外挂驱动] 开始测试用户 ID: " + userId + " ======");

        RestAssured
                .given()
                .when()
                .get("https://jsonplaceholder.typicode.com/users/" + userId)
                .then()
                .statusCode(200)
                .body("name", equalTo(expectedName));

        System.out.println("验证成功！用户 ID: " + userId + " 的名字确实是：" + expectedName);
    }
}