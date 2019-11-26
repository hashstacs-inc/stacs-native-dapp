#!/bin/bash

if [[ $TRAVIS_BRANCH == dev* ]];
then
  echo -e "Publishing maven snapshot...\n"
  mvn -s bin/settings.xml deploy -DskipTests -Dmaven.install.skip=true -Ddockerfile.skip
  echo -e "Published maven snapshot"
elif [[ $TRAVIS_BRANCH == master ]] || \
   [[ $TRAVIS_BRANCH == release* ]];
then
  echo -e "Publishing maven snapshot...\n"
  mvn -s bin/settings.xml deploy -DskipTests -Dmaven.install.skip=true -Ddockerfile.username=$DOCKER_USERNAME -Ddockerfile.password=$DOCKER_PASSWORD -B
  echo -e "Published maven snapshot"
else
  echo -e "branch $TRAVIS_BRANCH no need publish"
fi