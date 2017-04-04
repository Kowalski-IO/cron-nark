package io.kowalski.cronnark.resources;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.kowalski.cronnark.models.TrackedTask;
import io.kowalski.cronnark.services.TrackedTaskService;
import io.kowalski.cronnark.views.StatusPageView;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class StatusPageResource {

    private final TrackedTaskService trackedTaskService;

    @Inject
    public StatusPageResource(final TrackedTaskService trackedTaskService) {
        this.trackedTaskService = trackedTaskService;
    }

    @GET
    public StatusPageView buildPage() {
        return new StatusPageView(trackedTaskService.loadAllTasks().orElse(new ArrayList<TrackedTask>()));
    }

}
