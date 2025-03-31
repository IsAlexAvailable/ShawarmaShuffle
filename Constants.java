public interface Constants {
    //  CONSTANTS
    public static final int TILESIZE = 32;    //  num pixels in a tile
    public static final int SCALE = 2;    //  tile scale
    public static final int TRUETILESIZE = TILESIZE * SCALE;  //  working tile size with scale
    public static final int MAXCOLUMNS = 16;  //  num tiles along width
    public static final int MAXROWS = 12; //  num tiles along height
    public static final int SCREENWIDTH = MAXCOLUMNS * TRUETILESIZE;  //  pixel width
    public static final int SCREENHEIGHT = MAXROWS * TRUETILESIZE;    //  pixel height
}
