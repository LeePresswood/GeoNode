package com.leepresswood.geonode.db;

interface BooleanChangeDispatcher
{
	public void addBooleanChangeListener(BooleanChangeListener listener);
	public boolean getFlag();
	public void setFlag(boolean flag);
}