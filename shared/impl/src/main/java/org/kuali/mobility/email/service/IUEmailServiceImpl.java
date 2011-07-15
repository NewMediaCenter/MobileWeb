package org.kuali.mobility.email.service;

import edu.iu.uis.sit.util.mail.AuthenticatedMailer;

public class IUEmailServiceImpl implements EmailService {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(IUEmailServiceImpl.class);
	
	private String username;
	private String password;

	@Override
	public boolean sendEmail(String body, String subject, String emailAddressTo, String emailAddressFrom) {
        try {
            AuthenticatedMailer mailer = new AuthenticatedMailer(emailAddressFrom, username, password);
            mailer.sendMessage(emailAddressTo, subject, body);
        } catch (Exception e) {
            LOG.error("Error in sendEmail message", e);
            return false;
        }
        return true;
    }
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
