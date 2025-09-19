/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.servlet;

import java.beans.PropertyEditorManager;
import java.lang.management.ManagementFactory;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.formation.model.Etablissement;
import org.formation.model.Formateur;
import org.formation.model.Inscription;
import org.formation.model.Lieu;
import org.formation.model.Question;
import org.formation.model.Reponse;
import org.formation.model.Sessions;
import org.formation.model.Stagiaire;
import org.formation.model.Statut;
import org.formation.model.TypeFormation;
import org.formation.model.TypeSession;

/**
 * Web application lifecycle listener.
 * @author jean-laurent
 */
public class FormationServletListener implements ServletContextListener {

    private ServletContext context = null;

    public ServletContext getContext() {
        return context;
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }

    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();

        //Output a simple message to the server's console
        System.out.println("The Simple Web App. Is Ready");


    }

    public void contextDestroyed(ServletContextEvent sce) {
        //Output a simple message to the server's console
        System.out.println("The Simple Web App. Has Been Removed");
        System.out.println("before deregisterAllDrivers");
        try {
            deregisterAllDrivers();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("after deregisterAllDrivers");

        //ExesudocMARCXMLDataSource = null;
        //System.out.println("Datasources nullified");

        unregisterOracleDiagnosabilityMBean();



        //System.out.println("Shutdown LogManager (log4j) !");
        //LogHelper.shutdown();
        //System.out.println("after Shutdown LogManager (log4j)");
        //System.out.println("logger nullified");

        java.util.logging.LogManager m = java.util.logging.LogManager.getLogManager();
        m.reset();
        System.out.println("java.util.logging.LogManager resetted");

       /* PropertyEditorManager.registerEditor(ClientSelection.class, null);
        System.out.println("registerEditor ClientSelection removed");*/

        PropertyEditorManager.registerEditor(Formateur.class, null);
        System.out.println("registerEditor Formateur removed");

         PropertyEditorManager.registerEditor(Etablissement.class, null);
        System.out.println("registerEditor Etablissement removed");


        PropertyEditorManager.registerEditor(Inscription.class, null);
        System.out.println("registerEditor Inscription removed");

        PropertyEditorManager.registerEditor(Lieu.class, null);
        System.out.println("registerEditor Lieu removed");

        PropertyEditorManager.registerEditor(Sessions.class, null);
        System.out.println("registerEditor Sessions removed");

        PropertyEditorManager.registerEditor(Statut.class, null);
        System.out.println("registerEditor Statut removed");

        PropertyEditorManager.registerEditor(Reponse.class, null);
        System.out.println("registerEditor Reponse removed");

        PropertyEditorManager.registerEditor(Question.class, null);
        System.out.println("registerEditor Question removed");

        PropertyEditorManager.registerEditor(Stagiaire.class, null);
        System.out.println("registerEditor Stagiaire removed");

        PropertyEditorManager.registerEditor(TypeFormation.class, null);
        System.out.println("registerEditor TypeFormation removed");

        PropertyEditorManager.registerEditor(TypeSession.class, null);
        System.out.println("registerEditor TypeSession removed");

        

        //super.destroy();

        context = null;


    }

    public static void deregisterAllDrivers() throws SQLException {

        Set<Driver> driversToUnload = new HashSet<Driver>();

        Enumeration<Driver> e = DriverManager.getDrivers();

        while (e.hasMoreElements()) {

            Driver driver = (Driver) e.nextElement();
            // the driver class loader can be null if the driver comes from the
            // JDK, such as the
            // sun.jdbc.odbc.JdbcOdbcDriver
            ClassLoader driverClassLoader = driver.getClass().getClassLoader();
            if (driverClassLoader != null
                    && Thread.currentThread().getContextClassLoader().equals(
                    driverClassLoader)) {
                driversToUnload.add(driver);
            }

        }

        for (Iterator<Driver> iterator = driversToUnload.iterator(); iterator.hasNext();) {
            Driver driver = (Driver) iterator.next();
            System.out.println("Deregister driver: " + driver.getClass().getName());
            DriverManager.deregisterDriver(driver);

        }

    }

    /**
     *
     *  !!! Workaround to minimize lost pergmgen, CORRECT WAY IS TO LOAD DRIVERS AT SERVER APP LEVEL !!!
     *  in case driver is loaded from WebAppClassLoader (driver in WEB-INF/lib) we try to unregister Oracle 11g Diagnosability MBean loaded by
     *    WebAppClassLoader to prevent PermGen on undeploy (beacause link to
     *    system class in Oracle driver)
     *
     *  !!!be sure to have the patched Oracle driver !!!
     *  see http://forums.oracle.com/forums/thread.jspa?messageID=2172498
     *
     */
    private void unregisterOracleDiagnosabilityMBean() {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        String loader = ccl.getClass().getName() + '@'
                + Integer.toHexString(ccl.hashCode());

        ObjectName pattern = null;
        try {
            pattern = new ObjectName(
                    "com.oracle.jdbc:type=diagnosability,name=" + loader);

            Set<ObjectName> set = mbs.queryNames(pattern, null);

            if (set != null) {

                if (set.size() > 0) {

                    ObjectName[] array = new ObjectName[set.size()];
                    array = (ObjectName[]) set.toArray();
                    ObjectName diag = array[0];

                    if (diag != null) {
                        mbs.unregisterMBean(diag);
                        System.out.println("after try to deregister of MBean " + pattern);
                    }

                }

            }

        } catch (MalformedObjectNameException e) {

            System.out.println(e.getMessage());

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (MBeanRegistrationException e) {
            System.out.println(e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
}
