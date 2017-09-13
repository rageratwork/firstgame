package com.fourthwoods.firstgame;

/**
 * Copyright (c) 2013 David J. Rager
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class Settings {
	public static final String pref_shapes_settings = "pref_shapes_settings";
	
	public static int screen_height = 480;
	
	public static double scale_factor = 1.0;
	public static double gravity_factor;
	
	public static int difficulty; // 1, 2, 3
	
	public static boolean shapes_basic;
	public static boolean shapes_intermediate;
	public static boolean shapes_advanced;
	public static boolean numbers_basic;
	public static boolean numbers_intermediate;
	public static boolean letters_basic;
	public static boolean letters_intermediate;
	public static boolean letters_advanced;

	public static boolean voice_shapes;
	public static boolean voice_numbers;
	public static boolean voice_letters;
	public static boolean voice_colors;
	public static boolean voice_laughter;

	public static void reload(Context context)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		Resources r = context.getResources();
		
		String str = sharedPref.getString(r.getString(R.string.pref_difficulty_key), "1");
		difficulty = Integer.parseInt(str);
		
		str = sharedPref.getString(r.getString(R.string.pref_gravity_factor_key), "1.0");
		gravity_factor = Double.parseDouble(str);
		
		shapes_basic = sharedPref.getBoolean(r.getString(R.string.pref_shapes_basic_key), true);
		shapes_intermediate = sharedPref.getBoolean(r.getString(R.string.pref_shapes_intermediate_key), false);
		shapes_advanced = sharedPref.getBoolean(r.getString(R.string.pref_shapes_advanced_key), false);
		numbers_basic = sharedPref.getBoolean(r.getString(R.string.pref_numbers_basic_key), false);
		numbers_intermediate = sharedPref.getBoolean(r.getString(R.string.pref_numbers_intermediate_key), false);
		letters_basic = sharedPref.getBoolean(r.getString(R.string.pref_letters_basic_key), false);
		letters_intermediate = sharedPref.getBoolean(r.getString(R.string.pref_letters_intermediate_key), false);
		letters_advanced = sharedPref.getBoolean(r.getString(R.string.pref_letters_advanced_key), false);

		if((shapes_basic || shapes_intermediate || shapes_advanced || numbers_basic || numbers_intermediate || letters_basic || letters_intermediate || letters_advanced) == false)
			shapes_basic = true;
		
		voice_shapes = sharedPref.getBoolean(r.getString(R.string.pref_voice_shapes_key), true);
		voice_numbers = sharedPref.getBoolean(r.getString(R.string.pref_voice_numbers_key), true);
		voice_letters = sharedPref.getBoolean(r.getString(R.string.pref_voice_letters_key), true);
		voice_colors = sharedPref.getBoolean(r.getString(R.string.pref_voice_colors_key), true);
		voice_laughter = sharedPref.getBoolean(r.getString(R.string.pref_voice_laughter_key), true);
	}
	
	public static void save(Context context)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		Resources r = context.getResources();
		
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(r.getString(R.string.pref_gravity_factor_key), Double.toString(Settings.gravity_factor));
		editor.commit();
		
	}
}
