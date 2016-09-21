package com.iread.util;


import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JsonUtil {

	public static MorphDynaBean jsToJava(String json) {
		try {
			JSONObject obj = JSONObject.fromObject(json);
			return (MorphDynaBean) JSONObject.toBean(obj);
		} catch (Exception e) {
			return null;
		}
	}
	
    @SuppressWarnings("unchecked")
    public static List<MorphDynaBean> jsToJavaList(String json) {
        try {
            JSONArray jsonArray = JSONArray.fromObject(json);
            return  (List<MorphDynaBean>) JSONArray.toCollection(jsonArray);
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
    
	public static Object getObjectValue(Object bean, String prop) {
		try {
			return PropertyUtils.getProperty(bean, prop);
		} catch (Exception e) {
			return null;
		}
	}

	public static int getIntValue(Object bean, String prop, int defaultValue) {
		Object obj = getObjectValue(bean, prop);
		if (obj == null) {
			return defaultValue;
		} else {
			try {
				return Integer.parseInt(obj.toString());
			} catch (Exception e) {
				return defaultValue;
			}
		}
	}

	public static boolean getBoolValue(Object bean, String prop,
			boolean defaultValue) {
		Object obj = getObjectValue(bean, prop);
		if (obj == null) {
			return defaultValue;
		} else {
			try {
				return (Boolean) obj;
			} catch (Exception e) {
				return defaultValue;
			}
		}
	}

	public static double getDoubleValue(Object bean, String prop,
			double defaultValue) {
		Object obj = getObjectValue(bean, prop);
		if (obj == null) {
			return defaultValue;
		} else {
			try {
				return Double.parseDouble(obj.toString());
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
	}

	public static long getLongValue(Object bean, String prop, long defaultValue) {
		Object obj = getObjectValue(bean, prop);
		if (obj == null) {
			return defaultValue;
		} else {
			try {
				return Long.parseLong(obj.toString());
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
	}

	public static String getStringValue(Object bean, String prop,
			String defaultValue) {
		Object obj = getObjectValue(bean, prop);
		if (obj == null) {
			return defaultValue;
		} else {
			try {
				return String.valueOf(obj);
			} catch (ClassCastException e) {
				return defaultValue;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getListValue(Object bean, String prop) {
		Object obj = getObjectValue(bean, prop);
		if (obj == null) {
			return Collections.emptyList();
		} else {
			try {
				return (List<T>) obj;
			} catch (ClassCastException e) {
				return Collections.emptyList();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T, V> Map<T, V> getMapValue(Object bean, String prop) {
		return toMap(getObjectValue(bean, prop));
	}

	public static Map toMap(Object bean) {
		if (bean == null) {
			return Collections.emptyMap();
		}
		try {
			return PropertyUtils.describe(bean);
		} catch (Exception e) {
			return Collections.emptyMap();
		}
	}
}
