import junit.framework.TestCase;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by agherasim on 28/11/2014.
 */
public class TestSmallStuff{

	@Test
	public void TestADate(){

		Date date = new Date();
		date.setTime(1417017600000L);
		System.out.println(date);
	}

}
