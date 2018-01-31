package org.bahmni.module.bahmniucc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.GlobalPropertyListener;
import org.openmrs.api.context.Context;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Constants required by this module
 */
public class AppGlobalProperties implements GlobalPropertyListener {
	
	protected static final Log log = LogFactory.getLog(AppGlobalProperties.class);
	
	private static transient Map<String, Object> gpCache = new HashMap<String, Object>();
	
	// Global Property Names
	public static final String GLOBAL_NHIF_VERIFICATION_URL = "nhif.verification";
	
	public static final String GLOBAL_NHIF_REQUEST_TOKEN_URL = "nhif.token";
	
	public static final String GLOBAL_NHIF_CLAIM_URL = "nhif.claim_url";

	public static final String GLOBAL_NHIF_BASE_URL = "nhif.base_url";

	public static final String GLOBAL_NHIF_USERNAME= "nhif.username";

	public static final String GLOBAL_NHIF_PASSWORD= "nhif.password";

	public static final List<String> CACHED_PROPERTIES = Arrays.asList(GLOBAL_NHIF_VERIFICATION_URL,
			GLOBAL_NHIF_REQUEST_TOKEN_URL, GLOBAL_NHIF_CLAIM_URL, GLOBAL_NHIF_USERNAME, GLOBAL_NHIF_PASSWORD, GLOBAL_NHIF_BASE_URL);


	public static final String GLOBAL_NHIF_BASE_URL() {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(GLOBAL_NHIF_BASE_URL);

		return propertyValue;
	}

	public static final String GLOBAL_NHIF_VERIFICATION_URL() {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(GLOBAL_NHIF_VERIFICATION_URL);
		
		return propertyValue;
	}

	public static final String GLOBAL_NHIF_REQUEST_TOKEN_URL() {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(GLOBAL_NHIF_REQUEST_TOKEN_URL);

		return propertyValue;
	}

	public static final String GLOBAL_NHIF_CLAIM_URL() {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(GLOBAL_NHIF_CLAIM_URL);

		return propertyValue;
	}

	public static final String GLOBAL_NHIF_USERNAME() {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(GLOBAL_NHIF_USERNAME);

		return propertyValue;
	}

	public static final String GLOBAL_NHIF_PASSWORD() {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(GLOBAL_NHIF_PASSWORD);

		return propertyValue;
	}


	@Override
	public boolean supportsPropertyName(String s) {
		return CACHED_PROPERTIES.contains(s);
	}
	
	@Override
	public void globalPropertyChanged(GlobalProperty globalProperty) {
		gpCache.clear();
	}
	
	@Override
	public void globalPropertyDeleted(String s) {
		gpCache.clear();
	}
	
	private static String getPropertyValueAsString(String propertyName) {
		return Context.getAdministrationService().getGlobalProperty(propertyName);
	}
	
	private static int getPropertyValueAsInt(String propertyName, int defaultValue) {
		String propertyValue = getPropertyValueAsString(propertyName);
		if (StringUtils.hasText(propertyValue)) {
			try {
				return Integer.parseInt(propertyValue);
			}
			catch (Exception e) {
				log.warn("Invalid setting <" + propertyValue + "> found for global property: " + propertyName
				        + ".  An Integer is required.  Using default of " + defaultValue);
			}
		}
		return defaultValue;
	}
	
	private static boolean getPropertyValueAsBoolean(String propertyName, boolean defaultValue) {
		String propertyValue = getPropertyValueAsString(propertyName);
		if (StringUtils.hasText(propertyValue)) {
			try {
				return Boolean.parseBoolean(propertyValue);
			}
			catch (Exception e) {
				log.warn("Invalid setting <" + propertyValue + "> found for global property: " + propertyName
				        + ".  A Boolean is required.  Using default of " + defaultValue);
			}
		}
		return defaultValue;
	}
	
	private static Locale getPropertyValueAsLocale(String propertyName, Locale defaultValue) {
		String propertyValue = getPropertyValueAsString(propertyName);
		if (StringUtils.hasText(propertyValue)) {
			try {
				return new Locale(propertyValue);
			}
			catch (Exception e) {
				log.warn("Invalid setting <" + propertyValue + "> found for global property: " + propertyName
				        + ".  A Locale is required.  Using default of " + defaultValue);
			}
		}
		return defaultValue;
	}
	
	public static void clearGlobalPropertyCache() {
		gpCache.clear();
	}
}
