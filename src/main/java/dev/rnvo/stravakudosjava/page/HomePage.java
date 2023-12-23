package dev.rnvo.stravakudosjava.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ReloadOptions;
import com.microsoft.playwright.options.WaitUntilState;
import dev.rnvo.stravakudosjava.config.StravaProperties;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class HomePage {
    private StravaProperties properties;

    private String profileId;
    private boolean isLoggedIn;
    private Page page;
    private final String loginBtn = "button[data-cy='login_btn']";
    private final String userProfile = ".user-menu > a";
    private final String webFeedEntry = "[data-testid=web-feed-entry]";
    private final String unfilledKudoBtn = "[data-testid=unfilled_kudos]";
    private final String promoEntry = "[data-testid=promo-img]";
    private final String groupEntry = "[data-testid=group-header]";
    private final String stravaGroupEntry = "[data-testid=group-entry]";


    public HomePage(StravaProperties stravaProperties, Page webpage) {
        page = webpage;
        properties = stravaProperties;
    }


    public void refreshPage() {
        log.info("Refreshing the page");
        page.reload(new ReloadOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    }

    public void kudosNewFeeds() {
        validateHomePage();
        getProfileId();
        scrollDown(2);

        var nfActivities = page.locator(webFeedEntry);
        AtomicInteger givenKudos = new AtomicInteger();
        log.info("Kudos to {} activities in new feeds", nfActivities.count());
        for (var activity : nfActivities.all()) {
            log.info("Giving kudo to activity {}", activity);
            if (isInvalidActivity(activity) || isMyActivity(activity)) {
                log.info("Invalid activity {}", activity);
                continue;
            }
            getUnfilledKudoBtns(activity).ifPresent(kudoBtn -> {
                if (clickKudoBtn(kudoBtn)) {
                    givenKudos.getAndIncrement();
                }

            });
        }
        log.info("Kudos given {}", givenKudos);
    }

    private void getProfileId() {
        try {
            if (StringUtils.isEmpty(profileId)) {
                log.info("Getting profileId");
                profileId = page.locator(userProfile).getAttribute("href").split("/athletes/")[1];
            }
        } catch (Exception e) {
            log.error("Get profileId failed", e);
        }
    }

    public void validateHomePage() {
        if (!isLoggedIn) {
            login();
        }
    }

    private void login() {
        page.locator(loginBtn).waitFor(new Locator.WaitForOptions().setTimeout(1000));
        if (page.locator(loginBtn).isVisible()) {
            page.click(loginBtn);
            var loginPage = new LoginPage(page);
            if (!loginPage.login(properties.getEmail(), properties.getPassword())) {
                throw new IllegalStateException("Cannot logged in Strava");
            }
            isLoggedIn = true;
            page = loginPage.getPage();
            page.waitForURL("https://www.strava.com/dashboard");
        }
    }

    private boolean clickKudoBtn(Locator kudoBtn) {
        if (kudoBtn.count() == 1) {
            kudoBtn.click(new Locator.ClickOptions().setClickCount(1).setTimeout(100));
            return true;
        }
        return false;
    }

    private Optional<Locator> getUnfilledKudoBtns(Locator activity) {
        try {
            return Optional.of(activity.locator(unfilledKudoBtn));
        } catch (Exception e) {
            log.error("Error when getting unfilled kudo btn of activity {} {}", activity, e);
            return Optional.empty();
        }
    }

    private boolean isMyActivity(Locator activity) {
        try {
            var athleteId = activity.getByTestId("owners-name").getAttribute("href").split("/athletes/")[1];
            return profileId.equals(athleteId);
        } catch (Exception e) {
            log.error("Error when getting athlete id of activity {}", activity);
            return true;
        }
    }

    private boolean isInvalidActivity(Locator activity) {
        return activity.locator(groupEntry).count() > 0
                || activity.locator(stravaGroupEntry).count() > 0
                || activity.locator(promoEntry).count() > 0;
    }

    private void scrollDown(int times) {
        log.info("Scrolling down {} time(s)", times);
        try {
            for (int i = 0; i < times; i++) {
                page.keyboard().press("PageDown");
                Thread.sleep(100);
            }
        } catch (Exception e) {
            log.error("Error when pressing PageDown");
        }
    }

}
