package com.moxa.dream.flex;

import com.moxa.dream.system.annotation.Column;
import com.moxa.dream.system.annotation.Table;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DreamFlexProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Filer filer;

    public static String buildTableDef(String table, String tableDefPackage, String tableDefClassName, List<ColumnInfo> columnInfos) {
        StringBuilder content = new StringBuilder("package ");
        content.append(tableDefPackage).append(";\n\n");
        content.append("import com.moxa.dream.flex.def.ColumnDef;\n");
        content.append("import com.moxa.dream.flex.def.TableDef;\n\n");
        content.append("public class ").append(tableDefClassName).append(" extends TableDef {\n\n");
        content.append("    public static final ")
                .append(tableDefClassName)
                .append(' ')
                .append(table)
                .append(" = new ")
                .append(tableDefClassName)
                .append("();\n\n");
        columnInfos.forEach((columnInfo) -> {
            content.append("    public final ColumnDef ")
                    .append(columnInfo.column)
                    .append(" = new ColumnDef(")
                    .append("this,")
                    .append("\"").append(columnInfo.column).append("\",")
                    .append("\"").append(columnInfo.alias).append("\");\n");
        });
        content.append("    public ").append(tableDefClassName).append("() {\n")
                .append("        super").append("(\"").append(table).append("\");\n")
                .append("    }\n\n}\n");
        return content.toString();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.typeUtils = processingEnv.getTypeUtils();
        this.filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Table.class);
            for (Element element : elements) {
                String entityClass = element.toString();
                String className = getClassName(entityClass);
                String tableDefPackage = buildTableDefPackage(entityClass);
                String tableDefClassName = className.concat("TableDef");
                Table table = element.getAnnotation(Table.class);
                List<ColumnInfo> columnInfoList = columnInfoList((TypeElement) element);
                String content = buildTableDef(table.value(), tableDefPackage, tableDefClassName, columnInfoList);
                try {
                    processGenClass(tableDefPackage, tableDefClassName, content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotationTypes = new HashSet<>();
        supportedAnnotationTypes.add(Table.class.getCanonicalName());
        return supportedAnnotationTypes;
    }

    private String getClassName(String str) {
        return str.substring(str.lastIndexOf(".") + 1);
    }

    public List<ColumnInfo> columnInfoList(TypeElement classElement) {
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        for (Element fieldElement : classElement.getEnclosedElements()) {
            if (ElementKind.FIELD == fieldElement.getKind()) {
                Column column = fieldElement.getAnnotation(Column.class);
                if (column != null) {
                    ColumnInfo columnInfo = new ColumnInfo(column.value(), fieldElement.toString());
                    columnInfoList.add(columnInfo);
                }
            }
        }
        classElement = (TypeElement) typeUtils.asElement(classElement.getSuperclass());
        if (classElement != null) {
            columnInfoList.addAll(columnInfoList(classElement));
        }
        return columnInfoList;
    }

    private String buildTableDefPackage(String entityClass) {
        StringBuilder packageBuilder = new StringBuilder();
        if (!entityClass.contains(".")) {
            packageBuilder.append("table");
        } else {
            packageBuilder.append(entityClass, 0, entityClass.lastIndexOf(".")).append(".table");
        }
        return packageBuilder.toString();
    }

    private void processGenClass(String tableDefPackage, String tableDefClassName, String content) throws IOException {
        Writer writer = null;
        try {
            JavaFileObject sourceFile = filer.createSourceFile(tableDefPackage + "." + tableDefClassName);
            writer = sourceFile.openWriter();
            writer.write(content);
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


    class ColumnInfo {
        private String column;
        private String alias;

        public ColumnInfo(String column, String alias) {
            this.column = column;
            this.alias = alias;
        }
    }
}
