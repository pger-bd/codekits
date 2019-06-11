package org.codekits;

import static org.junit.Assert.assertNotNull;

import org.codekits.leanspring.bean.CDPlayerConfig;
import org.codekits.leanspring.bean.CompactDisc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CDPlayerConfig.class)
public class CDPlayerTest {

	@Autowired(required=true)
	private CompactDisc cd;
	
	@Test
	public void cdShuoldNotBeNull() {
		assertNotNull(cd);
	}
}
