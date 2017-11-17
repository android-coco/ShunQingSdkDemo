package yh.org.shunqinglib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import yh.org.shunqinglib.app.ShunQingApp;


/**
 * 
*    
* 项目名称：healthplus   
* 类名称：PreferenceHelper   
* 类描述：   SharedPreferences操作工具包
* <b>说明</b> 本工具包只能在单进程项目下使用，多进程共享请使用如下demo的两行代码重写: <br>
 * Context otherContext = c.createPackageContext( "com.android.contacts",
 * Context.CONTEXT_IGNORE_SECURITY); <br>
 * SharedPreferences sp = otherContext.getSharedPreferences( "my_file",
 * Context.MODE_MULTI_PROCESS);<br>
* 创建人：hao   
* 创建时间：2015-5-18 上午11:07:16   
* 修改人：hao   
* 修改时间：2015-5-18 上午11:07:16   
* 修改备注：   
* @version 1.0
*
 */
public class PreferenceHelper
{
	
	public static void write(String fileName, String k, int v)
	{
		SharedPreferences preference  = ShunQingApp.getInstance().getApplicationContext().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putInt(k, v);
		editor.commit();
	}

	public static void write(String fileName, String k,
							 boolean v)
	{
		SharedPreferences preference = ShunQingApp.getInstance().getApplicationContext().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putBoolean(k, v);
		editor.commit();
	}

	public static void write(String fileName, String k,
							 String v)
	{
		SharedPreferences preference = ShunQingApp.getInstance().getApplicationContext().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putString(k, v);
		editor.commit();
	}

	public static int readInt(String fileName, String k)
	{
		SharedPreferences preference = ShunQingApp.getInstance().getApplicationContext().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getInt(k, 0);
	}

	public static int readInt(String fileName, String k,
							  int defv)
	{
		SharedPreferences preference = ShunQingApp.getInstance().getApplicationContext().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getInt(k, defv);
	}

	public static boolean readBoolean(String fileName, String k)
	{
		SharedPreferences preference = ShunQingApp.getInstance().getApplicationContext().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getBoolean(k, false);
	}

	public static boolean readBoolean(String fileName,
									  String k, boolean defBool)
	{
		SharedPreferences preference = ShunQingApp.getInstance().getApplicationContext().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getBoolean(k, defBool);
	}

	public static String readString(String fileName, String k)
	{
		SharedPreferences preference = ShunQingApp.getInstance().getApplicationContext().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getString(k, "");
	}

	public static String readString(String fileName, String k,
									String defV)
	{
		SharedPreferences preference = ShunQingApp.getInstance().getApplicationContext().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getString(k, defV);
	}

	public static void remove(String fileName, String k)
	{
		SharedPreferences preference = ShunQingApp.getInstance().getApplicationContext().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.remove(k);
		editor.commit();
	}

	public static void clean(String fileName)
	{
		SharedPreferences preference = ShunQingApp.getInstance().getApplicationContext().getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.clear();
		editor.commit();
	}
}
