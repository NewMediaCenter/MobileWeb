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

package org.kuali.mobility.askiu.service;

import org.kuali.mobility.askiu.entity.AskIU;
import org.kuali.mobility.askiu.service.AskIUService;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AskIUServiceImpl implements AskIUService {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ConfigParamService configParamService;
	
	private static final String PARAM_ASKIU_EMAIL_VALIDATION_REGEX = "AskIU.Email.Validation.Regex";
	private static final String PARAM_ASKIU_SEND_EMAILADDRESS = "AskIU.Send.EmailAddress";
	
	public boolean postQuery(AskIU ask){
		String sendAddress = this.getAskIUEmail();
		if (sendAddress != null && this.isValidEmail(sendAddress)) {
			String sendSubject = "Mobile AskIU: ";
			if (ask.getSubject() != null && ask.getSubject().trim().length() > 0) {
				sendSubject += ask.getSubject();
			} else {
				sendSubject += "<no subject>";
			}
			emailService.sendEmail(ask.getMessage(), sendSubject, sendAddress, ask.getEmail());
			return true;
		} else {
			return false;
		}
	}
	
	private String getAskIUEmail() {
		String email = configParamService.findValueByName(PARAM_ASKIU_SEND_EMAILADDRESS);
		return email;
	}
	
	public boolean isValidEmail(String email) {
		String regex = configParamService.findValueByName(PARAM_ASKIU_EMAIL_VALIDATION_REGEX);
		// default: allow anything in email field
		boolean isValid = true;
		if (regex != null && regex.length() > 0) {
			isValid = email.matches(regex);
		}
		return isValid;
	}
}
