package com.github.harboat.battleshipsfrontendtests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

class TestHelper {

    void logIn(String email, String password, ChromeDriver driver) {
        driver.findElement(By.id("username")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.className("btn")).click();
    }

    String createGame(ChromeDriver driver) {
        driver.findElement(By.id("gameCreate")).click();
        var boardCreatedAlert = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.alertIsPresent());
        boardCreatedAlert.accept();
        return driver.findElement(By.id("roomId")).getText();
    }

    void joinGame(String roomId, ChromeDriver driver) {
        roomId = roomId.substring(9);
        driver.findElement(By.id("gameJoin")).click();
        var provideRoomId = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.alertIsPresent());
        provideRoomId.sendKeys(roomId);
        provideRoomId.accept();

        var boardCreatedAlert = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.alertIsPresent());
        boardCreatedAlert.accept();
    }

    void changeBoardSize(int height, int width, ChromeDriver driver) {
        var boardHeight = driver.findElement(By.id("boardHeight"));
        var selectBoardHeight = new Select(boardHeight);
        selectBoardHeight.selectByValue(String.valueOf(height));

        var boardWidth = driver.findElement(By.id("boardWidth"));
        var selectBoardWidth = new Select(boardWidth);
        selectBoardWidth.selectByValue(String.valueOf(width));

        driver.findElement(By.id("setBoardSizeButton")).click();

        var boardSizeChangedAlert = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.alertIsPresent());
        boardSizeChangedAlert.accept();
    }

    WebElement generateFleet(ChromeDriver driver) {
        driver.findElement(By.id("fleetGenButton")).click();
        return new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("player")));
    }

    void startGame(ChromeDriver driver) {
        driver.findElement(By.id("startGameButton")).click();
    }

    void playerReady(ChromeDriver driver) {
        driver.findElement(By.id("readyButton")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
}
