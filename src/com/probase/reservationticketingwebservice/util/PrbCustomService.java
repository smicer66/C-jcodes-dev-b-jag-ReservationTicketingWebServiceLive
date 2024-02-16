package com.probase.reservationticketingwebservice.util;


import org.apache.log4j.Logger;

import com.probase.reservationticketingwebservice.models.*;




public class PrbCustomService extends AbstractPrbCustomService{

  private static Logger log = Logger.getLogger(PrbCustomService.class);

  public static PrbCustomService getInstance()
  {
    return new PrbCustomService();
  }
  
}