package com.parasuram.spring.async.parasuramspringasync.utils;

import com.parasuram.spring.async.parasuramspringasync.web.security.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    protected MvcResult sendRequest(String url, RequestMethod method, Cookie cookie, String content,
                                    Map<String, String> uriVariableValues, Map<String, String> queryParams,
                                    ResultMatcher expectedResult, Boolean asyncDispatch,
                                    String tenantId, List<Role> userRoles) throws Exception {
        return sendRequest(url, method, cookie, content,
                uriVariableValues, queryParams,
                expectedResult, asyncDispatch,
                tenantId, userRoles, UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    protected MvcResult sendRequest(String url, RequestMethod method, Cookie cookie, String content,
                                    Map<String, String> uriVariableValues, Map<String, String> queryParams,
                                    ResultMatcher expectedResult, Boolean asyncDispatch,
                                    String tenantId, List<Role> userRoles, String requestId, String username) throws Exception {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);

        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        MockHttpServletRequestBuilder requestBuilder;
        switch (method) {
            case GET:
                requestBuilder = get(uriComponentsBuilder.buildAndExpand(uriVariableValues).toUri());
                break;
            case POST:
                requestBuilder = post(uriComponentsBuilder.buildAndExpand(uriVariableValues).toUri());
                break;
            case PUT:
                requestBuilder = put(uriComponentsBuilder.buildAndExpand(uriVariableValues).toUri());
                break;
            case DELETE:
                requestBuilder = delete(uriComponentsBuilder.buildAndExpand(uriVariableValues).toUri());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Request Method");
        }
        if (cookie != null) {
            requestBuilder = requestBuilder.cookie(cookie);
        }
        if (content != null) {
            requestBuilder = requestBuilder.content(content);
        }

        MvcResult mvcResult;
        if (asyncDispatch) {

            mvcResult =
                    this.mockMvc
                            .perform(requestBuilder.contentType(APPLICATION_JSON_VALUE)
                                    .headers(getHttpHeaders(tenantId, userRoles, requestId, username)))
//                            .andExpect(MockMvcResultMatchers.request().asyncStarted())
//                            .andDo(MockMvcResultHandlers.log())
                            .andReturn();
            this.mockMvc.perform(asyncDispatch(mvcResult))
                    .andExpect(expectedResult);

        } else {
            mvcResult =
                    this.mockMvc
                            .perform(requestBuilder.contentType(APPLICATION_JSON_VALUE).
                                    headers(getHttpHeaders(tenantId, userRoles, requestId, username)))
                            .andExpect(expectedResult)
                            .andReturn();
        }
        return mvcResult;
    }

    private HttpHeaders getHttpHeaders(String tenantId, List<Role> userRoles, String requestId, String username) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("TENANT_ID", tenantId);
        httpHeaders.add("REQUEST_ID", requestId);
        httpHeaders.add("USER_ID", username);
        httpHeaders.add("ADMIN_NAME", username);
        httpHeaders.add("PVER ","master");


        if (!CollectionUtils.isEmpty(userRoles)) {
            httpHeaders.add("USER_ROLE", StringUtils.join(userRoles.stream().map(e->e.name()).toArray(), ','));
            //httpHeaders.add("RKS_USER_ROLE", userRoles.stream().map(e->e.name()).collect(Collectors.joining(",")));
        }
        return httpHeaders;
    }
}
