package com.mumu.coinegg.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mumu.coinegg.bean.TradeListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin on 2018/1/14.
 */

public class JsonUtil {
    /**
     * json解析成class
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T Gson2Class(String json, Class<T> clazz) {
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().disableHtmlEscaping().create();
            return gson.fromJson(json, clazz);
        } catch (Throwable t) {
            return null;
        }
    }

    public static List<TradeListResult.Result> getListResultByGson(String jsonString) {
        Log.e("linwl","string   " + jsonString);

        List<TradeListResult.Result> list = new ArrayList<TradeListResult.Result>();
        Gson gson = new Gson();
        list = gson.fromJson(jsonString, new TypeToken<List<TradeListResult.Result>>() {
        }.getType());
        return list;
    }

}
