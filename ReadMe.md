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
* Attach *.mdf files to database:
    * Right click on database folder select attach
    * Add all mdf files
* Create kef database user:
    * Go to Security->Logins right click new Login
    * Insert Login name eg kef
    * Choose SQL Server authentication and set the same password as it is on the existing installation
    * Set as Default database master 
    * In the field Select a page 
      * Select Server Roles and after that check all the checkboxes.
      * Select User mapping and after that check all the databases.
    * Select Ok
* Open kef:
    * Go to Organwsi->Organwsi->Systima->SQL server setup
    * Fix user, password and hostname
    * Assign  changes
 * Enable TCP connection to database
    * You need to Go to Start > Microsoft SQL Server > Configuration Tools > SQL Server Configuration Manager
    * SQL Server Configuration Manager > SQL Server Network Configuration > Protocols for SQLExpress and enable TCP connections
* Change windows display language to Greek
   * Start  > Settings  > Language. 
* Change language for non unicode programs to greek
   * Control panel > region > admin > change local settings
* Install driver for printer [link](https://support.infopos.gr/tameiakes-michanes/)

