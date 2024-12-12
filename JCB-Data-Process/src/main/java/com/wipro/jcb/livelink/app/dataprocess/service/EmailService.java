package com.wipro.jcb.livelink.app.dataprocess.service;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
public interface EmailService {

    void sendFeedParserStatusMail(String type,String date,Integer count) ;

}
