package com.example.demo.configuration;

import org.apache.catalina.*;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.Date;

public class TestValve extends ValveBase {

    private Session getSession(final Manager manager, String requestedSessionId) {
        if (manager == null) {
            return null;
        }
        if (requestedSessionId != null) {
            Session session;
            try {
                session = manager.findSession(requestedSessionId);
            } catch (IOException e) {
                session = null;
            }
            if ((session != null) && !session.isValid()) {
                session = null;
            }
            if (session != null) {
                return session;
            }
        }
        return null;
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        final Manager manager = request.getContext().getManager();
        final Session session = getSession(manager, request.getRequestedSessionId());

        final boolean proxied   = session instanceof WrappedSession;
        final boolean normalUrl = request.getRequestURL().toString().contains("ON");
        if (normalUrl && proxied) {
            final Session original = (Session) ((WrappedSession) session).unwrap();
            original.access();
            manager.remove(session);
            manager.add(original);
            getNext().invoke(request, response);
            return;
        }

        if (session != null && !normalUrl) {

            final ClassLoader classLoader = this.getClass().getClassLoader();
            if (!(proxied)) {
                final Session decorator = (Session) Proxy.newProxyInstance(classLoader,
                        new Class[] {Session.class, WrappedSession.class}, (proxy, method, args) -> {
                            if (method.getName().equalsIgnoreCase("access")) {
                                System.out.println("NOOP[access()]: " + new Date(System.currentTimeMillis()));
                                return null;
                            }
                            if (method.getName().equalsIgnoreCase("endAccess")) {
                                System.out.println("NOOP[endAccess()]: " + new Date(System.currentTimeMillis()));
                                return null;
                            }

                            if (method.getName().equalsIgnoreCase("unwrap")) {
                                return session;
                            }

                            return method.invoke(session, args);
                        });
                manager.remove(session);
                manager.add(decorator);
            }
        }
        getNext().invoke(request, response);
    }

    interface WrappedSession {
        Object unwrap();
    }
}
