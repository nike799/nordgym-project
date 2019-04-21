package com.nordgym.web.controllers;

import com.nordgym.domain.entities.Role;
import com.nordgym.domain.entities.User;
import com.nordgym.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class HomeControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void index_returns_correctView() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    public void home_Admin_returns_correctView() throws Exception {
        this.mvc.perform(get("/home"))
                .andExpect(redirectedUrl("/admin/home"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void home_User_returns_correctView() throws Exception {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(user.getAuthorities()).thenReturn(new HashSet<>() {{
            Role role = new Role();
            role.setAuthority("USER");
            add(role);
        }});
        this.mvc.perform(get("/home"))
                .andExpect(redirectedUrl("/user-profile/"+user.getId()));
    }

}