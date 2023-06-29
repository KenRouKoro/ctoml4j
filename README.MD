# ctoml4j

![GitHub last commit (by committer)](https://img.shields.io/github/last-commit/KenRouKoro/ctoml4j)
![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/KenRouKoro/ctoml4j/gradle.yml)
[![GitHub release](https://img.shields.io/github/release/kenRouKoro/ctoml4j.svg)](https://GitHub.com/kenRouKoro/ctoml4j/releases/)
![GitHub commit activity (branch)](https://img.shields.io/github/commit-activity/t/KenRouKoro/ctoml4j)
![GitHub](https://img.shields.io/github/license/KenRouKoro/ctoml4j)


![GitHub top language](https://img.shields.io/github/languages/top/KenRouKoro/ctoml4j)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)


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