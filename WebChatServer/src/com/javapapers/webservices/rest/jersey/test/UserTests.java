package com.javapapers.webservices.rest.jersey.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.javapapers.webservices.rest.jersey.test.modules.UserXML;

@RunWith(Suite.class)
@SuiteClasses({ UserXML.class })
public class UserTests {

}
