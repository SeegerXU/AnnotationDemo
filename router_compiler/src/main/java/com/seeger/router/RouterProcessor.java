package com.seeger.router;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author Seeger
 */
@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {

    /**
     */
    private Filer mFileUtils;
    /**
     */
    private Elements mElementUtils;
    /**
     */
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFileUtils = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<String>();
        annotationTypes.add(ResourceTest.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Map<String, AnnotatedClass> mAnnotatedClassMap = new HashMap<String, AnnotatedClass>();

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mAnnotatedClassMap.clear();
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ResourceTest.class);
        for (Element element : elements) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "111111111111");
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, element.getEnclosingElement().getKind().toString());
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            String qualifiedName = typeElement.getQualifiedName().toString();
            AnnotatedClass annotatedClass = mAnnotatedClassMap.get(qualifiedName);
            if (annotatedClass == null) {
                annotatedClass = new AnnotatedClass(typeElement, mElementUtils);
                mAnnotatedClassMap.put(qualifiedName, annotatedClass);
            }
            annotatedClass.addField(new ResourceField(element));
        }
        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                annotatedClass.generateFile().writeTo(mFileUtils);
            } catch (IOException e) {
            }
        }
        return true;
    }
}
