
> `mvn clean install -DskipTests=true -Dcheckstyle.skip=true -Dforbiddenapis.skip=true  -Dcheckstyle.skip=true -Dcheckstyle.skip=true -Dmaven.test.skip=true  -s C:\Java\apache-maven-3.5.0\conf\settings.xml`
> 先编译Netty/Common 模块
> Netty/Common 模块编译后会在target 中生成一些java 源文件，不知道这些源文件是怎么生成的。
> 但是在其他地方有用到它们，所以必须要先编译Netty/Common 模块才可以使用。
> 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
