package com.leepresswood.geonode.db;

import java.util.EventListener;

/**
 * Listener interface for classes interested in knowing about a boolean
 * flag change.
 */
public interface ChangeListener extends EventListener
{

	public void stateChanged();

}