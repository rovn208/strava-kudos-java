package dev.rnvo.stravakudosjava.factory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import dev.rnvo.stravakudosjava.config.StravaProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class StravaFactory {
    public static final String BASE_URL = "https://www.strava.com/";
    private BrowserContext browserContext;
    private Playwright playwright;
    private Page page;
    private Browser browser;

    public StravaFactory(StravaProperties config) {
        this.initBrowser(config);
    }

    public void initBrowser(StravaProperties config) {
        var browser = config.getBrowser();
        this.setPlaywright(Playwright.create());

        switch (browser.toLowerCase()) {
            case "chromium":
                setBrowser(getPlaywright().chromium().launch(new LaunchOptions().setHeadless(config.isHeadless())));
                break;
            case "chrome":
                setBrowser(getPlaywright().chromium().launch(new LaunchOptions().setChannel("chrome").setHeadless(config.isHeadless())));
                break;
            case "edge":
                setBrowser(getPlaywright().chromium().launch(new LaunchOptions().setChannel("msedge").setHeadless(config.isHeadless())));
                break;
            case "firefox":
                setBrowser(getPlaywright().firefox().launch(new LaunchOptions().setHeadless(config.isHeadless())));
                break;
            case "safari":
                setBrowser(getPlaywright().webkit().launch(new LaunchOptions().setHeadless(config.isHeadless())));
                break;
            default:
                System.out.println("please choose the right browser name......");
                break;
        }

        setBrowserContext(getBrowser().newContext());
        setPage(getBrowserContext().newPage());
        getPage().navigate(BASE_URL);
    }
}
