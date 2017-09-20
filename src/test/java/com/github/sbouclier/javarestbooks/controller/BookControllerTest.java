package com.github.sbouclier.javarestbooks.controller;

import com.github.sbouclier.javarestbooks.JavaRestBooksApplication;
import com.github.sbouclier.javarestbooks.domain.Author;
import com.github.sbouclier.javarestbooks.domain.Book;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * BookController test
 *
 * @author Stéphane Bouclier
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JavaRestBooksApplication.class)
@WebAppConfiguration
@Transactional
public class BookControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
        Assert.assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
    }

    @SuppressWarnings("unchecked")
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    // ---------- create book ----------

    @Test
    public void should_create_valid_book_and_return_created_status() throws Exception {
        Book book = new Book("123-1234567890","My new book","Publisher");
        book.addAuthor(new Author("John","Doe"));

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(book)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is("http://localhost/api/books/123-1234567890")))
                .andExpect(content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void should_not_create_invalid_content_book_and_return_bad_request_status() throws Exception {
        Book book = new Book(null,"My new book","Publisher");

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(book)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void should_not_create_existing_book_and_return_conflict_status() throws Exception {
        Book book = new Book("978-0321356680","My new book","Publisher");
        book.addAuthor(new Author("John","Doe"));

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(book)))
                .andExpect(status().isConflict())
                .andDo(MockMvcResultHandlers.print());
    }

    // ---------- get book ----------

    @Test
    public void should_get_valid_book_with_ok_status() throws Exception {
        mockMvc.perform(get("/api/books/978-0321356680").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Effective Java")))
                .andExpect(jsonPath("$.publisher", is("Addison Wesley")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void should_no_get_unknown_book_with_not_found_status() throws Exception {
        mockMvc.perform(get("/api/books/000-1234567890").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].logref", is("error")))
                .andExpect(jsonPath("$[0].message", containsString("could not find book with ISBN: '000-1234567890'")))
                .andDo(MockMvcResultHandlers.print());
    }
    
}