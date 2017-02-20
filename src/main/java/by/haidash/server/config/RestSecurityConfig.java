package by.haidash.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import by.haidash.server.config.hmac.HmacAccessFilter;

@Configuration
@EnableWebSecurity
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private HmacAccessFilter hmFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(hmFilter, AnonymousAuthenticationFilter.class);
	}
}