package dev.rnvo.stravakudosjava.service;

import dev.rnvo.stravakudosjava.config.StravaProperties;
import dev.rnvo.stravakudosjava.factory.StravaFactory;
import dev.rnvo.stravakudosjava.page.HomePage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@EnableScheduling
@Service
public class StravaService {
    private final HomePage homePage;

    public StravaService(StravaProperties stravaProperties, StravaFactory factory) {
        homePage = new HomePage(stravaProperties, factory.getPage());
    }

    @Scheduled(fixedDelayString = "${strava.fix-delay}")
    public void scheduler() {
        homePage.refreshPage();
        homePage.kudosNewFeeds();
    }
}
