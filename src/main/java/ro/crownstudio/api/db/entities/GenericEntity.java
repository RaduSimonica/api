package ro.crownstudio.api.db.entities;

import java.lang.reflect.Field;

public class GenericEntity {

    public boolean updateIfNotNull(GenericEntity other) {
        boolean isUpdated = false;

        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                if (field.getName().equals("id")) {
                    // Never update "id" field
                    continue;
                }

                field.setAccessible(true); // I like to live dangerous!

                if (field.get(other) != null) {
                    field.set(this, field.get(other));
                    isUpdated = true;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return isUpdated;
    }
}
