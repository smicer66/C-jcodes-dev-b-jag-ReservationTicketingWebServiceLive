// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: HibernateSessionEJBLocator.vsl in andromda-hibernate-cartridge.
//
package com.probase.reservationticketingwebservice.util;

public class ServiceLocator
{
    /**
     * The shared instance of this ServiceLocator.
     */
    private static ServiceLocator instance;

    private ServiceLocator()
    {
        // shouldn't be instantiated
    }

    /**
     * Gets the shared instance of this Class
     *
     * @return the shared service locator instance.
     */
    public static final ServiceLocator getInstance()
    {
        if (instance == null)
        {
            instance = new ServiceLocator();
        }
        return instance;
    }
	
    //private static SwpServiceHome swpServiceHome = null;

    /**
     * Gets an instance of <code>smartpay.service.SwpService</code>
     * @throws Exception 
     */
    public final SwpService getSwpService() throws Exception
    {
        try
        {
        	com.probase.reservationticketingwebservice.util.SwpService service = new SwpServiceBeanImpl();
			return service;
        }
        catch (Exception ex)
        {
            throw new Exception(ex);
        }
    }

}