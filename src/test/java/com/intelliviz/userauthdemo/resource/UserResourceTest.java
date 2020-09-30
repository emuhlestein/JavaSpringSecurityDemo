package com.intelliviz.userauthdemo.resource;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import com.intelliviz.userauthdemo.models.User;
import com.intelliviz.userauthdemo.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Optional;

@WebMvcTest(UserResource.class)
public class UserResourceTest {
    @MockBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

//    private String userId;
//    private String firstName;
//    private String lastName;
//    private String userName;
//    private String password;
//    private String email;
//    private String profileImageUrl;

    @Test
    @DisplayName("GET /user/1 - Found")
    void testGetUserById() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/user/{id}", 1)
                .accept(MediaType.APPLICATION_JSON);
        User mockUser = new User("1234", "Ed", "Muhlestein", "emuhlestein",
                "letmeon", "emuhlestein@yahoo.com", "url");
        doReturn(Optional.of(mockUser)).when(service).findById(1L);

        MvcResult result = mockMvc.perform(request).andReturn();
    }
}
