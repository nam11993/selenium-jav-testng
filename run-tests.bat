@echo off
echo ================================
echo Local Selenium Test Runner
echo ================================
echo.

cd /d "d:\Microsoft VS Code\New folder\local-selenium-tests"

echo Please select which tests to run:
echo 1. All Tests (Default TestNG configuration)
echo 2. Signup Tests Only
echo 3. BrowserStack Demo Tests (Suite Tests)
echo 4. All Test Classes
echo 5. Single Test Class (you'll be prompted)
echo.

set /p choice=Enter your choice (1-5): 

if "%choice%"=="1" (
    echo Running default tests...
    mvn clean test
) else if "%choice%"=="2" (
    echo Running signup tests only...
    mvn clean test -DsuiteXmlFile=src/test/resources/signup-tests.xml
) else if "%choice%"=="3" (
    echo Running suite tests only...
    mvn clean test -DsuiteXmlFile=src/test/resources/suite-tests.xml
) else if "%choice%"=="4" (
    echo Running all test classes...
    mvn clean test -DsuiteXmlFile=src/test/resources/all-tests.xml
) else if "%choice%"=="5" (
    echo.
    echo Available test classes:
    echo - SignupTest
    echo - BStackSampleTest
    echo - BStackLocalTest
    echo - com.browserstack.suite.SuiteTest01
    echo - com.browserstack.suite.SuiteTest02
    echo - com.browserstack.suite.SuiteTest03
    echo - com.browserstack.suite.SuiteTest04
    echo.
    set /p testclass=Enter test class name: 
    echo Running test class: !testclass!
    mvn clean test -Dtest=!testclass!
) else (
    echo Invalid choice. Running default tests...
    mvn clean test
)

echo.
echo ================================
echo Tests completed!
echo ================================
echo Test reports are available in target/surefire-reports/
echo Check the output above for results.
pause
