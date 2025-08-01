package com.github.sisong;

public class HPatch {
    static { System.loadLibrary("hpatchz"); }

    public native int patch(String oldFileName, String diffFileName, String outNewFileName, long cacheMemory);

    public int patch(String oldFileName, String diffFileName, String outNewFileName) {
        return patch(oldFileName, diffFileName, outNewFileName, -1);
    }
}
