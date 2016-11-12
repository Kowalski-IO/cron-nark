package io.kowalski.cronnark.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.zaxxer.hikari.HikariDataSource;

import io.kowalski.cronnark.models.TrackedTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrackedService {

    private static final String ALL_TASKS = "SELECT id, name, interval, slack FROM task";
    private static final String TASK_FOR_ID = "SELECT id, name, interval, slack FROM task WHERE id = ?";
    private static final String INSERT_TASK = "INSERT INTO task (id, name, interval, slack) VALUES (?, ?, ?, ?)";
    private static final String DELETE_TASK = "DELETE FROM task WHERE id = ?";

    private final HikariDataSource hikari;

    @Inject
    public TrackedService(final HikariDataSource hikari) {
        this.hikari = hikari;
    }

    public Optional<List<TrackedTask>> loadAllTasks() {
        List<TrackedTask> tasks = null;
        try {
            final QueryRunner runner = new QueryRunner(hikari);
            final ResultSetHandler<List<TrackedTask>> handler = new BeanListHandler<TrackedTask>(TrackedTask.class);
            tasks = runner.query(ALL_TASKS, handler);
        } catch (final SQLException e) {
            log.error("Unable to load all tasks", e );
        }
        return Optional.ofNullable(tasks);
    }

    public Optional<TrackedTask> loadTaskById(final UUID id) {
        TrackedTask task = null;
        try {
            final QueryRunner runner = new QueryRunner(hikari);
            final ResultSetHandler<TrackedTask> handler = new BeanHandler<TrackedTask>(TrackedTask.class);
            task = runner.query(TASK_FOR_ID, handler, id);
        } catch (final SQLException e) {
            log.error("Unable to load all tasks", e );
        }
        return Optional.ofNullable(task);
    }

    public void saveTask(final TrackedTask task) {
        final QueryRunner runner = new QueryRunner(hikari);
        task.setId(UUID.randomUUID());
        try {
            runner.update(INSERT_TASK, task.getId(), task.getName(), task.getInterval(), task.getEndpoint());
        } catch (final SQLException e) {
            log.error("Unable to insert new Task.", e);
        }
    }

    public void deleteTask(final TrackedTask task) {
        final QueryRunner runner = new QueryRunner(hikari);
        try {
            runner.update(DELETE_TASK, task.getId());
        } catch (final SQLException e) {
            log.error("Unable to delete task for id ".concat(task.getId().toString()), e);
        }
    }

}
