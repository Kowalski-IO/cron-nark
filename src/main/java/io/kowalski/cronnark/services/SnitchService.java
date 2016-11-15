package io.kowalski.cronnark.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.kowalski.cronnark.models.Interval;
import io.kowalski.cronnark.models.TrackedTask;

@Singleton
public class SnitchService {

    private final SlackService slackService;
    private final TrackedTaskService taskService;

    @Inject
    public SnitchService(final SlackService slackService, final TrackedTaskService taskService) {
        this.slackService = slackService;
        this.taskService = taskService;
        init();
    }

    public void init() {
        final Runnable snitcher = new Runnable() {
            @Override
            public void run() {
                final Optional<List<TrackedTask>> tasks = taskService.loadAllTasks();
                if (tasks.isPresent()) {

                    final LocalDateTime now = LocalDateTime.now();

                    final Set<TrackedTask> awolTasks = new HashSet<>();

                    for (final TrackedTask task : tasks.get()) {
                        final LocalDateTime lastCheckIn = task.getLastCheckInLDT();
                        final long minutes = ChronoUnit.MINUTES.between(lastCheckIn, now);

                        final boolean isAWOLAfter15 = task.getInterval().equals(Interval.QUARTER_HOUR) && minutes > 15;
                        final boolean isAWOLAfter30 = task.getInterval().equals(Interval.HALF_HOUR) && minutes > 30;
                        final boolean isAWOLAfter60 = task.getInterval().equals(Interval.HOURLY) && minutes > 60;

                        final boolean isAWOL = isAWOLAfter15 || isAWOLAfter30 || isAWOLAfter60;

                        if (isAWOL) {
                            awolTasks.add(task);
                        }
                    }
                    slackService.sendAWOLMessages(awolTasks);
                }
            }
        };

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
        scheduler.scheduleWithFixedDelay(snitcher, 0, 1, TimeUnit.MINUTES);
    }

}
