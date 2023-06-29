# ctoml4j
ctoml4j是基于[TOML4J](https://github.com/mwanji/toml4j)修改而来的[TOML 0.4.0](https://github.com/toml-lang/toml/blob/master/versions/en/toml-v0.4.0.md)解析库  
增加了输出注释支持  
**示例：**
```java
static class AClass {
    @OutputAnnotation("Hello")
    int anInt = 1;
    @OutputAnnotation(value = "World" ,at = Location.Right)
    int[] anArray = { 2, 3 };
}

TomlWriter tomlWriter = new TomlWriter();
AClass obj = new AClass();

Map<String, Object> map = new HashMap<String, Object>();
int[] intArray = { 2, 3 };
map.put("anInt", 1);
map.put("anArray", intArray);

String tomlString = tomlWriter.write(obj);
tomlString = tomlWriter.write(map);

tomlWriter.write(obj, new File("path/to/file"));
tomlWriter.write(obj, new ByteArrayOutputStream());
tomlWriter.write(obj, new OutputStreamWriter(anOutputStream));

/*
All methods output:

# Hello
anInt = 1
anArray = [2, 3] # World
*/
```