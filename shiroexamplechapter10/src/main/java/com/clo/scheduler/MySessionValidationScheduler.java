package com.clo.scheduler;

import com.clo.utils.JdbcTemplateUtils;
import com.clo.utils.SerializableUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class MySessionValidationScheduler implements SessionValidationScheduler, Runnable {
    private static final Logger log = LoggerFactory.getLogger(ExecutorServiceSessionValidationScheduler.class);
    ValidatingSessionManager sessionManager;
    private ScheduledExecutorService service;
    private long interval = 3600000L;
    private boolean enabled = false;

    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();

    public MySessionValidationScheduler() {}
    public MySessionValidationScheduler(ValidatingSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public ValidatingSessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(ValidatingSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void run() {
        if (log.isDebugEnabled()) {
            log.debug("Executing session validation...");
        }
        System.err.println("Executing session validation...");
        long startTime = System.currentTimeMillis();

        String sql = "select session from sessions limit ?,?";
        int start = 0;
        int size = 20;
        List<String> sessions = jdbcTemplate.queryForList(sql, String.class, start, size);
        while(sessions.size() > 0) {
            for(String sessionStr : sessions) {
                try {
                    Session session = SerializableUtils.deserialize(sessionStr);
                    Method validateMethod = ReflectionUtils.findMethod(AbstractValidatingSessionManager.class, "validate", Session.class, SessionKey.class);
                    validateMethod.setAccessible(true);
                    ReflectionUtils.invokeMethod(validateMethod, AbstractValidatingSessionManager.class, session, new DefaultSessionKey(session.getId()));
                } catch (Exception e) {

                }
            }

            start += size;
            sessions = jdbcTemplate.queryForList(sql, String.class, start, size);
        }

        long stopTime = System.currentTimeMillis();
        System.err.println("Session validation completed successfully in " + (stopTime - startTime) + " milliseconds.");
        if (log.isDebugEnabled()) {
            log.debug("Session validation completed successfully in " + (stopTime - startTime) + " milliseconds.");
        }
    }

    @Override
    public void enableSessionValidation() {
        if (this.interval > 0L) {
            this.service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    return thread;
                }
            });
            this.service.scheduleAtFixedRate(this, this.interval, this.interval, TimeUnit.MILLISECONDS);
            this.enabled = true;
        }
    }

    @Override
    public void disableSessionValidation() {
        this.service.shutdownNow();
        this.enabled = false;
    }
}
