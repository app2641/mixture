package com.app2641.utility;

/**
 * com.app2641.utility.VersionManager
 * 
 * アプリケーションのバージョンを管理する
 * @author app2641
 */
public class VersionManager {

	// Mixtureアプリケーションの現在のバージョン
	public static int VERSION = 1;
	
	
	/**
	 * バージョンの差分処理を実行する
	 * @param now_version
	 */
	public int applyVersions (int now_version)
	{
		while (now_version < VERSION) {
			switch (now_version) {
				case 1:
					// version2へ以降する差分処理
					break;
					
				case 2:
					// version3へ以降する差分処理
					break;
			}
			
			now_version++;
		}
		
		return VERSION;
	}
}
