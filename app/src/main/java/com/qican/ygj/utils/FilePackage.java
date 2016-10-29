package com.qican.ygj.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class FilePackage {
	
	private static FilePackage sFilePackage = null;
	
	private Map<String, Integer> mFiles = new HashMap<String, Integer>();
	
	private FilePackage() {
		
	}

	public static FilePackage instance() {
		if (sFilePackage == null)
			sFilePackage = new FilePackage();
		return sFilePackage;
	}
	
	
	public void cleanup() {
		Collection<Integer> list = mFiles.values();
		for (Integer handle : list) {
			PackageUtils.closePackage(handle);
		}
		mFiles.clear();
	}
	
	public void close(String packagePath) {
		Integer handle = mFiles.get(packagePath);
		if (handle == null || handle == 0)
			return;
		
		PackageUtils.closePackage(handle);
		mFiles.remove(packagePath);
	}
	
	public StringBuffer readFileFromPck(String packagePath, String fileName) {
		Integer handle = getHandle(packagePath);
		if(null==handle || handle == 0)
			return new StringBuffer();
		return PackageUtils.readFile(handle, fileName);
	}
	
	public byte [] readBufferFromPck(String packagePath, String fileName) {
		Integer handle = getHandle(packagePath);
		if(null==handle || handle == 0)
			return new byte[0];
		return PackageUtils.readBuffer(handle, fileName);
	}
	
	public PackageUtils.FilePosition getFilePosition(String packagePath, String fileName) {
		Integer handle = getHandle(packagePath);
		if(null==handle || handle == 0) 
			return null;
		
		return PackageUtils.getFilePosition(handle, fileName);
	}
	
	public boolean writeFileFromPck(String packagePath, String src, String dest) {
		Integer handle = getHandle(packagePath);
		if(null==handle || handle == 0) 
			return false;
		return PackageUtils.writeFile(handle, src, dest);
	}
	
	private Integer getHandle(String packagePath) {
		Integer handle = mFiles.get(packagePath);
		if (handle == null || handle == 0)
		{
			handle = PackageUtils.openPackage(packagePath);
			if (handle == null || handle == 0) return null;
			
			mFiles.put(packagePath, handle);
		}
		return handle;
	}
}
