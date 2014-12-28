package utils;

import play.Play;

public class ConfigManager {

	/**
	 * 設定ファイルから値を取得する。
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
    	return Play.application()
    			.configuration()
    			.getString(key);
	}

	/**
	 * 設定ファイルから値を取得する。
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key) {		
    	return Play.application()
    			.configuration()
    			.getBoolean(key);
	}

}
