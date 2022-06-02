package com.github.harboat.battleshipsfrontendtests;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class MenuTest {

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

    @BeforeMethod(onlyForGroups = {"badLogin"})
    public void setUpChromeDriverForBadLogin() {
        WebDriverManager.chromiumdriver().setup();
        playerOne = new ChromeDriver();
        playerOne.get("http://207.154.222.51:8080");
        battleships.logIn("kamil@email.com", "test", playerOne);
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

    @AfterMethod(onlyForGroups = {"badLogin"})
    public void terminateChromeDriverForBadLogin() {
        playerOne.quit();
    }

    @Test(groups = {"onePlayer"}, description = "Player One can log in if he provides correct credentials")
    public void playerOneCanLogIn() {
        // given
        var expectedTitle = "Battleships";
        // when
        var title = playerOne.getTitle();
        // then
        assertEquals(expectedTitle, title);
    }

    @Test(groups = {"badLogin"}, description = "Player One cannot log in if he provides incorrect credentials")
    public void playerOneCannotLogIn() {
        // given
        var expectedElementText = "Bad credentials";
        // when
        var elementText = playerOne.findElement(By.className("alert")).getText();
        // then
        assertEquals(expectedElementText, elementText);
    }

    @Test(groups = {"onePlayer"}, description = "Player One can create a game")
    public void playerOneCanCreateGame() {
        // given

        // when
        var roomId = battleships.createGame(playerOne);
        // then
        assertNotNull(roomId);
    }

    @Test(groups = {"twoPlayers"}, description = "Player Two can join the game")
    public void playerTwoCanJoinGame() {
        // given
        var roomId = battleships.createGame(playerOne);
        // when
        battleships.joinGame(roomId, playerTwo);
        var joinedRoomId = playerTwo.findElement(By.id("roomId")).getText();
        // then
        assertEquals(joinedRoomId, roomId);
    }

}
