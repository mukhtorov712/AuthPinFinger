package uz.pdp.authpinfinger

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson


class MySharedPreference(context: Context) {
    private val appSharedPrefs = context.getSharedPreferences("have", Context.MODE_PRIVATE)
    private var gson = Gson()
    private val editor = appSharedPrefs?.edit()

    fun setRegister() {
        editor?.putString("reg", "reg")
        editor?.apply()
    }

    fun getRegister(): Boolean {
        val json = appSharedPrefs?.getString("reg", "")
        return if (TextUtils.isEmpty(json)) {
            false
        } else json == "reg"
    }

    fun setBool(key: String, value: Boolean) {
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun getBool(key: String): Boolean {
        return appSharedPrefs?.getBoolean(key, false) ?: false
    }

    fun setString(key: String, value: String) {
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getString(key: String): String {
        return appSharedPrefs?.getString(key, "") ?: ""
    }

//    fun setList(list: List<Category>) {
//        val str = gson.toJson(list)
//        editor?.putString("list", str)
//        editor?.apply()
//    }
//
//    fun getList(): List<Category> {
//        val type = object : TypeToken<List<Category>>() {}.type
//        return gson.fromJson(appSharedPrefs.getString("list", ""), type)
//    }
}