package org.codekits.leanspring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SgtPeppers implements CompactDisc {

	@Autowired
	public void play() {
		System.out.println("SgtPeppers: play()");
	}

}
