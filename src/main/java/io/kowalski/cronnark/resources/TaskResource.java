package io.kowalski.cronnark.resources;

import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.kowalski.cronnark.models.TrackedTask;

@Path("/task")
public class TaskResource {

    @Inject
    public TaskResource() {

    }

    @GET
    public Set<TrackedTask> fetchAllTrackedTasks() {

    }

}
