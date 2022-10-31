package com.parasuram.spring.async.parasuramspringasync.utils;

import com.parasuram.spring.async.parasuramspringasync.web.security.Role;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.Collections;

import static com.parasuram.spring.async.parasuramspringasync.web.constants.Constants.HELLO_WORLD;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HelloWorldCompletableFutureControllerTest extends AbstractTest {

    public static final String RAM_RUCKUS_TENANT = "ram-ruckus";

    @Test
    public void echoHelloWorldWithTenantTest() throws Exception {
        MvcResult mvcResult = sendRequest("/tenant/{tenantId}/testCompletableFuture", RequestMethod.GET, null, null,
                Collections.singletonMap("tenantId", RAM_RUCKUS_TENANT), null,
                status().isAccepted(), true, "ram-ruckus", Arrays.asList(Role.DEVELOPER));

        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertEquals(String.format("%s--%s",HELLO_WORLD,RAM_RUCKUS_TENANT),contentAsString);

    }

}