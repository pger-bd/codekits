package org.codekits.starter;

import org.codekits.conf.ConfigProperty;
import org.codekits.conf.ConfigPropertySingleFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
	@Autowired
	private ConfigProperty config;
	
	@Autowired
	private ConfigPropertySingleFile configFile;
	
	@GetMapping("/conf")
	public String getConf() {
		return config.uname+","+config.pwd;
	}
	
	@GetMapping("/confFile")
	public String getConfFile() {
		return "name: "+configFile.getUname();
	}
}
