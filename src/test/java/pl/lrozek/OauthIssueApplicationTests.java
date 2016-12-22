package pl.lrozek;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.lrozek.controller.UserController.USER_PATH;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest
public class OauthIssueApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void itShouldReturnUserName() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform( get( USER_PATH ) );

        //then
        result.andExpect( status().isOk() );
        result.andExpect( content().string( "John" ) );

    }

}
