package com.corejsf;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;

public class SystemPropertyResolver extends ELResolver {

	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (base == null && "sysprop".equals(property)) {
			context.setPropertyResolved(true);
			return new PartialResolution();
		}
		
		if (base instanceof PartialResolution && property instanceof String) {
			((PartialResolution)base).add((String)property);
			Object r = System.getProperty(base.toString());
			context.setPropertyResolved(true);
			if (r == null) {
				return base;
			} else {
				return r;
			}
		}
		
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		if (base instanceof PartialResolution) {
			context.setPropertyResolved(true);
			return Object.class;
		}
		
		return null;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		if (base instanceof PartialResolution) {
			context.setPropertyResolved(true);
			return true;
		}
		
		return false;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
			Object base) {
		return null;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		if (base instanceof PartialResolution) {
			return String.class;
		}
		
		return null;
	}
	
	public static class PartialResolution extends ArrayList<String> {
		private static final long serialVersionUID = 1L;

		public String toString() {
			StringBuilder builder = new StringBuilder();
			for(String s : this) {
				if (builder.length() > 0) {
					builder.append(".");
				}
				builder.append(s);
			}
			
			return builder.toString();
		}
	}

}
