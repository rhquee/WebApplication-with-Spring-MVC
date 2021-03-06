package webController;

import config.SpringRootConfig;
import config.SpringWebConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by kfrak on 05.12.2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration (classes = {SpringRootConfig.class, SpringWebConfig.class})
public class NameControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void resolveNameView_showErrorPageForEmptyName() throws Exception {
        this.mockMvc.perform(post("/hello")
                .param("name", "")
                .param("type", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("errorEmptyStringView"));
    }

    @Test (expected = IllegalArgumentException.class )
    public void resolveNameView_showErrorPageForNull() throws Exception {
        this.mockMvc.perform(post("/hello")
                .param("name", null)
                .param("type", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("errorEmptyStringView"));
    }

    @Test
    public void resolveNameView_showHelloPageForGivenName() throws Exception {
        this.mockMvc.perform(post("/hello")
                .param("name", "Rafael")
                .param("type", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"))
                .andExpect(model().attribute("name", "Rafael"));
    }

    @Test
    public void resolveNameView_showHelloPageForHal() throws Exception {
        this.mockMvc.perform(post("/hello")
                .param("name", "hal")
                .param("type", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("halView"))
                .andExpect(model().attribute("responseString", "My mind is going. I can feel it"));
    }

    @Test
    public void resolveView_showHelloPageForDavid() throws Exception {
        this.mockMvc.perform(post("/hello")
                .param("name", "david")
                .param("type", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("davidView"))
                .andExpect(model().attribute("responseString", "David here"));
    }

    @Test
    public void resolveView_show418ErrorPageForJohny() throws Exception {
        this.mockMvc.perform(post("/hello")
                .param("name", "johny")
                .param("type", "name"))
                .andExpect(status().is(418))
                .andExpect(view().name("error-418"))
                .andExpect(model().attribute("responseString", "Hi, I'm teapot"));
    }


    @Test
    public void resolveNameView_showHelloPageForUnnamedGetMethod() throws Exception {
        this.mockMvc.perform(get("/hello")
                .param("name", "nieznajomy")
                .param("type", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"))
                .andExpect(model().attribute("name", "nieznajomy"));
    }
}