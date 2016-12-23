package pl.lrozek;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.aop.support.AopUtils.isJdkDynamicProxy;
import static org.springframework.security.oauth2.common.OAuth2AccessToken.BEARER_TYPE;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.lrozek.controller.UserController.USER_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.lrozek.util.MyReflectionUtils;

import com.github.tomakehurst.wiremock.WireMockServer;

@SpringBootTest
@TestPropertySource(properties = "debug=true")
public class OauthIssueApplicationTests extends AbstractTestNGSpringContextTests {

    private WireMockServer wireMockServer;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserInfoTokenServices tokenServices;

    @DataProvider(name = "users")
    public static Object[][] user() {
        return new Object[][] { { "Bradley" }, { "Aldis" } };
    }

    @Test(dataProvider = "users", threadPoolSize = 2, invocationCount = 1)
    public void itShouldReturnUserName( String token ) throws Exception {
        //given

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

    @Test
    public void oAuth2ClientContextShouldBeProxy() throws Exception {
        //given

        //when
        OAuth2RestTemplate oAuth2RestTemplate = MyReflectionUtils.getField( tokenServices, "restTemplate" );
        OAuth2ClientContext context = MyReflectionUtils.getField( oAuth2RestTemplate, "context" );

        //then
        assertThat( isJdkDynamicProxy( context ), is( true ) );
    }

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders
                                 .webAppContextSetup( wac )
                                 .apply( springSecurity() )
                                 .build();
    }

    @BeforeClass
    public void setupWireMock() {
        wireMockServer = new WireMockServer( 8081 );
        wireMockServer.start();
    }

    @AfterClass
    public void cleanupWireMock() {
        wireMockServer.stop();
    }

}
