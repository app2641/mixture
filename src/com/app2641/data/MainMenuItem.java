package com.app2641.data;

public class MainMenuItem {
	
	private String mType;
	private int mText;
	private int mIcon;
	
	// メニュータイプ
	public static String SCAN = "scan";
	public static String MIXIN = "mixin";
	public static String SHOP = "shop";
	public static String COLLECTION = "collection";
	public static String STATUS = "status";
	public static String HELP = "help";
	

	public MainMenuItem(String type, int text_resource, int icon_resource)
	{
		mType = type;
		mText = text_resource;
		mIcon = icon_resource;
	}
	
	
	public String getType ()
	{
		return mType;
	}
	
	
	public int getTextResource ()
	{
		return mText;
	}
	
	
	public int getIconResource ()
	{
		return mIcon;
	}

}
