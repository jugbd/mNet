package org.jugbd.mnet.web.editor;

import java.beans.PropertyEditorSupport;

/**
 * @author Bazlur Rahman Rokon
 * @since 5/15/16.
 */
public class CaseInsensitivePropertyEditor<T extends Enum<T>> extends PropertyEditorSupport {
    private final Class<T> typeParameterClass;

    public CaseInsensitivePropertyEditor(Class<T> typeParameterClass) {
        super();
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        String upper = text.toUpperCase(); // or something more robust
        T value = Enum.valueOf(typeParameterClass, upper);
        setValue(value);
    }
}
