# Scheduling App

The purpose of this app is to allow a user to manage contacts, customers, and schedules (appointments) stored in a local database.

## Author
Jesse Westendorf

## Contact Information
jweste5@wgu.edu

## Application Version
1.0.0

## Date
12/27/2022

## IDE
IntelliJ IDEA 2021.1.3 (Community Edition)
Build #IC-211.7628.21, built on June 30, 2021
Runtime version: 11.0.11+9-b1341.60 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
Windows 10 10.0
GC: G1 Young Generation, G1 Old Generation
Memory: 768M
Cores: 4
Non-Bundled Plugins: Statistic (4.1.10)
Kotlin: 211-1.4.32-release-IJ7628.19

## JDK
Java SE 11.0.11

## JavaFX
openjfx 11.0.2

## How to run the program
1) Setup project with libraries
    1) JavaFX
    2) MySQL Connector
2) Setup MySQL
    1) Create user "sqlUser" with password "Passw0rd!"
    2) Import all files in ./DatabaseSchema
    3) Set privileges to user for schema "client_schedule" (all Object Rights and DDL Rights)
3) Set VM options to --module-path ${PATH_TO_FX} --add-modules javafx.fxml,javafx.graphics,javafx.controls
4) Set Main method
5) Run in IntelliJ

## Extra Report
I added an extra report that shows the total number of appointments, customers, and contacts.

## MySQL Connector
mysql-connector-java-8.0.25