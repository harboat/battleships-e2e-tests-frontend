package com.github.harboat.battleshipsfrontendtests;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

class TestHelper {

    void logIn(String email, String password, ChromeDriver driver) {
        driver.findElement(By.id("username")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.className("btn")).click();
    }

    void createGame(ChromeDriver driver) {
        driver.findElement(By.id("gameCreate")).click();
        var boardCreatedAlert = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.alertIsPresent());
        boardCreatedAlert.accept();
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
}
