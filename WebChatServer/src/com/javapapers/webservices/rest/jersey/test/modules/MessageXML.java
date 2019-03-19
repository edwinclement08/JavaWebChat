package com.javapapers.webservices.rest.jersey.test.modules;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.junit.Test;

import com.javapapers.webservices.rest.jersey.models.Message;

public class MessageXML {

	@Test
	public void testDump() {
		long currentTimeEpoch = Instant.now().getEpochSecond();

		Message message = new Message();
		message.setId(1);
		message.setMessage("Message Test 101");
		message.setSender("John");
		message.setReceiver("Susan");
		message.setTimeSent(currentTimeEpoch);

		String XMLStringDump = message.XMLDump().toString();
		String expectedResult = String.format(
				String.join("", "<message><content>Message Test 101</content><receiver>Susan</receiver>",
						"<sender>John</sender><timeSent>%d</timeSent><transferred>false</transferred></message>"),
				currentTimeEpoch);

		assertEquals(expectedResult, XMLStringDump);
	}

	@Test
	public void testLoad() {
		long currentTimeEpoch = Instant.now().getEpochSecond();

		String inputXMLString = String.format(
				String.join("", "<message><content>Message Test 101</content><receiver>Susan</receiver>",
						"<sender>John</sender><timeSent>%d</timeSent><transferred>false</transferred></message>"),
				currentTimeEpoch);

		Message message = new Message();
		message.XMLLoad(inputXMLString);

		String content = message.getMessage();
		String sender = message.getSender();
		String receiver = message.getReceiver();
		long timeSent = message.getTimeSent();

//		System.out.println(message.XMLDump());
//		System.out.println(inputXMLString);

		assertEquals("Message Test 101", content);
		assertEquals("John", sender);
		assertEquals("Susan", receiver);
		assertEquals(currentTimeEpoch, timeSent);

	}

}
