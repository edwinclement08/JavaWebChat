package com.javapapers.webservices.rest.jersey.test.modules;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.time.Instant;

import org.junit.Test;

import com.javapapers.webservices.rest.jersey.models.User;

public class UserXML {
	@Test
	public void testDump() {
		long currentTimeEpoch = Instant.now().getEpochSecond();
		String token = User.generateRandomString(User.TOKEN_LENGTH);
		User user = new User("John", "test123", token, currentTimeEpoch);

		String XMLStringDump = user.XMLDump().toString();
//		System.out.println(XMLStringDump);
		String expectedResult = String.format(String.join("", "<user><name>John</name><password>test123</password>",
				"<token>%s</token><tokenCreationTime>%d</tokenCreationTime></user>"), token, currentTimeEpoch);

		assertEquals(expectedResult, XMLStringDump);
	}

	@Test
	public void testLoad() {
		long currentTimeEpoch = Instant.now().getEpochSecond();
		String genToken = User.generateRandomString(User.TOKEN_LENGTH);

		String inputXMLString = String.format(
				String.join("", "<user><name>John</name><password>test123</password>",
						"<token>%s</token><tokenCreationTime>%d</tokenCreationTime></user>"),
				genToken, currentTimeEpoch);

		User user = new User("", "");
		user.XMLLoad(inputXMLString);

		String name = user.getName();
		String password = user.getPassword();
		String token = user.getToken();
		long tokenCreationTime = user.gettokenCreationTime();

//		System.out.println(message.XMLDump());
//		System.out.println(inputXMLString);

		assertEquals("John", name);
		assertEquals("test123", password);
		assertEquals(genToken, token);
		assertEquals(currentTimeEpoch, tokenCreationTime);

	}

	@Test
	public void testFile() {
		long currentTimeEpoch = Instant.now().getEpochSecond();
		String token = User.generateRandomString(User.TOKEN_LENGTH);
		User user = new User("John", "test123", token, currentTimeEpoch);
		String userString = user.toString();

		String testFileName = "tempUserFileTest.user.xml";
		// if file exists, delete
		File xx = new File(testFileName);
		if (xx.exists()) {
			xx.delete();
		}

		boolean XMLDumpToFileStatus = user.XMLDumpToFile(testFileName);

		User newUser = new User("", "");
		boolean XMLLoadToFileStatus = newUser.XMLLoadFromFile(testFileName);

		String loadedUserString = newUser.toString();

		assertEquals(XMLDumpToFileStatus, true);
		assertEquals(XMLLoadToFileStatus, true);
		assertEquals(userString, loadedUserString);
	}
}
