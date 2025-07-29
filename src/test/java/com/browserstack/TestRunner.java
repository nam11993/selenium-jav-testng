package com.browserstack;

import org.testng.TestNG;
import java.util.Arrays;

public class TestRunner {
    public static void main(String[] args) {
        TestNG testng = new TestNG();
        testng.setTestClasses(new Class[] { WorkingLoginTest.class });
        testng.run();
    }
}
