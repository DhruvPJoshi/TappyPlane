# Guidelines

**Note:** The project is in initial stage. More guideline notes will soon be added.

## Code Formatting

To ensure consistent source code format, the [spotless](https://github.com/diffplug/spotless/tree/master/plugin-gradle#applying-to-java-source-google-java-format) plugin is applied with google-java-format.

Using spotless plugin is very simple. When you have created a new or edited an existing `.java` source file, to make sure that your code is formatted properly, execute the following command from the root project:

```
$ ./gradlew spotlessCheck
```

If there were no errors, **bingo!** The code is in proper format. However if there were any errors, use the following command to format it properly:

```
$ ./gradlew spotlessApply
```
