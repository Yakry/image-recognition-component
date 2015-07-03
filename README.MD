# Graduation Project

## Getting started
First download and install [maven], ([how to install maven]) and [git]

On windows, navigate to an empty folder, right click and click "Git Bash Here" then write the following commands

    git clone https://github.com/mohamedkomalo/grad-project

The compile the project to make maven to download the libraries used, refer to the compiling section.

If you want to import the project in eclipse: File -> Import -> Maven -> Existing maven project

** [import it in eclipse](http://docs.opencv.org/trunk/doc/tutorials/introduction/java_eclipse/java_eclipse.html) because the OpenCV maven package doesn't support windows**

## Dependecies issue
We use the following fork of [OpenCV](https://github.com/mohamedkomalo/opencv/), you will have to clone and build it from source.

since we depend on OpenCV, and it is not yet in maven, you will have to manually download and extract it [OpenCV 3.0]
then you will have to create an environment variable "OPENCV3_HOME" for its path, maven is configured to use this environment variable to find the library

the path of OPENCV3_HOME should have the same hierarchy of OpenCV which is
  build/
    /java
      opencv-300.jar
      /x86                    (if you are running x86)
        opencv_java300.dll
      /x64                    (if you are running x64)
        opencv_java300.dll 


## Compiling
To compile the project use, open the git bash in the project folder, write 

    mvn compile

To run the tests, use

    mvn test

## Dataset folder

the datasets should be stored in the project root folder in the following hierarchy
  
 	datasets/
  		calteckUniversity/
  			images/
  				train/
  					apple
  					bikes
  				test/
  					apple
  					bikes
          localization/
            apple
  					combined
  			features/
  				train.txt
  				test.txt
  			featureVectorGenerators/
  				surf.xml
  
  		svmguide4
  			features
  
  any images folder is ignored from the source control
  to avoid bloating the repo with binary files
  
  the "combined" folder is a special class that is not loaded by default unless explicitly requested
  it is used in localization tests where more than one object exists in the image
 

[OpenCV 3.0]:http://opencv.org/downloads.html
[maven]:http://maven.apache.org/download.cgi
[git]:http://git-scm.com/downloads
[how to install maven]:http://www.mkyong.com/maven/how-to-install-maven-in-windows/
