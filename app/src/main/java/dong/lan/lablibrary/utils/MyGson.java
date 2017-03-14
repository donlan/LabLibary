package dong.lan.lablibrary.utils;

import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by 梁桂栋 on 17-2-28 ： 下午4:50.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: LabLibrary
 */

public class MyGson {

    private static MyGson myGson;
    private Gson gson;

    private MyGson() {
        gson = new GsonBuilder()
                .serializeNulls()
                .create();
    }

    public static MyGson gson() {
        if (myGson == null)
            myGson = new MyGson();
        return myGson;
    }

    public <T> T toTarget(String json, Class<T> tClass) {
        try {
            return gson.fromJson(json, tClass);
        } catch (Exception e) {
            return null;
        }
    }

    public String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            return "";
        }
    }
}
