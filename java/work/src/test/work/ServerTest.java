package work;

import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import br.ufu.sd.work.server.Server;

public class ServerTest {

	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testStart() {
		Server s;
		s = new Server();
		Assert.assertTrue(s.start(61666));
		
	}
}