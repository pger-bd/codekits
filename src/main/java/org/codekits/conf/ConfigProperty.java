package org.codekits.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigProperty {

	@Value("${user.uname}")
	public String uname;
	
	@Value("${user.pwd}")
	public String pwd;
	
}
