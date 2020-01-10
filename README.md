[![Build Status](https://travis-ci.org/Auroraic/stacs-native-dapp.svg?branch=master)](https://travis-ci.org/Aurorasic/stacs-native-dapp)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.stacs.nav:stacs-native-dapp&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.stacs.nav:stacs-native-dapp)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.stacs.nav/stacs-native-dapp/badge.svg?cache=foo)](https://search.maven.org/search?q=g:%22io.stacs.nav%22%20AND%20a:%22stacs-native-dapp%22)
# Welcome to Stacs-Native-Dapp

## What is Stacs-Native-Dapp ?
Dapp is a decentralized application. At present, the traditional block chain DApp is based on smart contract, and a whole set of business logic and data processing are implemented on it. On the basis of BD, Stacs Dapp realizes some or all business functions of BD. Different dapps execute one or more business functions of BD respectively, and multiple Stacs dapps assist each other to execute BD, so as to solve the decentralization problem in the process of financial business execution.

Description of each module:
1. drs-boot: The running environment of Dapp is responsible for the life cycle of Dapp download, installation, operation and uninstallation
2. drs-service: The implementation of Dapp related interfaces is provided to realize the unified processing of block chain requests and callbacks
3. drs-api: Dapp requires a dependency API interface layer
4. dapp-core: Includes event communication handling for Drs and Dapp
5. dapp-sample: Sample code for a dapp

## More details about stacs-native-dapp
[Detailed introduction](https://stacs-native.netlify.com/design/dapp/)
