package com.seeger.router;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author Seeger
 */

public class AnnotatedClass {

    private static class TypeUtil {
        static final ClassName BINDER = ClassName.get("com.seeger.router.api", "ResourceBinder");
        static final ClassName PROVIDER = ClassName.get("com.seeger.router.api", "ResourceFinder");
    }

    private TypeElement mTypeElement;
    private ArrayList<ResourceField> mFields;
    private Elements mElements;

    AnnotatedClass(TypeElement typeElement, Elements elements) {
        mTypeElement = typeElement;
        mElements = elements;
        mFields = new ArrayList<>();
    }

    void addField(ResourceField field) {
        mFields.add(field);
    }

    JavaFile generateFile() {
        MethodSpec.Builder resourceMethod = MethodSpec.methodBuilder("bindResource")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "host")
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(TypeUtil.PROVIDER, "finder");
        for (ResourceField mField : mFields) {
            resourceMethod.addStatement("host.$N = ($T)(finder.findResource(source, $L))",
                    mField.getName(),
                    ClassName.get(mField.getFieldType()),
                    mField.getResId());
        }
        MethodSpec.Builder unResourceMethod = MethodSpec.methodBuilder("unBindResource")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(mTypeElement.asType()), "host")
                .addAnnotation(Override.class);
        for (ResourceField mField : mFields) {
            unResourceMethod.addStatement("host.$N = null", mField.getName());
        }

        //generaClass
        TypeSpec injectClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$$ResourceBinder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.BINDER, TypeName.get(mTypeElement.asType())))
                .addMethod(resourceMethod.build())
                .addMethod(unResourceMethod.build())
                .build();

        String packageName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, injectClass).build();
    }
}
