package net.leejjon.blufpoker;

public class Settings {
	private boolean allowSwitchingPositions = true;
	private boolean allowBok = true;
	private boolean allowSharedBok = false;
	
	public boolean isAllowSwitchingPositions() {
		return allowSwitchingPositions;
	}
	public void setAllowSwitchingPositions(boolean allowSwitchingPositions) {
		this.allowSwitchingPositions = allowSwitchingPositions;
	}
	public boolean isAllowBok() {
		return allowBok;
	}
	public void setAllowBok(boolean allowBok) {
		this.allowBok = allowBok;
	}
	public boolean isAllowSharedBok() {
		return allowSharedBok;
	}
	public void setAllowSharedBok(boolean allowSharedBok) {
		this.allowSharedBok = allowSharedBok;
	}
}
