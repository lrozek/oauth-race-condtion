it reproduces race condition in UserInfoTokenServices when OAuth2ClientContext isn't request scoped and multiple threads modify same instance of OAuth2ClientContext. 
The problem is in following snippet of getMap method in UserInfoTokenServices class:


			OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext()
					.getAccessToken();
			if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
				DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(
						accessToken);
				token.setTokenType(this.tokenType);
				restTemplate.getOAuth2ClientContext().setAccessToken(token);
			}

"mvn clean test" to reproduce the issue
The OauthIssueApplicationTests.itShouldReturnUserName should fail with either 
java.lang.AssertionError: Response content expected:<Aldis> but was:<Bradley>
or
java.lang.AssertionError: Response content expected:<Bradley> but was:<Aldis>

In pom change to spring boot version higher than 1.4.0.RELEASE and tests should pass

The issue is with OAuth2ClientIdCondition in OAuth2RestOperationsConfiguration
in 1.4.0 the OAuth2ClientIdCondition it returns matching ConditionOutcome based on security.oauth2.client.client-id

After following change:
https://github.com/spring-projects/spring-boot/commit/7396ccfe04994c97b45b2e52d49bb0754575118f
the OAuth2ClientIdCondition always matches regardless if security.oauth2.client.client-id is set or not.

This means that above 1.4.0.RELEASE OAuth2RestOperationsConfiguration.RequestScopedConfiguration is enabled, creating request scoped OAuth2ClientContext
and fixing race condition.
