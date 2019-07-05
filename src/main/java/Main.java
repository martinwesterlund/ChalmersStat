import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.*;

public class Main {
    private static ArrayList<String> refNumberArrayList = new ArrayList<String>();
    private static int totalOffers = 1;

    public static void main(String[] args) throws IOException {

        //Fill in correct filename here
        Scanner s = new Scanner(new File("file.txt"));


        ArrayList<String> refNumbers = new ArrayList();
        while (s.hasNext()) {
            refNumbers.add(s.next());
        }
        s.close();

        //Fill in username and password here
        String username = "xxx";
        String password = "xxx";

        ChromeDriver browser = new ChromeDriver();
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        options.setHeadless(false);
        browser.get("http://login.reachmee.com/chalmers/?V8");

        WebElement usernameField = new WebDriverWait(browser, 5).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"userID\"]")));
        WebElement passwordField = new WebDriverWait(browser, 5).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"password\"]")));
        WebElement loginButtonField = new WebDriverWait(browser, 5).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"loginbtn2\"]")));
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButtonField.click();

        WebElement recruitmentField = new WebDriverWait(browser, 5).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"headmenuholder\"]/table/tbody/tr/td[3]/a")));
        recruitmentField.click();
        System.out.println("Loaded recruitments");

        WebElement allRecruitmentsField = new WebDriverWait(browser, 5).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"CommissionMenu_lnkAllCommissionsShort\"]")));
        allRecruitmentsField.click();
        System.out.println("Listing all recruitments");

        WebElement filterField = new WebDriverWait(browser, 5).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"lnkToggleFilter\"]/span")));
        filterField.click();
        System.out.println("Clicked on filter");

        for (int i = 0; i < refNumbers.size(); i++) {
//            ChromeDriver browser = new ChromeDriver();
//            ChromeOptions options = new ChromeOptions();
//            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
//            options.setHeadless(false);
            getInfo(refNumbers.get(i), browser);
        }

        browser.close();
        printResults();


    }

    public static void printResults() throws IOException {

        // Fill in output filename here
        PrintWriter pw = new PrintWriter(new FileOutputStream("File_answers.txt"));
        for (int i = 0; i < refNumberArrayList.size(); i++) {
            pw.println(refNumberArrayList.get(i));
        }
        pw.close();

    }

    public static void getInfo(String refId, ChromeDriver browser) {

        WebElement refIdField = new WebDriverWait(browser, 1).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"AppGeneral_tbCommissionId_txtTextBox\"]")));
        refIdField.clear();
        refIdField.sendKeys(refId);
        System.out.println("Filling in Ref number");

        WebElement updateFilterField = new WebDriverWait(browser, 1).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"btnFilter\"]")));
        updateFilterField.click();
        System.out.println("Updated filter");

        WebElement projectField = new WebDriverWait(browser, 1).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"comm\"]/a")));
        projectField.click();
        System.out.println("Clicked on the project " + refId);

        WebElement organisationField = new WebDriverWait(browser, 1).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"ContentPlaceHolder_CtlProjectDescription_ContentPlaceHolder_CtlProjectDescription_rmcpOrganisation_lblTitle\"]")));
        organisationField.click();

        WebElement departmentField = new WebDriverWait(browser, 1).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), 'Institution')]//parent::td//following-sibling::td//option[@selected='selected']")));
        String department = departmentField.getText();
        System.out.println("Getting department");

        WebElement candidatesField = new WebDriverWait(browser, 1).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), 'Kandidater')]")));
        candidatesField.click();
        System.out.println("Clicking on candidates view");

        try {
            WebElement numberOfOffersField = new WebDriverWait(browser, 1).
                    until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(), 'Jobberbjudande')]")));
            if (!numberOfOffersField.getText().contains("0") && !numberOfOffersField.getText().contains("1")) {
                totalOffers = Integer.parseInt(numberOfOffersField.getText().substring(16, 17));
                System.out.println(totalOffers);

            }

        } catch (TimeoutException e) {
           // String answer = "The job offer folder was not found";
            System.out.println("The job offer folder was not found");
            //refNumberMap.put(refId + " " + 0, answer);
        }

        for (int i = 1; i < totalOffers + 1; i++) {
            try {
                WebElement candidatesField2 = new WebDriverWait(browser, 1).
                        until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), 'Kandidater')]")));
                candidatesField2.click();
                System.out.println("Clicking on candidates view");

                WebElement allCandidatesField = new WebDriverWait(browser, 1).
                        until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(), 'Visa samtliga kandidater')]")));
                allCandidatesField.click();
                System.out.println("Clicking on the all candidates list");


                WebElement happyCandidateField = new WebDriverWait(browser, 1).
                        until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//img[@src='../images/workflow_28.gif'])[" + i + "]//parent::td//following-sibling::td/a")));
                happyCandidateField.click();
                System.out.println("Clicking on the happy candidate's profile");


                try {
                    WebElement questionField = new WebDriverWait(browser, 1).
                            //until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(), 'At what university did you obtain your most relevant Masters’ degree?')]")));
                            until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(), 'At what university did you obtain your PhD degree?')]")));

                    String question = questionField.getText();
                    System.out.println("Question 1 is " + question);

                    try {
                        WebElement answerField = new WebDriverWait(browser, 1).
                                //until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(), 'At what university did you obtain your most relevant Masters’ degree?')]/following-sibling::table/tbody/tr/td/img[@src='../images/radiobutton_on.gif']//parent::td//following-sibling::td[text()]")));
                                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(), 'At what university did you obtain your PhD degree?')]/following-sibling::table/tbody/tr/td/img[@src='../images/radiobutton_on.gif']//parent::td//following-sibling::td[text()]")));
                        String answer = answerField.getText();
                        System.out.println("Answer is " + answer);
                        refNumberArrayList.add(department + "\t" + refId + "\t" + i + "\t" + answer);

                    } catch (TimeoutException e) {
                        String answer = "No answer from candidate";
                        refNumberArrayList.add(department + "\t" + refId + "\t" + i + "\t" + answer);
                    }
                } catch (TimeoutException e) {
                    System.out.println("Did not find alternative question, now looking for free text question");
                    try {
                        WebElement questionTwoField = new WebDriverWait(browser, 1).

                                // Enable below row for PhD student positions
                                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(), 'At what University did you take your Master')]")));

                                // Enable below row for Postdoc positions
                               // until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'at what university did you earn your phd degree?')]")));
                        String question = questionTwoField.getText();
                        System.out.println("Question 2 is " + question);
                        try {
                            // Enable below row for PhD student positions
                            WebElement answerField = questionTwoField.findElement(By.xpath("//p[contains(text(), 'At what University did you take your Master')]//following-sibling::div"));

                            // Enable below row for Postdoc positions
                            // WebElement answerField = questionTwoField.findElement(By.xpath("//p[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'at what university did you earn your phd degree?')]//following-sibling::div"));

                            String answer = "FRITEXT - " + answerField.getText();
                            if(answer.equals("")){
                                answer = "No answer from candidate";
                            }
                            System.out.println("Answer is " + answer);
                            refNumberArrayList.add(department + "\t" + refId + "\t" + i + "\t" + answer);
                        } catch (TimeoutException e2) {
                            String answer = "No answer from candidate";
                            System.out.println(answer);
                            refNumberArrayList.add(department + "\t" + refId + "\t" + i + "\t" + answer);
                        }

                    } catch (TimeoutException e2) {
                        String answer = "Question is missing";
                        System.out.println(answer);
                        refNumberArrayList.add(department + "\t" + refId + "\t" + i + "\t" + answer);
                    }
                }
            } catch (TimeoutException e) {
                System.out.println("No happy candidate here!");
                String answer = "No offer";
                System.out.println(answer);
                refNumberArrayList.add(department + "\t" + refId + "\t" + i + "\t" + answer);
            }

        }
        totalOffers = 1;
        WebElement recruitmentField = new WebDriverWait(browser, 1).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"headmenuholder\"]/table/tbody/tr/td[3]/a")));
        recruitmentField.click();
        System.out.println("Loaded recruitments");

    }
}
