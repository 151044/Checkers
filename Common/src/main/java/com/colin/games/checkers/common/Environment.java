package com.colin.games.checkers.common;

public class Environment {
    private Environment(){
        throw new AssertionError("Creating instance of Environment!");
    }
    private static Config conf = null;
    private static boolean singlePlayer = false;
    private static Side side = null;
    public static Config getConfig(){
        return conf;
    }
    public static void setConfig(Config config){
        conf = config;
    }
    public static boolean isOffline(){
        return singlePlayer;
    }
    public static Side getSide(){
        return side;
    }
    public static void setSide(Side toSet){
        side = toSet;
    }
    public enum Side{
        CLIENT,SERVER
    }
}
