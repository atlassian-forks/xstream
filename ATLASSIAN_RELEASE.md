# Xstream Release for Atlassian Use

This branch contains configuration and code changes to release XStream and publish package to maven.
At the time of creating this process, there is no CI process available to push changes 
to github from bamboo and do releases from a CI pipeline. 
Hence release for this library needs to be done manually. Another thing to note is that the 
package is not signed.

The version mentioned in the POM files here is  **1.4.22-atlassian-02-SNAPSHOT**

The version is a combination of the original version **1.4.22** and the atlassian version **atlassian-02-SNAPSHOT**.

When making a release version, replace the SNAPSHOT with **-mX**, 

for example **1.4.22-atlassian-02-m2**.


### Before Release
1. Make your custom changes in a different branch and merge them into v-1.4.x
2. Make sure to take changes from upstream(i.e v-1.4.x) and merge them into this branch.


### To Release

Follow the standard maven process for release. 

mvn release:clean

mvn release:prepare

mvn release:perform

This will create a release version and push it 
to [internal repository](https://packages.atlassian.com/maven/com/thoughtworks/xstream/xstream/) 
and the maven repository.
