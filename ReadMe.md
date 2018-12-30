# Overview
An MVC Java application that is used by a supermarket to automate a daily process.
The automation steps involve:
* calculation of new prices
* update of ERP application databases with new prices
* exporting price labels for products
* exporting a catalogue with new prices

# Build & Run
* Install:
  * Intellij IDEA
  * SQL Server Management Studio 2012 using the following [tutotrial](http://eservices.unisoft.gr/pages/loadpage.asp?id=1079)
  * Kefalaio 5 ERP
  * Java SE Development Kit 8
  * Git for Windows
* Import project using Intellij IDEA
* Copy credentials in cfg folder
* Build and Run using Intellij IDEA

# Software Requirements
* Intellij IDEA
* Java JDK 1.8.0_171
* Kefalaio 5 ERP (Unisoft)

# Migration Instructions for Kefalaio 5 & SQL Database
* Copy and paste Kefalaio5 folder
* Copy *.mdf from C:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\DATA
* Install SQL Server Management Studio 2012 using the following [tutotrial](http://eservices.unisoft.gr/pages/loadpage.asp?id=1079) and the installation file inside kefalaio5/SQL
* Open SQL Server Management Studio 2012 and connect using the following for login:
    * Server type:  Database Engine
    * Server name: .
    * Authentication: Windows Authentication
* Create kef5 database user:
    * Go to Security->Logins right click new Login
    * Insert Login name eg kef5
    * Choose SQL Server authentication and set password
    * Set as Default database master 
    * In the field Select a page we select Server Roles and after that we select dbcreator.
    * Select Ok
* Attach *.mdf files to database:
    * Right click on database folder select attach
    * Add all mdf files
* Open kef5:
    * Go to Organwsi->Organwsi->Systima->SQL server setup
    * Fix user, password and hostname
    * Assign  changes
 
