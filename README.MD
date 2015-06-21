# Graduation Project

## Getting started
First download and install [maven], ([how to install maven]) and [git]

On windows, navigate to an empty folder, right click and click "Git Bash Here" then write the following commands

    git clone https://github.com/mohamedkomalo/grad-project

The compile the project to make maven to download the libraries used, refer to the compiling section.

If you want to import the project in eclipse: File -> Import -> Maven -> Existing maven project

**You will have to manually download [OpenCV](http://opencv.org/downloads.html) and [import it in eclipse](http://docs.opencv.org/trunk/doc/tutorials/introduction/java_eclipse/java_eclipse.html) because the OpenCV maven package doesn't support windows**

## Dependecies issue
since we depend on OpenCV, and it is not yet in maven, you will have to manually download and extract it [OpenCV 3.0]
then you will have to create an environment variable "OPENCV3_HOME" for its path, maven is configured to use this environment variable to find the library

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
  					.
  					.
  				test/
  					apple
  					bikes
  					.
  					.
  					combined
  			features/
  				train.txt
  				test.txt
  			classifiers/
  				svm.xml
  
  		svmguide4
  			features
  
  any images folder is ignored from the source control
  to avoid bloating the repo with binary files
  
  the "combined" folder belong to test images that belong to more than one category 
 

[OpenCV 3.0]:http://opencv.org/downloads.html
[maven]:http://maven.apache.org/download.cgi
[git]:http://git-scm.com/downloads
[how to install maven]:http://www.mkyong.com/maven/how-to-install-maven-in-windows/
