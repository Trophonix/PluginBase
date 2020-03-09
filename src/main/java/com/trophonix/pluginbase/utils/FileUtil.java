package com.trophonix.pluginbase.utils;

import java.io.File;

public class FileUtil {

  public static File file(File folder, String path) {
    int idx = path.indexOf("/");
    if (idx > 0) return file(new File(folder, path.substring(0, idx)), path.substring(idx+1));
    return new File(folder, path);
  }

}
