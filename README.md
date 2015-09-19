# scala-cta-demo

## Introduction

This is a repo to explore some basic concepts of Scala and functional data processing.
The data used is by the Chicago Transit Authority (CTA) and is part of their freely downloadable
[datasets](https://data.cityofchicago.org/Transportation/CTA-List-of-CTA-Datasets/pnau-cf66).

## Getting Started

This project is based [Typesafe Activator](https://www.typesafe.com/activator), which is an
[SBT](http://www.scala-sbt.org/) based tool. Activator simplifies the installation of SBT and
scala and provides a script that allows you to get started very quickly.

When you run Activator you are actually running SBT, which gives an interactive SBT shell
where you can run different commands.

First, run activator in a shell from a checkout of this project:

    ./activator

You should see something similar to:

    [info] Set current project to uic-cta-demo (in build file:~/projects/uic-cta-demo/)
    >

Now that you have an SBT shell, you can run all tests by just typing `test`.

You should see something similar to:

    [info] Tests: succeeded 2, failed 0, canceled 0, ignored 0, pending 0
    [info] All tests passed.
    [success] Total time: 1 s, completed Sep 18, 2015 6:38:29 PM

You can leave the SBT shell at any time using the `exit` command.

Before we can run the core of the project, we need to download the datasets. There is a
bash script that downloads them for you:

    ./download_datasets.sh

