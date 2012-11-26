package de.engineapp;

public interface Constants
{
    // software-version
    public final static int MAJOR = 0;
    public final static int MINOR = 1;
    public final static int REVISION = 1;
    public final static String FILE_VERSION = "1.2";
    
    // properties, states (storage)
    public final static String STG_GRID = "grid";
    public final static String STG_MAXIMIZED = "maximized";
    public final static String STG_RUN_PHYSICS = "runPhysics";
    public final static String STG_DEBUG = "debug";
    public final static String STG_SHOW_ARROWS_ALWAYS = "showArrowsAlways";
    public final static String STG_DBLCLICK_SHOW_PROPERTIES = "dblClickshowProperties";
    
    public final static String PRP_CURRENT_FILE = "currentFile";
    public final static String PRP_LOOK_AND_FEEL = "lookAndFeel";
    public final static String PRP_LANGUAGE_CODE = "langCode";
    public final static String PRP_OBJECT_MODE = "ObjectMode";
    public final static String PRP_MODE = "mode";
    public final static String PRP_CURRENT_PLAYBACK_FRAME = "playbackFrame";
    public final static String PRP_SKIN = "skin";
    
    // events
    public final static String EVT_SCENE_LOADED = "sceneLoaded";
    public final static String EVT_ANIMATION_LOADED = "animationLoaded";
    
    // localization
    public final static String L_APP_NAME = "APP_NAME";
    public final static String L_NAME_OF_OBJECT = "NAME_OF_OBJECT";
    public final static String L_MATERIAL = "MATERIAL";
    public final static String L_X_COORDINATE = "X_COORDINATE";
    public final static String L_Y_COORDINATE = "Y_COORDINATE";
    public final static String L_X_VELOCITY = "X_VELOCITY";
    public final static String L_Y_VELOCITY = "Y_VELOCITY";
    public final static String L_MASS = "MASS";
    public final static String L_RADIUS = "RADIUS";
    public final static String L_POT_ENERGY = "POT_ENERGY";
    public final static String L_KIN_ENERGY = "KIN_ENERGY";
    public final static String L_REMOVE = "REMOVE";
    public final static String L_CLOSE = "CLOSE";
    public final static String L_PINNED = "PINNED";
    public final static String L_SETTINGS = "SETTINGS";
    public final static String L_LANGUAGE = "LANGUAGE";
    public final static String L_OK = "OK";
    public final static String L_CANCEL = "CANCEL";
    public final static String L_YES = "YES";
    public final static String L_NO = "NO";
    public final static String L_OPEN = "OPEN";
    public final static String L_SAVE = "SAVE";
    public final static String L_TITLE_OPEN = "TITLE_OPEN";
    public final static String L_TITLE_SAVE = "TITLE_SAVE";
    public final static String L_SHOW_PROPERTIES = "SHOW_PROPERTIES";
    public final static String L_OBJECT_SELECTED = "OBJECT_SELECTED";
    public final static String L_DBLCLICK_OBJECT = "DBLCLICK_OBJECT";
    public final static String L_LOOKANDFEEL = "LOOKANDFEEL";
    public final static String L_ALUMINIUM = "ALUMINIUM";
    public final static String L_RUBBER = "RUBBER";
    public final static String L_STEEL = "STEEL";
    public final static String L_WATER = "WATER";
    public final static String L_NACL = "NACL";
    public final static String L_PHYSICS_MODE = "PHYSICS_MODE";
    public final static String L_RECORDING_MODE = "RECORDING_MODE";
    public final static String L_PLAYBACK_MODE = "PLAYBACK_MODE";
    public final static String L_WRONG_VERSION = "WRONG_VERSION";
    public final static String L_TITLE_IMPORT = "TITLE_IMPORT";
    public final static String L_TITLE_EXPORT = "TITLE_EXPORT";
    public final static String L_OBJECTS = "OBJECTS";
    public final static String L_ABOUT = "ABOUT";
    public final static String L_HELP = "HELP";
    public final static String L_ABOUT_TEXT = "ABOUT_TEXT";
    public final static String L_OVERWRITE_FILE = "OVERWRITE_FILE";
    
    //tooltips
    public final static String TT_NEW = "TT_NEW";
    public final static String TT_OPEN = "TT_OPEN";
    public final static String TT_SAVE = "TT_SAVE";
    public final static String TT_PLAY = "TT_PLAY";
    public final static String TT_PAUSE = "TT_PAUSE";
    public final static String TT_RESET = "TT_RESET";
    public final static String TT_GRID = "TT_GRID";
    public final static String TT_SHOW_ARROWS = "TT_SHOW_ARROWS";
    public final static String TT_FOCUS = "TT_FOCUS";
    public final static String TT_ZOOM = "TT_ZOOM";
    public final static String TT_SETTINGS = "TT_SETTINGS";
    public final static String TT_CIRCLE = "TT_CIRCLE";
    public final static String TT_SQUARE = "TT_SQUARE";
    public final static String TT_GROUND = "TT_GROUND";
    public final static String TT_DISCARD = "TT_DISCARD";
    public final static String TT_HELP = "TT_HELP";
    public final static String TT_ABOUT = "TT_ABOUT";
    
    // commands
    // TODO - add all string constants for action commands
    public final static String CMD_NEW = "new";
    public final static String CMD_OPEN = "open";
    public final static String CMD_SAVE = "save";
    public final static String CMD_PLAY = "play";
    public final static String CMD_PAUSE = "pause";
    public final static String CMD_RESET = "reset";
    public final static String CMD_GRID = "grid";
    public final static String CMD_SHOW_ARROWS = "showArrows";
    public final static String CMD_FOCUS = "focus";
    public final static String CMD_NEXT_MODE = "nextMode";
    public final static String CMD_PHYSICS_MODE = "physicsMode";
    public final static String CMD_RECORDING_MODE = "recordingMode";
    public final static String CMD_PLAYBACK_MODE = "playBackMode";
    public final static String CMD_SETTINGS = "settings";
    public final static String CMD_DISCARD = "discard";
    public final static String CMD_DELETE = "delete";
    public final static String CMD_CLOSE = "close";
    public final static String CMD_NEXT = "next";
    public final static String CMD_PREVIOUS = "previous";
    public final static String CMD_OK = "ok";
    public final static String CMD_CANCEL = "cancel";
    public final static String CMD_HELP = "help";
    public final static String CMD_ABOUT = "about";
    public final static String CMD_COPY = "copy";
    public final static String CMD_PASTE = "paste";
    public final static String CMD_SELECT_ALL = "selectAll";
    
    // decor ID's
    public final static String DECOR_SELECTION = "selection";
    public final static String DECOR_SELECTION_RECT = "selectionRect";
    public final static String DECOR_AABB = "aabb";
    public final static String DECOR_ARROW = "arrow";
    public final static String DECOR_RANGE = "range";
    public final static String DECOR_COORDINATE = "coordinate";
    public final static String DECOR_CLOSEST_POINT = "closestPoint";
    public final static String DECOR_MULTIPLE_ARROW = "multipleArrow";
    public final static String DECOR_ANGLE_VIEWER = "angleViewer";
    
    // objects
    public final static String OBJ_CIRCLE = "circle";
    public final static String OBJ_SQUARE = "square";
    public final static String OBJ_GROUND = "ground";
    
    // icons
    public final static String ICO_CIRCLE = "circle";
    public final static String ICO_DISCARD = "discard";
    public final static String ICO_FOCUS = "focus";
    public final static String ICO_GRID = "grid";
    public final static String ICO_GROUND = "ground";
    public final static String ICO_LOUPE = "loupe";
    public final static String ICO_NEW = "new";
    public final static String ICO_NEXT = "next";
    public final static String ICO_OBJECT_ARROWS = "object_arrows";
    public final static String ICO_OPEN = "open";
    public final static String ICO_PAUSE = "pause";
    public final static String ICO_PHYSICS = "physics";
    public final static String ICO_PLAY = "play";
    public final static String ICO_PLAYBACK = "playback";
    public final static String ICO_PREVIOUS = "previous";
    public final static String ICO_RECORD = "record";
    public final static String ICO_RESET = "reset";
    public final static String ICO_RULER = "ruler";
    public final static String ICO_SAVE = "save";
    public final static String ICO_SETTINGS = "settings";
    public final static String ICO_SQUARE = "square";
    public final static String ICO_HELP = "help";
    public final static String ICO_ABOUT = "about";
    public final static String ICO_BLANK = "blank";
    
    public final static String ICO_MAIN_16 = "main/16";
    public final static String ICO_MAIN_24 = "main/24";
    public final static String ICO_MAIN_32 = "main/32";
    public final static String ICO_MAIN_48 = "main/48";
    public final static String ICO_MAIN_64 = "main/64";
    public final static String ICO_MAIN_128 = "main/128";
    public final static String ICO_MAIN_256 = "main/256";
}