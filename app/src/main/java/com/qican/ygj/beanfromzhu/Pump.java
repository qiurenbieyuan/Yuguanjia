package com.qican.ygj.beanfromzhu;

public class Pump {
	/**
	 * @return the pumpLocation
	 */
	public String getPumpLocation() {
		return pumpLocation;
	}

	/**
	 * @param pumpLocation
	 *            the pumpLocation to set
	 */
	public void setPumpLocation(String pumpLocation) {
		this.pumpLocation = pumpLocation;
	}

	/**
	 * @return the pumpId
	 */
	public String getPumpId() {
		return pumpId;
	}

	/**
	 * @param pumpId
	 *            the pumpId to set
	 */
	public void setPumpId(String pumpId) {
		this.pumpId = pumpId;
	}

	/**
	 * @return the cameraId
	 */
	public String getCameraId() {
		return cameraId;
	}

	/**
	 * @param cameraId
	 *            the cameraId to set
	 */
	public void setCameraId(String cameraId) {
		this.cameraId = cameraId;
	}

	/**
	 * @return the pumpName
	 */
	public String getPumpName() {
		return pumpName;
	}

	/**
	 * @param pumpName
	 *            the pumpName to set
	 */
	public void setPumpName(String pumpName) {
		this.pumpName = pumpName;
	}

	/**
	 * @return the runningState
	 */
	public String getRunningState() {
		return runningState;
	}

	/**
	 * @param runningState
	 *            the runningState to set
	 */
	public void setRunningState(String runningState) {
		this.runningState = runningState;
	}

	private String pumpId;
	private String cameraId;
	private String pumpName;
	private String runningState;
	private String pumpLocation;
}
