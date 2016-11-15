package io.kowalski.cronnark.services;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
public class TrackedTaskService {

    private static final String ALL_TASKS = "SELECT id, name, interval, webhook, last_check_in AS lastCheckIn FROM task";
    private static final String TASK_FOR_ID = "SELECT id, name, interval, webhook, last_check_in FROM task WHERE id = ?";
    private static final String INSERT_TASK = "INSERT INTO task (id, name, interval, webhook, last_check_in) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_TASK = "DELETE FROM task WHERE id = ?";

    private static final String TASK_CHECK_IN = "UPDATE task SET last_checkz_in = ? WHERE id = ?";

    private final HikariDataSource hikari;

    @Inject
    public TrackedTaskService(final HikariDataSource hikari) {
        this.hikari = hikari;
    }

    public Optional<List<TrackedTask>> loadAllTasks() {
        List<TrackedTask> tasks = null;
        try {
            final QueryRunner runner = new QueryRunner(hikari);
            final ResultSetHandler<List<TrackedTask>> handler = new BeanListHandler<>(TrackedTask.class);
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
            final ResultSetHandler<TrackedTask> handler = new BeanHandler<>(TrackedTask.class);
            task = runner.query(TASK_FOR_ID, handler, id);
        } catch (final SQLException e) {
            log.error("Unable to load all tasks", e );
        }
        return Optional.ofNullable(task);
    }

    public Optional<UUID> saveTask(final TrackedTask task) {
        UUID uuid = null;
        try {
            final QueryRunner runner = new QueryRunner(hikari);
            task.setId(UUID.randomUUID());
            task.setLastCheckIn(Timestamp.valueOf(LocalDateTime.now()));

            runner.update(INSERT_TASK, task.getId(), task.getName(), task.getInterval().name(), task.getWebhook(), task.getLastCheckIn());
            uuid = task.getId();
        } catch (final SQLException e) {
            log.error("Unable to insert new Task.", e);
        }
        return Optional.ofNullable(uuid);
    }

    public void deleteTask(final UUID id) {
        try {
            final QueryRunner runner = new QueryRunner(hikari);
            runner.update(DELETE_TASK, id);
        } catch (final SQLException e) {
            log.error("Unable to delete task for id ".concat(id.toString()), e);
        }
    }

    public void checkIn(final UUID id) {
        try {
            final QueryRunner runner = new QueryRunner(hikari);
            final Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            runner.update(TASK_CHECK_IN, now, id);
        } catch (final SQLException e) {
            log.error("Unable to check in task with id ".concat(id.toString()), e);
        }
    }

}
