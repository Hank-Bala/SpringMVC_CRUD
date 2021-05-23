package _00_init.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//@WebListener
public class CrudServletContextListener implements ServletContextListener {
   
    public void contextDestroyed(ServletContextEvent sce)  { 
    }

    public void contextInitialized(ServletContextEvent sce)  { 
    	System.out.println("----------------- 由程式來啟動ServletContextListener ----------------- ");
    }
	
}
