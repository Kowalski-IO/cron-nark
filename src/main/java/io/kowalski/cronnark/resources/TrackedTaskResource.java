package io.kowalski.cronnark.resources;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.kowalski.cronnark.filter.JWT;
import io.kowalski.cronnark.models.TrackedTask;
import io.kowalski.cronnark.services.TrackedTaskService;

@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
public class TrackedTaskResource {

    private final TrackedTaskService taskService;

    @Inject
    public TrackedTaskResource(final TrackedTaskService taskService) {
        this.taskService = taskService;
    }

    @GET
    public Optional<List<TrackedTask>> allTasks() {
        return taskService.loadAllTasks();
    }

    @GET
    @Path("/{taskId}")
    public void checkIn(final UUID id) {
        taskService.checkIn(id);
    }

    @JWT
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Optional<UUID> createTask(final TrackedTask task) {
        return taskService.saveTask(task);
    }

    @JWT
    @DELETE
    @Path("/{taskId}")
    public void deleteTask(@PathParam("taskId") final UUID id) {
        taskService.deleteTask(id);
    }

}
