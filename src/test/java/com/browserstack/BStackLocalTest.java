package com.browserstack;

import org.testng.annotations.Test;
import static com.codeborne.selenide.Selenide.title;
import static com.codeborne.selenide.Selenide.open;
import org.testng.Assert;

public class BStackLocalTest extends LocalSelenideTest {

    @Test
    public void test() throws Exception {
        // Note: This test was originally for BrowserStack Local testing
        // For local execution, we'll test a publicly accessible site instead
        open("https://www.bstackdemo.com");

        String pageTitle = title();

        Assert.assertEquals(pageTitle, "StackDemo");
    }
}
