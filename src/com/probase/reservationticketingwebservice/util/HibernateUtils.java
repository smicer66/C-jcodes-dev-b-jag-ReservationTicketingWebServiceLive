package com.probase.reservationticketingwebservice.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.util.Enumeration;
import java.util.Properties;
//import com.sf.encrypt.HibernateEncryptionInterceptor;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtils
{
	private static SessionFactory sessionFactoryObject;

	private static Logger log = Logger.getLogger(HibernateUtils.class);
	
    static
    {
        try
        {
			
			try
			{
			
				/* Save the session factory that is built in this initializer*/
				//sessionFactoryObject = new Configuration().configure("SeamfixHibConfig.xml").buildSessionFactory();
				Configuration configuration=new Configuration();
				configuration.configure();
				//log.info("111");
				ServiceRegistry sr= new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
				//log.info("112");
				Enumeration e = configuration.getProperties().keys();
				//log.info("113");
				while(e.hasMoreElements())
				{
					String key = (String) e.nextElement();
					log.info(key + " = " + configuration.getProperties().getProperty(key));
				}
				//log.info("114");
				sessionFactoryObject=configuration.buildSessionFactory(sr);
			}
			catch (HibernateException ex)
			{
				throw new RuntimeException(ex);
			}
			
        }
        catch (HibernateException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public static SessionFactory getSessionFactory() throws NamingException
    {
        return sessionFactoryObject;
    }
}