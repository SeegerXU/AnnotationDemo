package com.seeger.router;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author Seeger
 */
public class ResourceField {
    private VariableElement mVariableElement;
    private int mResId;

    public ResourceField(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only field can be annotated with @%s", ResourceField.class.getSimpleName()));
        }
        mVariableElement = (VariableElement) element;
        ResourceTest test = mVariableElement.getAnnotation(ResourceTest.class);
        mResId = test.value();
        if (mResId == -1) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for field %s is not valid !",
                            ResourceField.class.getSimpleName(),
                            mVariableElement.getSimpleName()));
        }
    }

    Name getName() {
        return mVariableElement.getSimpleName();
    }

    public int getResId() {
        return mResId;
    }

    TypeMirror getFieldType() {
        return mVariableElement.asType();
    }
}
