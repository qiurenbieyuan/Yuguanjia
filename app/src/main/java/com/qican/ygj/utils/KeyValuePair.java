package com.qican.ygj.utils;

import java.util.Map.Entry;

public class KeyValuePair<K, V> implements Entry<K, V>
{
	private K mKey 	= null;
	private V mValue = null;
	
	public KeyValuePair(K key, V value)
	{
		mKey = key;
		mValue = value;
	}
	
	@Override
	public K getKey() 
	{
		return mKey;
	}

	@Override
	public V getValue() 
	{
		return mValue;
	}

	@Override
	public V setValue(V object) 
	{
		mValue =  object;
		return mValue;
	}

	public K setKey(K key) 
	{
		K tmp = mKey;
		mKey = key;
		return tmp;
	}
}
