package dev.marco.example.springboot.rest;

import dev.marco.example.springboot.model.Announcement;
import dev.marco.example.springboot.model.impl.AnnouncementImpl;
import dev.marco.example.springboot.model.impl.UserImpl;
import dev.marco.example.springboot.service.AnnouncementService;
import org.junit.jupiter.api.Test;
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
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class AnnouncementControllerTest {

    @MockBean
    private AnnouncementService announcementService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createAnnouncementTest() throws Exception {
            this.mockMvc.perform(MockMvcRequestBuilders
                    .post("/announcement/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"title\":\"Let`s go to party\"," +
                             "  \"description\":\"Welcome to Festival\"," +
                             "  \"idUser\":5," +
                             "  \"address\":\"st. Shevchenko 1\"}"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            verify(announcementService).buildNewAnnouncement(any(Announcement.class));
    }

    @Test
    void getAllAnnouncementTest() throws Exception {

        when(announcementService.getAllAnnouncements(BigInteger.ONE)).thenReturn(
                Arrays.asList(
                        new AnnouncementImpl.AnnouncementBuilder()
                                .setId(BigInteger.ONE)
                                .setTitle("TEST_TITLE1")
                                .setDescription("TEST_DESCRIPTION1")
                                .setIdUser(BigInteger.TEN)
                                .setAddress("TEST_ADDRESS1")
                                .setParticipantsCap(5)
                                .setIsLiked(true)
                                .build(),
                        new AnnouncementImpl.AnnouncementBuilder()
                                .setId(BigInteger.TWO)
                                .setTitle("TEST_TITLE2")
                                .setDescription("TEST_DESCRIPTION2")
                                .setIdUser(BigInteger.TEN)
                                .setAddress("TEST_ADDRESS2")
                                .setParticipantsCap(10)
                                .setIsLiked(false)
                                .build()));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/announcement/all/{idUser}", BigInteger.ONE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].id").value(BigInteger.ONE))
                .andExpect(jsonPath("$[0].title").value("TEST_TITLE1"))
                .andExpect(jsonPath("$[0].description").value("TEST_DESCRIPTION1"))
                .andExpect(jsonPath("$[0].idUser").value(BigInteger.TEN))
                .andExpect(jsonPath("$[0].address").value("TEST_ADDRESS1"))
                .andExpect(jsonPath("$[0].participantsCap").value(5))
                .andExpect(jsonPath("$[0].isLiked").value(true))

                .andExpect(jsonPath("$[1].id").value(BigInteger.TWO))
                .andExpect(jsonPath("$[1].title").value("TEST_TITLE2"))
                .andExpect(jsonPath("$[1].description").value("TEST_DESCRIPTION2"))
                .andExpect(jsonPath("$[1].idUser").value(BigInteger.TEN))
                .andExpect(jsonPath("$[1].address").value("TEST_ADDRESS2"))
                .andExpect(jsonPath("$[1].participantsCap").value(10))
                .andExpect(jsonPath("$[1].isLiked").value(false));

        verify(announcementService).getAllAnnouncements(BigInteger.ONE);
    }

    @Test
    void deleteAnnouncement() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/announcement/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idUser\": 1," +
                                 "  \"idAnnouncement\": 1 }"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(announcementService).deleteAnnouncement(BigInteger.ONE, BigInteger.ONE);
    }

    @Test
    void editAnnouncement() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/announcement/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1," +
                         "  \"title\": \"Go to stand up club\"," +
                         "  \"description\": \"welcome to the club body\"," +
                         "  \"idUser\": 1," +
                         "  \"address\": \"st. Bolshaya Dmitrovka 13\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(announcementService).editAnnouncement(any(AnnouncementImpl.class));
    }

    @Test
    void likeAnnouncement() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/announcement/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"idUser\" : 1, " +
                         "  \"idAnnouncement\": 1 }"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(announcementService).setLikeAnnouncement(BigInteger.ONE);
    }

    @Test
    void getSetByTitle() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/announcement/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"searchProject\":\"title\"," +
                                 "  \"idUser\":1}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(announcementService).getSetByTitle("title", BigInteger.ONE);
    }

    @Test
    void getAnnouncementsByPage() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/announcement/all?page=1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(announcementService).getAnnouncementsByPage(1);
    }

    @Test
    void getAnnouncementsLikeTitle() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/announcement/search?page=1&title=chill" ))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(announcementService).getAnnouncementsLikeTitle("chill", 1);

    }
}