# README #

Welcome to development at Bright!

### What is this repository for? ###

* This repository entails the codebase for the Bright mobile app.
* Platform: Android
* Language: Java
* Version: MVP
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo) - I briefly used this resource in order to produce this README, but it is somewhat intuitive

### How do I get set up? ###

* Install Oracle's [JRE](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) and [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (n.b. the JRE may be contained in the JDK, I'm not entirely sure, so install the JDK first)
* Install Git (platform-dependent on the procedure for installing)
* [Download Android Studio](https://developer.android.com/studio/index.html) and follow the instructions on the install page you are redirected to
* Install Android Studio, SDK Tools, and AVD Manager in the installer
* Clone the repository into the folder in which you keep your Android Studio Projects, and load the project from file
* If you have an Android phone, and you wish to use that for testing, [give it a shot](https://developer.android.com/studio/run/device.html)
* Otherwise, or in addition, [set up an emulator](https://developer.android.com/studio/run/emulator.html)

### Contribution guidelines ###

* For each task, first pull the latest master branch into your directory:

```
git checkout master
git pull
```

* Then, create a new branch with name corresponding to initial label in the Issue. For example:

```
git checkout -b AN-0001
```

* Commit messages are to include the aforementioned label, and are to describe what has been achieved in the particular commit:

```
git commit -am "AN-0001 - added SQL library includes"
```

* Pushing for the first time for a branch:
```
git push -u origin AN-0001
```

* Pushing afterwards:
```
git push
```

* Suppose I want to work on AN-0002 now, which in this case does not exist:
```
git checkout master
git pull
git checkout -b AN-0002
```

* A branch should be worked on by ONE person, the person who the issue is assigned to, unless otherwise discussed.
* Do NOT push to master unless instructed or if discussed
* To merge a branch into master, create a pull request. Given the code has been "approved", you are safe to press "merge".
* On this note, do not be afraid of merge conflicts. Will discuss as they arise.
* Code review - in the PR, comment away if see room for improvement/have discussion points/see any glaring errors

### Testing/QA ###

* We will need to get into a rhythm of performing QA after a branch has been merged into master. This means that another set of eyes, apart from the branch's creator, will be able to confirm that the code works and is bug-free. The specifics of this I will need to confirm, but at this stage, once a branch has been merged, create an issue called "QA - [title of issue here]".
* Writing tests - Android Studio provides reasonable support for unit testing. Given functionality is added to the code base, corresponding tests ought to accompany it.

### Who do I talk to? ###

* Repo owner or admin: Daniel Collins
* Other community or team contact: Bright's Slack on the #software channel, or other channels depending on what you need to know. Facebook for urgent messages.