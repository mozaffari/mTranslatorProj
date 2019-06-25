package dev.mozaffari.mtranslator.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

public class SettingsManager {
    private static SharedPreferences pref;

    public static void setTranslateFromIndex(Context context,int fromIndex)
    {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString("last_selected_from_index", String.valueOf(fromIndex)).commit();
    }
    public static  int getTranslateFromIndex(Context context){
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        int fromIndex = Integer.parseInt(pref.getString("last_selected_from_index", "0"));
        return fromIndex;
    }
    public static void setTranslateToIndex(Context context,int toIndex)
    {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString("last_selected_to_index", String.valueOf(toIndex)).commit();
    }
    public static  int getTranslateToIndex(Context context){
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        int toIndex = Integer.parseInt(pref.getString("last_selected_to_index", "1"));
        return toIndex;
    }




    public static Boolean isUserFollowedCategories(Context context)
    {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = pref.getString("UserFollowedCategories", "null");
        return (!lang.equals("null") || lang.equals("false"));
    }

    public static void setUserFollowedCategories(Context context, Boolean bol)
    {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString("UserFollowedCategories", String.valueOf(bol)).commit();

    }

    ///


    public static void setFollowedSources(Context context,String vals){

        pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString("followed_sources", vals).commit();
    }
    public static JSONArray getFollowedSources(Context context) throws JSONException {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        String string = pref.getString("followed_sources", "null");
        JSONArray jsonArray = new JSONArray();
        if(!string.equals("null"))
            jsonArray= new JSONArray(string);
        return jsonArray;
    }



}
