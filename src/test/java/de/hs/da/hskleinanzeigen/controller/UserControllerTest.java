package de.hs.da.hskleinanzeigen.controller;

import de.hs.da.hskleinanzeigen.entity.AD;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.Notepad;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.enumeration.Type;
import de.hs.da.hskleinanzeigen.repository.AdRepository;
import de.hs.da.hskleinanzeigen.repository.NotepadRepository;
import de.hs.da.hskleinanzeigen.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    static {
        GenericContainer<?> redis =
                new GenericContainer<>(DockerImageName.parse("redis:6.2.6-alpine")).withExposedPorts(6379);
        redis.start();
        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    }
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    private NotepadRepository notepadRepository;

    @Autowired
    private AdRepository adRepository;

    @BeforeEach
    @AfterEach
    void setUp() {
        adRepository.deleteAll();
        notepadRepository.deleteAll();
        repository.deleteAll();
    }

    private User createUser() {
        User user = new User();
        user.setEmail("email@gmail.com");
        user.setFirstName("User");
        user.setLastName("test");
        user.setCreated(LocalDateTime.now());
        user.setPassword("password");
        user.setId(1);
        return user;
    }

    private AD createAd() {
        AD ad = new AD();
        ad.setPrice(1);
        ad.setTitle("cool ad");
       // ad.setId(1);
        ad.setDescription("cool description");
        User user = new User();
        user.setEmail("email145@gmail.com");
        user.setCreated(LocalDateTime.now());
        user.setPassword("password");
        ad.setUser(user);
        ad.setCategory(new Category());
        ad.setType(Type.OFFER);
        ad.setLocation("Darmstadt");
        ad.setCreated(LocalDateTime.now());
        return ad;
    }

    private Notepad createNotepad(User user, AD ad) {
        Notepad notepad = new Notepad();
        notepad.setId(1);
        notepad.setUser(user);
        notepad.setAd(ad);
        notepad.setCreated(LocalDateTime.now());
        return notepad;
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void givenUsers_whenGetAll_thenStatus200()
            throws Exception {
        repository.save(createUser());
        mvc.perform(get("/api/users")
                        .queryParam("pageStart", "0")
                        .queryParam("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void givenUsers_whenParametersMissing_thenStatus400()
            throws Exception {
        mvc.perform(get("/api/users")
                        .queryParam("pageStart", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void getAllUsers_whenNoResultFound_thenStatus204()
            throws Exception {
        repository.deleteAll();
        mvc.perform(get("/api/users")
                        .queryParam("pageStart", "1")
                        .queryParam("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }


    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void givenUser_whenGetUserId_thenStatus200()
            throws Exception {
        User user = repository.save(createUser());
        mvc.perform(get("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void notGivenUser_whenGetUserId_thenStatus404()
            throws Exception {
        mvc.perform(get("/api/users/{id}", 40)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void WhenPostUser_thenStatus201() throws Exception {
        String USER_EMAIL = "test@testing.com";
        String payload = "{\n" +
                "   \"email\":\"" + USER_EMAIL + "\",\n" +
                "   \"password\":\"secret\",\n" +
                "   \"firstName\":\"Thomas\",\n" +
                "   \"lastName\":\"Müller\",\n" +
                "   \"phone\":\"069-123456\",\n" +
                "   \"location\":\"Darmstadt\"\n" +
                "}\n";

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is(201))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void GivenUser_WhenPayloadIncomplete_thenStatus400() throws Exception {
        String payload = "{\n" +
                "   \"password\":\"secret\",\n" +
                "   \"firstName\":\"Thomas\",\n" +
                "   \"lastName\":\"Müller\",\n" +
                "   \"phone\":\"069-123456\",\n" +
                "   \"location\":\"Darmstadt\"\n" +
                "}\n";
        mvc.perform(post("/api/advertisements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is(400));

    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void WhenPostUser_AndEmailExists_thenStatus409() throws Exception {
        repository.save(createUser());

        String USER_EMAIL = "email@gmail.com";
        String payload = "{\n" +
                "   \"email\":\"" + USER_EMAIL + "\",\n" +
                "   \"password\":\"secret\",\n" +
                "   \"firstName\":\"Thomas\",\n" +
                "   \"lastName\":\"Müller\",\n" +
                "   \"phone\":\"069-123456\",\n" +
                "   \"location\":\"Darmstadt\"\n" +
                "}\n";


        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is(409));

    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void WhenPutNotepad_thenStatus200() throws Exception {
        User user = repository.save(createUser());
        AD ad = adRepository.save(createAd());
        notepadRepository.save(createNotepad(user, ad));
        String NOTEPAD_AD_ID = ad.getId().toString();
        String payload = "{\n" +
                "        \"advertisementId\": " + NOTEPAD_AD_ID + ",\n" +
                "        \"note\": \"Zimmer direkt bei der HS\"\n" +
                "    }\n";
        mvc.perform(put("/api/users/{userId}/notepad", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                        .andExpect(status().is(200))
                        .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void WhenPutNotepad_AndPayloadNotComplete_thenStatus400() throws Exception {
        User user = repository.save(createUser());
        AD ad = adRepository.save(createAd());
        notepadRepository.save(createNotepad(user, ad));

        String payload = "{\n" +
                "        \"note\": \"Zimmer direkt bei der HS\"\n" +
                "    }\n";
        mvc.perform(put("/api/users/{userId}/notepad", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is(400));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void WhenDeleteNotepad_thenStatus204() throws Exception {
        User user = repository.save(createUser());
        AD ad = adRepository.save(createAd());
        notepadRepository.save(createNotepad(user, ad));
        mvc.perform(delete("/api/users/{userId}/notepad/{adId}", user.getId(), ad.getId()))
                .andExpect(status().is(204));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void WhenGetNotepad_thenStatus200() throws Exception {
        User user = repository.save(createUser());
        AD ad = adRepository.save(createAd());
        notepadRepository.save(createNotepad(user, ad));

        mvc.perform(get("/api/users/{userId}/notepad", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void WhenGetNotepad_AndNothingFound_thenStatus204() throws Exception {
        User user = repository.save(createUser());
        AD ad = adRepository.save(createAd());
        mvc.perform(get("/api/users/{userId}/notepad", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void WhenGetNotepad_andUserNotFound_thenStatus404() throws Exception {
        mvc.perform(get("/api/users/{userId}/notepad", 30)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}