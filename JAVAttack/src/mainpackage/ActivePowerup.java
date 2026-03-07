package mainpackage;

public class ActivePowerup {
    PowerupType aType;
    private long startTime;
    private boolean isActive;
    ActivePowerup(PowerupType aType, long startTime, boolean isActive){
        this.aType = aType;
        this.startTime = startTime;
        this.isActive = isActive;
    }
    public void setStartTime(long startTime){
        this.startTime = startTime;
    }
    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }

    public long getStartTime() { return startTime;}
    public boolean getIsActive() { return isActive;}
    
}
