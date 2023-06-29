package com.moandjiezana.toml;

import cn.korostudio.ctoml.Location;
import cn.korostudio.ctoml.OutputAnnotationData;

import static com.moandjiezana.toml.PrimitiveArrayValueWriter.PRIMITIVE_ARRAY_VALUE_WRITER;
import static com.moandjiezana.toml.TableArrayValueWriter.TABLE_ARRAY_VALUE_WRITER;
import static com.moandjiezana.toml.ValueWriters.WRITERS;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MapValueWriter implements ValueWriter {
  static final ValueWriter MAP_VALUE_WRITER = new MapValueWriter();

  private static final Pattern REQUIRED_QUOTING_PATTERN = Pattern.compile("^.*[^A-Za-z\\d_-].*$");

  @Override
  public boolean canWrite(Object value) {
    return value instanceof Map;
  }

  @Override
  public void write(Object value, WriterContext context) {
    Map<?, ?> from = (Map<?, ?>) value;

    if (hasPrimitiveValues(from, context)) {
      context.writeKey();
    }

    // Render primitive types and arrays of primitive first so they are
    // grouped under the same table (if there is one)
    for (Map.Entry<?, ?> entry : from.entrySet()) {
      Object key = entry.getKey();
      Object fromValue = entry.getValue();

      OutputAnnotationData annotationData = null;
      boolean isAnnotation = fromValue instanceof OutputAnnotationData;
      if (isAnnotation){
        annotationData = (OutputAnnotationData)fromValue;
        fromValue = annotationData.getData();
      }

      if (fromValue == null) {
        continue;
      }

      ValueWriter valueWriter = WRITERS.findWriterFor(fromValue);
      if (valueWriter.isPrimitiveType()) {
        printTop(annotationData,context);
        context.indent();
        context.write(quoteKey(key)).write(" = ");
        valueWriter.write(fromValue, context);
        printRight(annotationData,context);
        context.write('\n');
        printBottom(annotationData,context);
      } else if (valueWriter == PRIMITIVE_ARRAY_VALUE_WRITER) {
        printTop(annotationData,context);
        context.setArrayKey(key.toString());
        context.write(quoteKey(key)).write(" = ");
        valueWriter.write(fromValue, context);
        printRight(annotationData,context);
        context.write('\n');
        printBottom(annotationData,context);
      }
    }

    // Now render (sub)tables and arrays of tables
    for (Object key : from.keySet()) {
      Object fromValue = from.get(key);

      OutputAnnotationData annotationData = null;
      boolean isAnnotation = fromValue instanceof OutputAnnotationData;
      if (isAnnotation){
        annotationData = (OutputAnnotationData)fromValue;
        fromValue = annotationData.getData();
      }

      if (fromValue == null) {
        continue;
      }

      ValueWriter valueWriter = WRITERS.findWriterFor(fromValue);
      if (valueWriter == this || valueWriter == ObjectValueWriter.OBJECT_VALUE_WRITER || valueWriter == TABLE_ARRAY_VALUE_WRITER) {
        printTop(annotationData,context);
        valueWriter.write(fromValue, context.pushTable(quoteKey(key)));
        printBottom(annotationData,context);
      }
    }
  }

  @Override
  public boolean isPrimitiveType() {
    return false;
  }

  private static String quoteKey(Object key) {
    String stringKey = key.toString();
    Matcher matcher = REQUIRED_QUOTING_PATTERN.matcher(stringKey);
    if (matcher.matches()) {
      stringKey = "\"" + stringKey + "\"";
    }

    return stringKey;
  }

  private static boolean hasPrimitiveValues(Map<?, ?> values, WriterContext context) {
    for (Object key : values.keySet()) {
      Object fromValue = values.get(key);
      if (fromValue == null) {
        continue;
      }

      ValueWriter valueWriter = WRITERS.findWriterFor(fromValue);
      if (valueWriter.isPrimitiveType() || valueWriter == PRIMITIVE_ARRAY_VALUE_WRITER) {
        return true;
      }
    }

    return false;
  }

  public static void printTop(OutputAnnotationData annotationData,WriterContext context){
    if (annotationData==null||annotationData.getAt()!= Location.Top)return;
    context.indent();
    context.write("# "+ annotationData.getValue());
    context.write('\n');
  }
  public static void printRight(OutputAnnotationData annotationData,WriterContext context){
    if (annotationData==null||annotationData.getAt()!=Location.Right)return;
    context.write(" # "+ annotationData.getValue());
  }
  public static void printBottom(OutputAnnotationData annotationData,WriterContext context){
    if (annotationData==null||annotationData.getAt()!=Location.Bottom)return;
    context.indent();
    context.write("# "+ annotationData.getValue());
    context.write('\n');
  }

  private MapValueWriter() {}
}
