package com.github.harboat.battleshipsfrontendtests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.*;

@Test
public class GameConfigurationTest {

    private ChromeDriver playerOne;
    private ChromeDriver playerTwo;
    private final TestHelper battleships = new TestHelper();

    @BeforeMethod(onlyForGroups = {"onePlayer"})
    public void setUpChromeDriverForOnePlayer() {
        WebDriverManager.chromiumdriver().setup();
        playerOne = new ChromeDriver();
        playerOne.get("http://207.154.222.51:8080");
        battleships.logIn("kamil@email.com", "testtest", playerOne);
    }

    @BeforeMethod(onlyForGroups = {"twoPlayers"})
    public void setUpChromeDriverForTwoPlayers() {
        WebDriverManager.chromiumdriver().setup();
        playerOne = new ChromeDriver();
        playerOne.get("http://207.154.222.51:8080");
        playerTwo = new ChromeDriver();
        playerTwo.get("http://207.154.222.51:8080");
        battleships.logIn("kamil@email.com", "testtest", playerOne);
        battleships.logIn("maciek@email.com", "testtest", playerTwo);

    }

    @AfterMethod(onlyForGroups = {"onePlayer"})
    public void terminateChromeDriverForOnePlayer() {
        playerOne.quit();
    }

    @AfterMethod(onlyForGroups = {"twoPlayers"})
    public void terminateChromeDriverForTwoPlayers() {
        playerOne.quit();
        playerTwo.quit();
    }

    @Test(groups = {"onePlayer"}, description = "Player one can generate fleet")
    public void playerOneCanGenerateFleet() {
        // given
        var expectedCellAmount = 100;
        battleships.createGame(playerOne);
        // when
        var playerOneFleet = battleships.generateFleet(playerOne);
        var cellAmount = playerOneFleet.findElements(By.className("cell")).size();
        // then
        assertEquals(expectedCellAmount, cellAmount);
    }

    @Test(groups = {"twoPlayers"}, description = "Player that created a room can change size of board",
            dependsOnMethods = {"playerOneCanGenerateFleet"})
    public void playerCanChangeBoardSize() {
        // given
        var expectedCellAmount = 225;
        battleships.createGame(playerOne);
        // when
        battleships.changeBoardSize(15, 15, playerOne);
        var playerOneFleet = battleships.generateFleet(playerOne);
        var cellAmount = playerOneFleet.findElements(By.className("cell")).size();
        // then
        assertEquals(expectedCellAmount, cellAmount);
    }

    @Test(groups = {"twoPlayers"},
            description = "Player one can start the game after generating fleet and only if player Two is ready")
    public void playerOneCanStartGame() {
        // given
        var roomId = battleships.createGame(playerOne);
        battleships.joinGame(roomId, playerTwo);
        battleships.generateFleet(playerOne);
        battleships.generateFleet(playerTwo);
        battleships.playerReady(playerTwo);
        // when
        battleships.startGame(playerOne);
        var fluentWait = fluentWait(playerOne);
        var turnElement = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("turn")));
        // then
        assertNotNull(turnElement);
    }

    @Test(groups = {"twoPlayers"}, description = "Player two can be ready after generating fleet")
    public void playerTwoCanBeReadyAfterGeneratingFleet() {
        // given
        var roomId = battleships.createGame(playerOne);
        battleships.joinGame(roomId, playerTwo);
        battleships.generateFleet(playerOne);
        battleships.generateFleet(playerTwo);
        // when
        battleships.playerReady(playerTwo);
        var fluentWait = fluentWait(playerTwo);
        var buttonColorIsGreen = fluentWait.until(ExpectedConditions.attributeContains(By.id("readyButton"),
                "style", "background-color: rgb(163, 190, 140); border-color: rgb(163, 190, 140);"));
        // then
        assertTrue(buttonColorIsGreen);
    }

    private FluentWait<WebDriver> fluentWait(ChromeDriver driver) {
        return new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
    }

}
