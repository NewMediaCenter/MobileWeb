/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.mobility.feedback.service;

import java.util.StringTokenizer;

import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.email.service.EmailService;
import org.kuali.mobility.feedback.dao.FeedbackDao;
import org.kuali.mobility.feedback.entity.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	
	@Autowired
    private FeedbackDao feedbackDao;
	
	@Autowired
	private ConfigParamService configParamService;
	
	@Autowired
	private EmailService emailService;
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FeedbackServiceImpl.class);

    @Override
    @Transactional
	public void saveFeedback(Feedback feedback) {
		feedbackDao.saveFeedback(feedback);
		sendEmail(feedback);
	}
    
    private void sendEmail(Feedback f) {
    	try {
            String sendEmailException = configParamService.findValueByName("Feedback.SendEmail.On");
            if ("true".equals(sendEmailException)) {
            	String emailAddress = configParamService.findValueByName("Feedback.SendEmail.EmailAddress");
                StringTokenizer stringTokenizer = new StringTokenizer(emailAddress);
                while (stringTokenizer.hasMoreTokens()) {
                    String email = stringTokenizer.nextToken();
                    emailService.sendEmail(f.toString(), "MIU Feedback", email, configParamService.findValueByName("Core.EmailAddress"));
                }        	
            }    		
    	} catch (Exception e) {
    		LOG.error("Error sending feedback email " + f.getFeedbackId(), e);
    	}
    }
    
    public FeedbackDao getFeedbackDao() {
        return feedbackDao;
    }

    public void setFeedbackDao(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }
    
    public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
}


