package com.github.harboat.battleshipsfrontendtests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class GameTest {

    private ChromeDriver playerOne;
    private ChromeDriver playerTwo;
    private final TestHelper battleships = new TestHelper();


    @BeforeMethod(onlyForGroups = {"twoPlayers"})
    public void setUpChromeDriverForTwoPlayers() {
        WebDriverManager.chromiumdriver().setup();
        playerOne = new ChromeDriver();
        playerOne.get("http://207.154.222.51:8080");
        playerTwo = new ChromeDriver();
        playerTwo.get("http://207.154.222.51:8080");
        battleships.logIn("kamil@email.com", "testtest", playerOne);
        battleships.logIn("maciek@email.com", "testtest", playerTwo);
        var roomId = battleships.createGame(playerOne);
        battleships.joinGame(roomId, playerTwo);
        battleships.generateFleet(playerOne);
        battleships.generateFleet(playerTwo);
        battleships.playerReady(playerTwo);
        battleships.startGame(playerOne);
    }

    @AfterMethod(onlyForGroups = {"twoPlayers"})
    public void terminateChromeDriverForTwoPlayers() {
        playerOne.quit();
        playerTwo.quit();
    }

    @Test(groups = {"twoPlayers"}, description = "Starting player can shoot")
    public void startingPlayerCanShoot() {
        // given
        var shot = false;
        var playerOneStarts = battleships.checkIfPlayerStarts(playerOne);
        // when
        if (playerOneStarts) {
            shot = battleships.shoot(playerOne, "1");
        }
        if (!playerOneStarts) {
            shot = battleships.shoot(playerTwo, "2");
        }
        // then
        assertTrue(shot);
    }

    @Test(groups = {"twoPlayers"}, description = "Player can forfeit and that results in YOU LOST alert")
    public void playerCanForfeit() {
        // given
        var expectedForfeitText = "YOU LOST";
        // when
        battleships.forfeit(playerOne);
        var forfeitText = battleships.fluentWait(playerOne).until(ExpectedConditions.alertIsPresent()).getText();
        // then
        assertEquals(expectedForfeitText, forfeitText);
    }

    @Test(groups = {"twoPlayers"}, description = "Player wins after opponent presses forfeit button")
    public void playerWinsAfterOpponentForfeits() {
        // given
        var expectedForfeitText = "YOU WIN!";
        // when
        battleships.forfeit(playerOne);
        var forfeitText = battleships.fluentWait(playerTwo).until(ExpectedConditions.alertIsPresent()).getText();
        // then
        assertEquals(expectedForfeitText, forfeitText);
    }

}
