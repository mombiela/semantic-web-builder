Required Packages
-----------------
The following packages are required for this Java client to work:
* J2SE version 1.4.2 or higher. This is available from http://java.sun.com
* A JCE compliant crypto implementation (crypto-124.tar.gz or crypto-124.zip) from BouncyCastle that can be obtained from http://www.bouncycastle.org/latest_releases.html. More information about BouncyCastle can be obtained from http://www.bouncycastle.org. 
* The lastest version for JCE(Java Cryptography Extension). This can be found at http://java.sun.com/j2se/1.4.2/download.html. Then copy the jar files to $JAVA_HOME/jre/lib/security directory. Additional information about JCE and JCA (Java Cryptography Architecture) can be obtained from http://java.sun.com/products/jce/index.jsp.
* An OpenSSL distribution. This can be obtained from http://www.openssl.org.
 
Package Installation
---------------------
* Install OpenSSL binaries. Follow the installation instruction for OpenSSL which is available at http://www.openssl.org
* Untar the bouncycastle crypto file. Setup your classpath with bouncycastle jar file. Please refer to enclosed "set_classpath.bat" for windows and "set_classpath.sh" for UNIX. 
* Use key.bat (for Windows) or key.sh (for UNIX) to generate public and private key pair, as well as a PKCS12 file which contains user's private key and certificate.
 
Building the ButtonEncryption Application
------------------------------------------
 
* On Windows, use build-window.bat to build ButtonEncryption application.
* On Unix platforms, use build-app.sh to build ButtonEncryption application.
 
To run the example code
------------------------
* Run ButtonEncryption application. 
java ButtonEncryption <CertFile> <PKCS12File> <PPCertFile> <Password> <CmdTxt> <OutputFile>
 
For example, 
java ButtonEncryption my-pubcert.pem my-prvkey.p12 paypal_pubcert.pem password “cmd=_xclick,business=x@test.com,amount=23.00,currency_code=USD,item_name=todos son asi,cert_id=ZWTMUVWGGH8T8" java_bn.html 
 
If you would like to use Sandbox, please add the word "sandbox" as last argument
For example, 
java ButtonEncryption my-pubcert.pem my-prvkey.p12 paypal_pubcert.pem password "cmd=_xclick,business=x@test.com,amount=23.00,currency_code=USD,item_name=todos son asi,cert_id=ZWTMUVWGGH8T8" java_bn.html sandbox
 
Files included in this distribution
------------------------------------
 
1) The ButtonEncryption project. This project is a java application that will take command line options and in end, an Html file will be generated which you can use to test your settings. 
 
2) key.bat, a batch file that will generate a public and private key pair, as well as a PKCS12 file which contains user's private key and certificate on windows. Just run "key <basename>" and it will genereate basename-prvkey.pem, basename-pubcert.pem and basename-prvkey.p12. This also requires the "OpenSSL for Windows" package. 
 
3) Key.sh, a shell script that will generate a public and private key pair, as well as a PKCS12 file which contains user's private key and certificate on UNIX. Just run "key.sh <basename>" and it will generate basename-prvkey.pem, basename-pubcert.pem and basename-prvkey.p12.
 
4) build-window.bat, a batch file which is used to build ButtonEncryption application on windows.
 
5) build-app.sh, a shell script which is used to build ButtonEncryption application on UNIX. 
 
6) set_classpath.bat, a batch file that will setup classpath for bouncycastle crypto jar files.
 
7) set_classpath.sh, a shell script that will setup classpath for bouncycastle crypto jar files.


