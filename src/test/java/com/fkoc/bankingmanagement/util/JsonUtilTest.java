package com.fkoc.bankingmanagement.util;

import static com.fkoc.bankingmanagement.util.TestConstants.ACCOUNT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fkoc.bankingmanagement.entity.Account;
import org.junit.jupiter.api.Test;

public class JsonUtilTest {

    @Test
    void givenNullObjectWhenObjectToJsonThenReturnNullObjectMessage() {
        assertThat(JsonUtil.objectToJson(null)).hasToString("Null Object");
    }

    @Test
    void givenObjectWhenObjectToJsonThenReturnJson() {
        Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        assertThat(JsonUtil.objectToJson(account)).isNotBlank();
    }
}