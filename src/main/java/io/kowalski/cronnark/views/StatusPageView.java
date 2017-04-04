package io.kowalski.cronnark.views;

import java.io.Serializable;
import java.util.Collection;

import io.dropwizard.views.View;
import io.kowalski.cronnark.models.TrackedTask;
import lombok.Getter;

@Getter
public class StatusPageView extends View implements Serializable {

    private static final long serialVersionUID = 1081208252133052923L;

    private final Collection<TrackedTask> tasks;

    public StatusPageView(final Collection<TrackedTask> tasks) {
        super("status_page.ftl");
        this.tasks = tasks;
    }

}
