public interface Constants {
    //  CONSTANTS
    public static final int TILESIZE = 32;    //  num pixels in a tile
    public static final int SCALE = 2;    //  tile scale
    public static final int SCALEDTILESIZE = TILESIZE * SCALE;  //  working tile size with scale
    public static final int MAXCOLUMNS = 16;  //  num tiles along width
    public static final int MAXROWS = 12; //  num tiles along height
    public static final int SCREENWIDTH = MAXCOLUMNS * SCALEDTILESIZE;  //  pixel width
    public static final int SCREENHEIGHT = MAXROWS * SCALEDTILESIZE;    //  pixel height
    public static final String CARD_MAIN_GAME = "MainGame";
    public static final String CARD_PAUSE_PANEL = "PausePanel";
    public static final int FPS = 60;
}
