package GameControl;

public class GlobalSettings {
    private static GlobalSettings instance = null;

    private GlobalSettings() {
    }

    public synchronized static GlobalSettings getInstance() {
        if (instance == null) {
            instance = new GlobalSettings();
        }
        return instance;
    }

    private boolean showHitboxes = false;

    public void setShowHitboxes(boolean isActive) {
        showHitboxes = isActive;
    }
    
    public boolean isShowHitboxes() {
        return showHitboxes;
    }
}