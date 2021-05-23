package _00_init.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	public static final String DB_MYSQL = "M";
	public static final String DB_SQLSERVER = "S";
	public static final String DB_TYPE = DB_SQLSERVER;
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] {RootAppConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] {WebAppJavaConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}

	@Override
	protected Filter[] getServletFilters() {
		HiddenHttpMethodFilter hhmf = new HiddenHttpMethodFilter();
		CharacterEncodingFilter  cef = new CharacterEncodingFilter();
		cef.setEncoding("UTF-8");
		return new Filter[] {cef, hhmf};
		
//		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//		characterEncodingFilter.setEncoding("UTF-8");
//		return new Filter[] {characterEncodingFilter, new HiddenHttpMethodFilter()};
	}
	@Override
    public void onStartup(ServletContext container) throws ServletException
    {
		// 在Web Applicaltion啟動之初，請 Spring MVC框架幫我們做些事
		super.onStartup(container);
		container.setAttribute("dbType", DB_TYPE);
		
		Resource resource1 = null;
		if (DB_TYPE.equals(DB_MYSQL)) {
		   resource1 = new ClassPathResource("/db_MySQL.properties");
		} else if (DB_TYPE.equals(DB_SQLSERVER)) {
		   resource1 = new ClassPathResource("/db_SQLServer.properties");	
		}
		Resource resource2 = new ClassPathResource("/db.properties");
		File outFile = null;
		try(
			InputStream is = resource1.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));	
		) {
			outFile = resource2.getFile();
			try (
			    OutputStream os =  new FileOutputStream(outFile);
				PrintWriter out = new PrintWriter(new OutputStreamWriter(os));	
			) {
				String line = null;
				
				while ((line = br.readLine())!= null) {
					out.println(line);
					out.flush();
					System.out.println(line);
				}
				System.out.println("資料輸出完畢...");
			}
			} catch(Exception e) {
			e.printStackTrace();
		}
		// 由程式來啟動ServletContextListener
        container.addListener(CrudServletContextListener.class);
    }
	
}
