package core.bet365;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;



public class Main {

    private static String BET365_TABLE_TENNIS_REQUEST_URL = "https://www.bet365.com/#/AC/B92/C1/D50/E2/F163/";

    public static void main(String[] args) {

        ResultEntity resultEntity = new ResultEntity();
        HashSet<TableTennisEventEntity> tableTennisEventEntitiesSet = new HashSet<>();
        resultEntity.setTableTennisEventEntitySet(tableTennisEventEntitiesSet);

        WebDriver driver = null;
        Process process = null;
        try {

            Date date = new Date(System.currentTimeMillis());
            resultEntity.setTime(date);

            process = new ProcessBuilder("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"
                    , BET365_TABLE_TENNIS_REQUEST_URL, "--remote-debugging-port=9222",
                    "--user-data-dir=C:\\\\Temp").start();


            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
            driver = new ChromeDriver(options);


            String participantsXpath = "//*[contains(@class, 'rcl-ParticipantFixtureDetails gl-Market_General-cn1 ')]";
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(participantsXpath)));
            List<WebElement> participants = driver.findElements(By.xpath(participantsXpath));

            List<WebElement> odds = getOddsWebElements(driver, wait);

            List<WebElement> dates = getDatesWebElements(driver, wait);

            TreeMap<Integer, List<WebElement>> grouped = groupWebElements(participants, odds, dates);

            parseWebElements(resultEntity, grouped);

            persistResultEntity(resultEntity);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                driver.close();
                Process p = Runtime.getRuntime().exec("taskkill /im chromedriver.exe /f");
            } catch (IOException e) {
            }
        }
    }

    private static List<WebElement> getOddsWebElements(WebDriver driver, WebDriverWait wait) {
        String oddsXpath = "//*[contains(@class, 'sgl-ParticipantOddsOnly80 gl-Participant_General gl-Market_General-cn1 ')]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(oddsXpath)));
        List<WebElement> odds = driver.findElements(By.xpath(oddsXpath));
        return odds;
    }

    private static List<WebElement> getDatesWebElements(WebDriver driver, WebDriverWait wait) {
        String datesXpath = "//*[contains(@class, 'rcl-MarketHeaderLabel rcl-MarketHeaderLabel-isdate ')]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(datesXpath)));
        List<WebElement> dates = driver.findElements(By.xpath(datesXpath));
        return dates;
    }

    private static TreeMap<Integer, List<WebElement>> groupWebElements(List<WebElement> participants, List<WebElement> odds, List<WebElement> dates) {
        List<WebElement> all = new ArrayList<>();
        all.addAll(participants);
        all.addAll(odds);
        all.addAll(dates);
        TreeMap<Integer, List<WebElement>> grouped = new TreeMap<>();


        for (WebElement webEl : all) {
            int currentY = webEl.getLocation().y;

            if (grouped.containsKey(currentY)) {
                grouped.get(currentY).add(webEl);
            } else {
                ArrayList<WebElement> webElements = new ArrayList<>();
                webElements.add(webEl);
                grouped.put(currentY, webElements);
            }

        }
        return grouped;
    }

    private static void parseWebElements(ResultEntity resultEntity, TreeMap<Integer, List<WebElement>> grouped) {
        Date eventDate = null;
        for (Map.Entry<Integer, List<WebElement>> entry : grouped.entrySet()) {

            try {
                if (entry.getValue().size() == 3) {
                    if (eventDate == null) {
                        continue;
                    }

                    TableTennisEventEntity ttee = new TableTennisEventEntity();

                    String eventParts[] = entry.getValue().get(0).getText().split("\n");
                    String fm = entry.getValue().get(1).getText();
                    String sm = entry.getValue().get(2).getText();

                    if (fm.equals("") || sm.equals("") || eventParts[0].toLowerCase(Locale.ROOT).equals("live")) {
                        continue;
                    }

                    LocalTime localTime = LocalTime.parse(eventParts[0], DateTimeFormatter.ofPattern("HH:mm"));

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(eventDate);
                    calendar.add(Calendar.HOUR, localTime.get(ChronoField.HOUR_OF_DAY));
                    calendar.add(Calendar.MINUTE, localTime.get(ChronoField.MINUTE_OF_HOUR));

                    Date finalDateOfEvent = calendar.getTime();

                    ttee.setEventDate(finalDateOfEvent);
                    ttee.setFirstPlayerName(eventParts[1]);
                    ttee.setSecondPlayerName(eventParts[2]);
                    ttee.setFirstPlayerWinningOdd(Double.parseDouble(fm));
                    ttee.setSecondPlayerWinningOdd(Double.parseDouble(sm));


                    HashSet<ResultEntity> resultEntitySet = new HashSet<>();
                    resultEntitySet.add(resultEntity);
                    ttee.setResultEntity(resultEntitySet);

                    resultEntity.getTableTennisEventEntitySet().add(ttee);

                }
                if (entry.getValue().size() == 1) {
                    String[] eventDateParts = entry.getValue().get(0).getText().split(" ");
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    int day = Integer.parseInt(eventDateParts[1]);
                    String monthASString = eventDateParts[2];

                    SimpleDateFormat formatter = new SimpleDateFormat("dddd-MMM-yyyy", Locale.getDefault());
                    eventDate = formatter.parse(day + "-" + monthASString + "-" + year);
                    System.out.println(eventDate.toString());

                }
            } catch (Exception e) {
            }
        }
    }

    private static void persistResultEntity(ResultEntity resultEntity) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("core.bet365.persistent_unit_xml");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        resultEntity.getTableTennisEventEntitySet().forEach(tt -> entityManager.persist(tt));
        entityManager.persist(resultEntity);
        entityManager.getTransaction().commit();
    }
}
