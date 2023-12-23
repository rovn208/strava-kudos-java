package dev.rnvo.stravakudosjava.page;

import com.microsoft.playwright.Page;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginPage {
    @Getter
    private final Page page;
    private String emailInput = "#email";
    private String passwordInput = "#password";
    private String submitBtn = "button[type='submit']";
    private String userNav = ".user-nav";


    public boolean login(String email, String password) {
        page.fill(emailInput, email);
        page.fill(passwordInput, password);
        page.click(submitBtn);
        page.locator(userNav).waitFor();
        if (page.locator(userNav).isVisible()) {
            System.out.println("user is logged in successfully....");
            return true;
        }
        System.out.println("user is not logged in successfully....");
        return false;
    }
}
