# 拦截器
[![Maven Central][mavenbadge-svg]][mavencentral]

hilt的扩展，提供增强功能

[mavenbadge-svg]: https://maven-badges.herokuapp.com/maven-central/io.github.chengzis/interceptor/badge.svg
[mavencentral]: https://search.maven.org/artifact/io.github.chengzis/interceptor-annotation


## Download

1. 添加ksp插件
   在项目的build.gradle中添加ksp插件
    ```grooy
    plugins {
        id 'com.google.devtools.ksp' version '1.8.20-1.0.10'
    }
    ```
    在模块的build.gradle中添加ksp插件
    ```grooy
    plugins {
        id 'com.google.devtools.ksp'
    }
    ```
2. 添加依赖
    ```grooy
     implementation 'io.github.chengzis:ksp-annotation:$last-version'
     ksp 'io.github.chengzis:ksp-compiler:$last-version'
    ```
