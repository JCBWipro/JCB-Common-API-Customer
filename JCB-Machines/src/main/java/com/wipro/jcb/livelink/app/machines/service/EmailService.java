package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;

import java.io.IOException;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/21/2024
 */
public interface EmailService {
    void sendFeedbackEmail(String feedbackMsg, String userName) throws ProcessCustomError, IOException;
}
