package com.moxa.dream.flex;

import com.moxa.dream.system.annotation.Column;
import com.moxa.dream.system.annotation.Ignore;
import com.moxa.dream.system.annotation.Table;
import com.moxa.dream.system.annotation.View;

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
import java.util.*;
import java.util.function.Function;

public class DreamFlexProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.typeUtils = processingEnv.getTypeUtils();
        this.filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            Set<? extends Element> tableElements = roundEnv.getElementsAnnotatedWith(Table.class);
            Map<String, Map<String, Set<String>>> tableFieldMap = tableFieldMap(roundEnv);
            for (Element tableElement : tableElements) {
                String entityClass = tableElement.toString();
                String className = getClassName(entityClass);
                String tableDefPackage = buildTableDefPackage(entityClass);
                String tableDefClassName = className.concat("TableDef");
                Table table = tableElement.getAnnotation(Table.class);
                String tableName = table.value();
                Map<String, Set<String>> fieldMap = tableFieldMap.get(entityClass);
                Map<String, List<String>> columnMap = new HashMap<>();
                List<String> columnList = columnInfoList((TypeElement) tableElement, fieldElement -> {
                    Column column = fieldElement.getAnnotation(Column.class);
                    if (column != null) {
                        String columnName = column.value();
                        if (fieldMap != null && !fieldMap.isEmpty()) {
                            String fieldName = fieldElement.toString();
                            fieldMap.forEach((k, v) -> {
                                if (v.contains(fieldName)) {
                                    List<String> columns = columnMap.get(k);
                                    if (columns == null) {
                                        columns = new ArrayList<>();
                                        columnMap.put(k, columns);
                                    }
                                    columns.add(columnName);
                                }
                            });
                        }
                        return columnName;
                    } else {
                        return null;
                    }
                });
                String content = buildTableDef(tableName, tableDefPackage, tableDefClassName, columnList, columnMap);
                try {
                    processGenClass(tableDefPackage, tableDefClassName, content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
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

    public List<String> columnInfoList(TypeElement classElement, Function<Element, String> function) {
        List<String> columnInfoList = new ArrayList<>();
        for (Element fieldElement : classElement.getEnclosedElements()) {
            if (ElementKind.FIELD == fieldElement.getKind()) {
                String column = function.apply(fieldElement);
                if (column != null && !column.isEmpty()) {
                    columnInfoList.add(column);
                }
            }
        }
        classElement = (TypeElement) typeUtils.asElement(classElement.getSuperclass());
        if (classElement != null) {
            columnInfoList.addAll(columnInfoList(classElement, function));
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

    private String buildTableDef(String table, String tableDefPackage, String tableDefClassName, List<String> columnList, Map<String, List<String>> columnMap) {
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
        columnList.forEach((column) -> {
            content.append("    public final ColumnDef ")
                    .append(column)
                    .append(" = new ColumnDef(")
                    .append("this,")
                    .append("\"").append(column).append("\");\n");
        });
        content.append("\n    public final ColumnDef[]columns")
                .append(" = new ColumnDef[]{")
                .append(String.join(",", columnList))
                .append("};\n");
        if (columnMap != null && !columnMap.isEmpty()) {
            columnMap.forEach((k, v) -> {
                if (!v.isEmpty()) {
                    content.append("    public final ColumnDef[]")
                            .append(k)
                            .append(" = new ColumnDef[]{")
                            .append(String.join(",", v))
                            .append("};\n");
                }
            });
        }
        content
                .append("\n    public ").append(tableDefClassName).append("() {\n")
                .append("        this").append("(null);\n")
                .append("    }")
                .append("\n    public ").append(tableDefClassName).append("(String alias) {\n")
                .append("        super").append("(\"").append(table).append("\",alias);\n")
                .append("    }\n\n}\n");
        return content.toString();
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

    private Map<String, Map<String, Set<String>>> tableFieldMap(RoundEnvironment roundEnv) {
        Map<String, Map<String, Set<String>>> tableFieldMap = new HashMap<>();
        Set<? extends Element> viewElements = roundEnv.getElementsAnnotatedWith(View.class);
        for (Element viewElement : viewElements) {
            String entityClass = viewElement.toString();
            String className = getClassName(entityClass);
            View view = viewElement.getAnnotation(View.class);
            String viewStr = view.toString();
            String tableClassName = viewStr.substring(viewStr.lastIndexOf("=") + 1, viewStr.lastIndexOf(")"));
            List<String> columnInfoList = columnInfoList((TypeElement) viewElement, fieldElement -> {
                if (fieldElement.getAnnotation(Ignore.class) != null) {
                    return null;
                }
                return fieldElement.toString();
            });
            Set<String> columnSet = new HashSet<>(columnInfoList);
            Map<String, Set<String>> fieldMap = tableFieldMap.get(tableClassName);
            if (fieldMap == null) {
                fieldMap = new HashMap<>();
                tableFieldMap.put(tableClassName, fieldMap);
            }
            String lowClassName = String.valueOf(Character.toLowerCase(className.charAt(0))).concat(className.substring(1));
            fieldMap.put(lowClassName, columnSet);
        }
        return tableFieldMap;
    }
}
