package com.cryptofiat.aml.jobs;

import com.cryptofiat.aml.sanctions.SanctionsUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class SanctionsUpdateJob {
    @Autowired
    SanctionsUpdater sanctionsUpdater;

    // runs every day at 6am
    @Scheduled(cron = "0 0 6 * * *")
    public void updateSanctions() {
        sanctionsUpdater.updateAllLists();
    }
}
