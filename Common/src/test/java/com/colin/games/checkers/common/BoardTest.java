package com.colin.games.checkers.common;

import java.io.IOException;
import java.nio.file.Path;

public class BoardTest {
    public static void main(String[] args) throws IOException {
        Environment.setConfig(Config.read(Path.of("config/Checkers.cfg")));
        System.out.println(new Board().boardDump());
        System.out.println(new Board().gridDump());
    }
}
