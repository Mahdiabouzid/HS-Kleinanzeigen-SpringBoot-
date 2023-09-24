package de.hs.da.hskleinanzeigen.controller;

import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

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
    private CategoryRepository repository;

    @AfterEach
    public void reset() {
        repository.deleteAll();
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void WhenPostCategory_thenStatus201() throws Exception {
        String CATEGORY_NAME = "test category";
        String payload = "{\n" +
                "   \"name\":\"" + CATEGORY_NAME + "\"\n" +
                "}\n";
        mvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is(201))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void WhenPostCategory_AndParentNotFound_thenStatus400() throws Exception {
        String CATEGORY_NAME = "test category";
        String payload = "{\n" +
                "   \"parentId\": 4711,\n" +
                "   \"name\":\"" + CATEGORY_NAME + "\"\n" +
                "}\n";
        mvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is(400));
    }

    @Test
    @WithMockUser(username="admin",roles="ADMIN")
    public void WhenPostCategory_AndCategoryAlreadyExists_thenStatus409() throws Exception {
        Category category = new Category();
        category.setName("test category");
        repository.save(category);
        String CATEGORY_NAME = "test category";
        String payload = "{\n" +
                "   \"name\":\"" + CATEGORY_NAME + "\"\n" +
                "}\n";
        mvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is(409));
    }

}