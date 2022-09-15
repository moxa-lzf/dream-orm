package com.moxa.dream.template.session;

import com.moxa.dream.system.config.Configuration;
import com.moxa.dream.system.core.session.Session;
import com.moxa.dream.system.core.session.SessionFactory;
import com.moxa.dream.system.mapped.MethodInfo;
import com.moxa.dream.system.mapper.factory.MapperFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SessionTemplate implements Session {
    private final Session sessionProxy;
    private Configuration configuration;
    private MapperFactory mapperFactory;
    private SessionFactory sessionFactory;

    private SessionHolder sessionHolder;

    public SessionTemplate(SessionHolder sessionHolder, SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.configuration = sessionFactory.getConfiguration();
        this.mapperFactory = configuration.getMapperFactory();
        this.sessionHolder = sessionHolder;
        this.sessionProxy = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class}, new SessionInterceptor());
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return mapperFactory.getMapper(type, (methodInfo, arg) ->
                sessionProxy.execute(methodInfo, arg));
    }

    @Override
    public Object execute(MethodInfo methodInfo, Object o) {
        return sessionProxy.execute(methodInfo, o);
    }

    @Override
    public boolean isAutoCommit() {
        return false;
    }

    @Override
    public void commit() {
    }

    @Override
    public void rollback() {
    }

    @Override
    public void close() {
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    private class SessionInterceptor implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Session session = sessionHolder.getSession(SessionTemplate.this.sessionFactory);
            try {
                Object result = method.invoke(session, args);
                if (!sessionHolder.isSessionTransactional(session)) {
                    session.commit();
                }
                return result;
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } finally {
                if (session != null) {
                    sessionHolder.closeSession(session);
                }
            }
        }
    }
}