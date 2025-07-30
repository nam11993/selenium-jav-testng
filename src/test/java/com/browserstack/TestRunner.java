package com.browserstack;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("Starting Email Signup Test...");
        
        // Create TestNG suite programmatically
        XmlSuite suite = new XmlSuite();
        suite.setName("EmailSignupTestSuite");
        
        XmlTest test = new XmlTest(suite);
        test.setName("EmailSignupTest");
        test.addParameter("browser", "chrome");
        
        XmlClass testClass = new XmlClass("com.browserstack.SignupTest");
        List<XmlInclude> methodsToRun = new ArrayList<>();
        methodsToRun.add(new XmlInclude("testValidEmailSignup"));
        testClass.setIncludedMethods(methodsToRun);
        
        test.setXmlClasses(List.of(testClass));
        
        List<XmlSuite> suites = new ArrayList<>();
        suites.add(suite);
        
        TestNG testng = new TestNG();
        testng.setXmlSuites(suites);
        
        try {
            System.out.println("Running testValidEmailSignup...");
            testng.run();
            System.out.println("Test execution completed!");
        } catch (Exception e) {
            System.err.println("Error running tests: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
