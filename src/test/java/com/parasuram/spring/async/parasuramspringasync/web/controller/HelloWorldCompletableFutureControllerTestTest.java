package com.parasuram.spring.async.parasuramspringasync.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.DispatcherType;

import static com.parasuram.spring.async.parasuramspringasync.web.constants.Constants.HELLO_WORLD;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HelloWorldCompletableFutureControllerTestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_async_hello_world() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/tenant/ram/testCompletableFuture"))
                //.andExpect(request().asyncStarted())
                //.andDo(MockMvcResultHandlers.log())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                //.andExpect(content().contentTypeCompatibleWith("text/plain"))
                .andExpect(content().string(String.format("%s--%s",HELLO_WORLD,"ram")));
    }
    public static RequestBuilder asyncDispatch(MvcResult mvcResult) {
        mvcResult.getAsyncResult(100);
        return (servletContext) -> {
            MockHttpServletRequest request = mvcResult.getRequest();
            request.setDispatcherType(DispatcherType.ASYNC);
            request.setAsyncStarted(false);
            return request;
        };
    }
}