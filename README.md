There are of course gobs of ways to load projects into Eclipse or IDEA. The following two are based on Maven and the pom file included in the project. You might also have luck with simply loading the appropriate project file.

You will need JDK 7 or 8 installed. The end-of-life'd JDK 6 should work with perhaps a few small changes.

Please don't hesitate to contact me at jeff @ langrsoft.com if you have any problems.

Disclaimer: Some of the source in the codebase deliberately stinks. Some of it stinks because, well, it's easy for all of us to write code we're soon not proud of. (No worries--we accept that reality and know that we can incrementally improve the code.)

Eclipse Instructions
---

* Open the `File->Import` dialog. Select `Existing Maven Projects` and click `Next`.
* Select the `library` directory as the root folder from the `Select Root Folder` dialog. Click `Open`.
* From the `Package Explorer`, select the `library` project.
* From the context (right-click) menu, select `Run As->JUnit Test`.

You should see about 190 or so passing (green) unit tests.

JetBrains IDEA Instructions
---

* Start IntelliJ IDEA.
* Select `Import Project`.
* From the dialog `Select File or Directory to Import`, select the `library` directory and click `OK`.
* From the dialog `Import Project`, ensure that the radio button `Import project from external model` is selected, and ensure that `Maven` is selected in the list. Click `Next`.
* From the next very detailed dialog, click `Next`.
* From the next dialog, you should see `com.langrsoft:library:0.0.1-SNAPSHOT` selected in the list of Maven projects to import. Click `Next`.
* From the next dialog, select an SDK version (either 1.7 or 1.8).
* From the next dialog, click `Finish`.
* From the IntelliJ IDEA menu, select `Run->Edit Configurations...`.
* From the `Run/Debug Configurations` dialog, press the `+` button (located in the upper left portion of the dialog) to create a new configuration.
* From the `Add New Configuration` popup, select `JUnit`.
* Change the configuration name to `All Tests`.
* Under the `Configuration` tab, change the `Test Kind` dropdown to be `All in package`.
* From the `Search for tests:` radio group, select `In whole project`.
* Click `OK`.
* From the IntelliJ IDEA menu, select `Run->Run 'All Tests'`.

You should see about 190 or so passing (green) unit tests.
