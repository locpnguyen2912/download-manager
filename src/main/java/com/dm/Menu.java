package com.dm;

/**
 * Menu items that can be called by the user.
 *
 * <li>{@link #EXIT}</li>
 * <li>{@link #GET}</li>
 * <li>{@link #LIST}</li>
 * <li>{@link #HELP}</li>
 */
public enum Menu {

    /**
     * List all files.
     */
    LIST    ("list", "View all files to download."),

    /**
     * Get file.
     */
    GET("get <file_name> ", "Select files to download. Ex: get <file-image.png> <file-excel.xlsx>"),

    /**
     * Show the help.
     */
    HELP    ("help", "Show menu."),

    /**
     * Exit the program.
     */
    EXIT    ("exit", "Exit the program."),
    ;


    private final String menuItemName;
    private final String menuItemDesc;

    /**
     * <p>Menu enum constructor</p>
     * @param menuItemName name of the menu item.
     * @param menuItemDesc description of the menu item.
     */
    Menu(String menuItemName, String menuItemDesc) {
        this.menuItemName = menuItemName;
        this.menuItemDesc = menuItemDesc;
    }

    /**
     * <p>Get the name of the menu item.</p>
     * @return String of the menu item name.
     */
    public String getMenuItemName() {
        return this.menuItemName;
    }

    /**
     * <p>Get the description of the menu item.</p>
     * @return String of the menu item description.
     */
    public String getMenuItemDesc() {
        return this.menuItemDesc;
    }
}
