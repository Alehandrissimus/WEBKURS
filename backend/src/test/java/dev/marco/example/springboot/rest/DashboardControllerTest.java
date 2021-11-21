package dev.marco.example.springboot.rest;

import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.model.impl.UserImpl;
import dev.marco.example.springboot.service.DashboardService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verify;

@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DashboardControllerTest {

    private static final Logger log = Logger.getLogger(DashboardControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void generateDashboard() throws Exception {
        User user = new UserImpl.UserBuilder()
                .setId(BigInteger.valueOf(3))
                .build();

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/dashboard/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(dashboardService).generateDashboard(user.getId());
    }
}
