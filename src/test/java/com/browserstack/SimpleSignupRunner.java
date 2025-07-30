package com.browserstack;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.testng.xml.XmlInclude;

import java.util.Arrays;

public class SimpleSignupRunner {
    public static void main(String[] args) {
        System.out.println("Starting testValidEmailSignup test execution...");
        
        // Create TestNG suite programmatically
        XmlSuite suite = new XmlSuite();
        suite.setName("SignupSuite");
        
        XmlTest test = new XmlTest(suite);
        test.setName("EmailSignupTest");
        
        XmlClass xmlClass = new XmlClass("com.browserstack.SignupTest");
        XmlInclude xmlInclude = new XmlInclude("testValidEmailSignup");
        xmlClass.setIncludedMethods(Arrays.asList(xmlInclude));
        test.setXmlClasses(Arrays.asList(xmlClass));
        
        TestNG testng = new TestNG();
        testng.setXmlSuites(Arrays.asList(suite));
        testng.setVerbose(2); // Set verbose level for more output
        
        System.out.println("Running test...");
        testng.run();
        System.out.println("Test execution completed.");
    }
}
