package org.kuali.mobility.view;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;

public class KualiMobileResourceBundleViewResolver extends ResourceBundleViewResolver {
	public static final String REDIRECT_URL_PREFIX = "redirect:";

	public static final String FORWARD_URL_PREFIX = "forward:";

	@Override
	protected View createView(String viewName, Locale locale) throws Exception {
		if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
			String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
			return new RedirectView(redirectUrl, true, true);
		}
		// Check for special "forward:" prefix.
		if (viewName.startsWith(FORWARD_URL_PREFIX)) {
			String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
			return new InternalResourceView(forwardUrl);
		}
		// Else fall back to superclass implementation: calling loadView.
		return super.createView(viewName, locale);
	}

}
