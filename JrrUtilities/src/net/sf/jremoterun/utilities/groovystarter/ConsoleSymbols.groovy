package net.sf.jremoterun.utilities.groovystarter


enum ConsoleSymbols implements ShortcutInfo {

    question('h'),
    directoryBaseSelector('d'),
    userBaseSelector('u'),
    hostBaseSelector('l'),
    ;

    String s;

    ConsoleSymbols(String s) {
        this.s = ShortcutPrefix.shortcutPrefix + s
    }

    public static List<ConsoleSymbols> all = ConsoleSymbols.values().toList()
}