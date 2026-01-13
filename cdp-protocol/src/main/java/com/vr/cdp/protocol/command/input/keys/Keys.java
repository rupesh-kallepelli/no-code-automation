package com.vr.cdp.protocol.command.input.keys;


public final class Keys {

    private Keys() {
    }

    public static final Key ENTER =
            new Key("Enter", "Enter", 13);

    public static final Key TAB =
            new Key("Tab", "Tab", 9);

    public static final Key ESCAPE =
            new Key("Escape", "Escape", 27);

    public static final Key BACKSPACE =
            new Key("Backspace", "Backspace", 8);

    public static final Key DELETE =
            new Key("Delete", "Delete", 46);

    public static final Key SPACE =
            new Key(" ", "Space", 32);

    public static final Key ARROW_UP =
            new Key("ArrowUp", "ArrowUp", 38);

    public static final Key ARROW_DOWN =
            new Key("ArrowDown", "ArrowDown", 40);

    public static final Key ARROW_LEFT =
            new Key("ArrowLeft", "ArrowLeft", 37);

    public static final Key ARROW_RIGHT =
            new Key("ArrowRight", "ArrowRight", 39);

    /* ============================
       Modifier Keys
       ============================ */

    public static final Key CONTROL =
            new Key("Control", "ControlLeft", 17);

    public static final Key SHIFT =
            new Key("Shift", "ShiftLeft", 16);

    public static final Key ALT =
            new Key("Alt", "AltLeft", 18);

    public static final Key META =
            new Key("Meta", "MetaLeft", 91);
}
