package pl.lrozek;

import static org.springframework.security.oauth2.common.OAuth2AccessToken.BEARER_TYPE;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.lrozek.controller.UserController.USER_PATH;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OauthIssueApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Test
    public void itShouldReturnUserName() throws Exception {
        //given
        String token = "John";

        //when
        ResultActions result = mockMvc.perform( get( USER_PATH ).header( "Authorization", BEARER_TYPE + " " + token ) );

        //then
        result.andExpect( status().isOk() );
        result.andExpect( content().string( token ) );
    }

    @Test
    public void itShouldForbidAccessForUnauthorizedUser() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform( get( USER_PATH ) );

        //then
        result.andExpect( status().isUnauthorized() );

    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                                 .webAppContextSetup( wac )
                                 .apply( springSecurity() )
                                 .build();
    }

}
