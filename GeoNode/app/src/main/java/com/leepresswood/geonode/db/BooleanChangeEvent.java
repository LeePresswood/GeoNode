package com.leepresswood.geonode.db;

import java.util.EventObject;

/**
 * This class lets the listener know when the change occured and what
 * object was changed.
 */
class BooleanChangeEvent extends EventObject
{

	private final BooleanChangeDispatcher dispatcher;

	public BooleanChangeEvent(BooleanChangeDispatcher dispatcher)
	{
		super(dispatcher);
		this.dispatcher = dispatcher;
	}

	//Type safe way to get source (as opposed to getSource of EventObject
	public BooleanChangeDispatcher getDispatcher() {
		return dispatcher;
	}
}