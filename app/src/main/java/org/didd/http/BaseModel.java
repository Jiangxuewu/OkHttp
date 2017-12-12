package org.didd.http;



import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */

public abstract class BaseModel {

    public String interfaceName;

    public BaseModel(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public abstract HttpEntry toHttpEntry();

//    protected String toJson(Object object) {
//        return new Gson().toJson(object);
//    }

    protected Map<String, String> toMap(Object object) {
        Map<String, String> map = new HashMap<>();
        if (null == object) {
            return map;
        }
        Class<?> cls = object.getClass();
        Field[] fields = cls.getFields();
        if (null == fields) {
            return map;
        }
        for (Field item : fields) {
            try {
                item.setAccessible(true);
                if (null != item.get(object)) {
                    if (!"serialVersionUID".equalsIgnoreCase(item.getName())
                            && !"interfaceName".equalsIgnoreCase(item.getName())) {
                        map.put(item.getName(), String.valueOf(item.get(object)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }
}
