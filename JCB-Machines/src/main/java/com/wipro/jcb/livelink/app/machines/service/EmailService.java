package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.entity.ServiceCallRequest;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/21/2024
 */
public interface EmailService {
    void sendFeedbackEmail(String feedbackMsg, String userName) throws ProcessCustomError, IOException;
    public void sendServiceCallRequestEmail(ServiceCallRequest requestParam, String concernvalues, List<InputStream> imageFile, List<String> fileName, String userName) throws ProcessCustomError;

}
