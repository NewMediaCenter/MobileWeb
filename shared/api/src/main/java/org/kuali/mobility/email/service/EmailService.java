package org.kuali.mobility.email.service;

public interface EmailService {
    
    boolean sendEmail(String body, String subject, String emailAddressTo, String emailAddressFrom);
    
}
