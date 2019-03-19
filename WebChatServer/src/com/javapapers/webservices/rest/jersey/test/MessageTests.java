package com.javapapers.webservices.rest.jersey.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.javapapers.webservices.rest.jersey.test.modules.MessageXML;

@RunWith(Suite.class)
@SuiteClasses({ MessageXML.class })
public class MessageTests {

}
