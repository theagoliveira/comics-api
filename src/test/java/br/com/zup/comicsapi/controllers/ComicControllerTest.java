package br.com.zup.comicsapi.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.zup.comicsapi.models.Comic;
import br.com.zup.comicsapi.repositories.ComicRepository;
import br.com.zup.comicsapi.services.ComicService;

@SpringBootTest
@AutoConfigureMockMvc
class ComicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ComicService comicService;

    @Test
    void index() throws Exception {
        Set<String> authors = new HashSet<>(List.of("Author 1", "Author 2"));
        Comic comic1 = new Comic(1L, "Title1", 1.99, authors, "12345678900", "A comic");
        Comic comic2 = new Comic(2L, "Title2", 2.99, authors, "12345678900", "A comic");

        when(comicService.findAll()).thenReturn(List.of(comic1, comic2));

        mockMvc.perform(get(ComicController.BASE_URI))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$.[0].comicId", equalTo(1)))
               .andExpect(jsonPath("$.[0].title", equalTo("Title1")))
               .andExpect(jsonPath("$.[0].price", equalTo(1.99)))
               .andExpect(jsonPath("$.[0].isbn", equalTo("12345678900")))
               .andExpect(jsonPath("$.[0].description", equalTo("A comic")));

        verify(comicService).findAll();
    }

    @Test
    void deleteNotSupported() throws Exception {
        mockMvc.perform(delete(ComicController.BASE_URI)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void putNotSupported() throws Exception {
        mockMvc.perform(put(ComicController.BASE_URI)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void patchNotSupported() throws Exception {
        mockMvc.perform(patch(ComicController.BASE_URI)).andExpect(status().isMethodNotAllowed());
    }

}
