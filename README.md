技术交流QQ群：651499479，欢迎java大神指点迷津，也欢迎新手进群学习。


# jframe
java web application architecture based on spring mvc

## todos
1. AppContxt.getCurrentRequest 与直接 request 注入方式的性能比较；
2. 抛异常的方式与直接返回的方式的性能比较；

## create maven archetype commands
1. mvn archetype:create-from-project //安装该archetype项目到你的本地仓库
2. cd target/generated-sources/archetype
3. mvn install
4. mvn archetype:crawl //执行crawl命令，生成archetype-catalog.xml，会发现在咱们的本地仓库的根目录生成archetype-catalog.xml骨架配置文件。
