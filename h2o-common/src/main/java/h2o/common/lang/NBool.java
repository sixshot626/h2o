package h2o.common.lang;

public enum NBool implements OptionalValue<Boolean> {

    FALSE, TRUE, NULL;


    @Override
    public boolean isPresent() {
        return this != NULL;
    }

    @Override
    public Boolean getValue() {
        return this == NULL ? null : Boolean.valueOf(this == TRUE);
    }

    public static NBool of(boolean bool) {
        return bool ? TRUE : FALSE;
    }

    public static NBool of(Boolean bool) {
        if (bool == null) {
            return NULL;
        } else {
            return bool.booleanValue() ? TRUE : FALSE;
        }
    }

}
