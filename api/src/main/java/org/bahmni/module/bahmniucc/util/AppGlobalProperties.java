package org.bahmni.module.bahmniucc.util;

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
	public static final String GLOBAL_CONSULTATION_PRODUCT_ID = "consultation.productId";
	
	public static final String GLOBAL_CONSULTATION_AMOUNT = "consultation.amount";

	public static final String OPENERP_HOST = "openerp.host";

	public static final String OPENERP_PORT = "openerp.port";

	public static final String CONSULTATION_EXEMPTION_NO_DAYS= "cons.exemption.days";
	
	public static final List<String> CACHED_PROPERTIES = Arrays.asList(GLOBAL_CONSULTATION_PRODUCT_ID,
			GLOBAL_CONSULTATION_AMOUNT, OPENERP_HOST, OPENERP_PORT);
	
	public static final String GLOBAL_CONSULTATION_PRODUCT_ID() {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(GLOBAL_CONSULTATION_PRODUCT_ID);
		
		return propertyValue;
	}
	
	public static final String GLOBAL_CONSULTATION_AMOUNT() {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(GLOBAL_CONSULTATION_AMOUNT);
		
		return propertyValue;
	}


	public static final String OPENERP_HOST() {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(OPENERP_HOST);

		return propertyValue;
	}

	public static final String CONSULTATION_EXEMPTION_NO_DAYS() {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(CONSULTATION_EXEMPTION_NO_DAYS);

		return propertyValue;
	}

	public static final String OPENERP_PORT() {
		String propertyValue = Context.getAdministrationService().getGlobalProperty(OPENERP_PORT);

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
