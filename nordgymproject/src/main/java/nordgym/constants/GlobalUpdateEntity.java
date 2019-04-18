package nordgym.constants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class GlobalUpdateEntity<T> {
    private static final List<String> excludedFields = new ArrayList<>(){{
        add("id");
        add("password");
        add("confirmPassword");
        add("isAdmin");
    }};
    public static <T> void updateEntity(T model, T origin) throws IllegalAccessException, NoSuchFieldException {
        for (Field f : model.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (f.get(model) != null && !excludedFields.contains(f.getName())) {
                String fieldName = f.getName();
                Field f1 = origin.getClass().getDeclaredField(fieldName);
                f1.setAccessible(true);
                f1.set(origin, f.get(model));
            }
        }
    }
}
