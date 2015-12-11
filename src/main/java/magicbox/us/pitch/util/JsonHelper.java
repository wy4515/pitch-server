package magicbox.us.pitch.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import magicbox.us.pitch.exception.ExceptionLog;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper<T> {
    ExceptionLog logger = new ExceptionLog();

    public JsonArray toJsonArray(ArrayList<T> list) {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<T>>() {}.getType());

        JsonArray jsonArray = null;
        try {
            jsonArray = element.getAsJsonArray();
        } catch (Exception e) {
            logger.log(e.getMessage());
        }
        return jsonArray;
    }
}
