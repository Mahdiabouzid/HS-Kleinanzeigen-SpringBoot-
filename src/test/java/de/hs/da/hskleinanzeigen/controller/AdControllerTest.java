package de.hs.da.hskleinanzeigen.controller;

import de.hs.da.hskleinanzeigen.entity.AD;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.enumeration.Type;
import de.hs.da.hskleinanzeigen.repository.AdRepository;
import de.hs.da.hskleinanzeigen.repository.CategoryRepository;
import de.hs.da.hskleinanzeigen.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdControllerTest {

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
    private AdRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @AfterEach
    void setUp() {
        repository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
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
    private AD arrangeAd() {
        AD ad = new AD();
        ad.setPrice(1);
        ad.setTitle("cool ad");
        //ad.setId(1);
        ad.setDescription("cool description");
        User user = new User();
        user.setEmail("email@gmail.com");
        user.setCreated(LocalDateTime.now());
        user.setPassword("password");
        ad.setUser(user);
        ad.setCategory(new Category());
        ad.setType(Type.OFFER);
        ad.setLocation("Darmstadt");
        ad.setCreated(LocalDateTime.now());
        return ad;
    }


    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void givenAD_whenGetAll_thenStatus200()
            throws Exception {
        AD ad = arrangeAd();
        repository.save(ad);

        mvc.perform(get("/api/advertisements")
                        .queryParam("pageStart", "0")
                        .queryParam("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void whenGetAll_andMissingParameters_thenStatus400()
            throws Exception {
        AD ad = arrangeAd();
        repository.save(ad);

        mvc.perform(get("/api/advertisements")
                        .queryParam("pageStart", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }



    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void whenGetAll_andNoAdFound_thenStatus204()
            throws Exception {

        mvc.perform(get("/api/advertisements")
                        .queryParam("pageStart", "1")
                        .queryParam("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void whenGetAll_andNoAdFound_withCategory_thenStatus204()
            throws Exception {

        mvc.perform(get("/api/advertisements")
                        .queryParam("pageStart", "1")
                        .queryParam("pageSize", "2")
                        .queryParam("category", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void givenAD_whenGetADId_thenStatus200()
            throws Exception {
       AD ads = repository.save(arrangeAd());
        mvc.perform(get("/api/advertisements/{id}", ads.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void notGivenAD_whenGetADId_thenStatus404()
            throws Exception {
        mvc.perform(get("/api/advertisements/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void GivenAd_WhenPostAd_thenStatus201() throws Exception {
        Category category = new Category();
        category.setName("test category");
        Category cat = categoryRepository.save(category);
        User user = userRepository.save(createUser());
        String ADpayload = "{\n" +
                "    \"type\" : \"OFFER\",\n" +
                "    \"category\" : {\"id\" : "+ cat.getId() + "},\n" +
                "    \"title\":\"Zimmer in 4er WG\",\n" +
                "    \"description\":\"Wohnheim direkt neben der HS\",\n" +
                "    \"price\" : 400,\n" +
                "    \"location\":\"Birkenweg, Darmstadt\",\n" +
                "    \"user\" : {\"id\" : "+ user.getId() + "}\n" +
                "}";
        mvc.perform(post("/api/advertisements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ADpayload))
                .andExpect(status().is(201))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void GivenAd_WhenPayloadIncomplete_thenStatus400() throws Exception {
        AD ad = arrangeAd();
        repository.save(ad);
        String payload = "{\n" +
                "    \"type\" : \"OFFER\",\n" +
                "    \"category\" : {\"id\" : 1},\n" +
                "    \"title\":\"Zimmer in 4er WG\",\n" +
                "    \"description\":\"Wohnheim direkt neben der HS\",\n" +
                "    \"location\":\"Birkenweg, Darmstadt\",\n" +
                "    \"user\" : {\"id\" : 1}\n" +
                "}";
        mvc.perform(post("/api/advertisements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is(400));

    }

}