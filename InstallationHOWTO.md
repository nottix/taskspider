# Installation HOW-TO #

## Requsiti ##
  * Tomcat 5.5
  * Ant 1.7
  * Java SDK 1.5

## Installazione ##
1. Posizionarsi nella directory root di tomcat;

2. Modificare il file di configurazione `conf/policy.d/04webapps.policy` tramite il seguente comando:
```
echo "grant codeBase \"file:<taskspiderDirectory>-\" {
    permission java.security.AllPermission;
};" >> conf/policy.d/04webapps.policy
```
dove `<taskspiderDirectory>` equivale alla directory di installazione della webapp taskspider all'interno di tomcat, ad es:
```
/var/lib/tomcat5.5/webapps/taskspider/
```
3. Effettuare il checkout dal repository in una directory predefinita:
```
cd /home/stud/projects/
svn checkout http://taskspider.googlecode.com/svn/trunk/TaskSpider
```
4. Spostarsi nella directory root di taskspider;
```
cd TaskSpider
```
5. Aprire il file `build.xml` con un editor e modificare le variabili `tomcat` e `instdir` inserendo rispettivamente la directory root di tomcat e la directory in cui si desidera installare taskspider, ad es:
```
<property name="tomcat" value="/var/lib/tomcat5.5"/>
<property name="instdir" value="/home/stud/programs/taskspider"/>
```
6. Aprire il file `conf/config.properties.conf` e modificare le direttive desiderate tra cui:

> `logPath`: path in cui salvare il file di log;

> `indexPath`: path in cui salvare gli indici del sistema;

> `wordnetIndexPath`: path in cui salvare gli indici di wordnet;

> `prologFilename`: path in cui è posizionato il file contenente le direttive di wordnet.

7. Modificare il file `src/taskspider/util/properties/PropertiesReader.java` camabiando il percorso in cui si trova il file conf.properties, ad es:
```
file = new File("/home/stud/programs/taskspider/conf/conf.properties");
```
8. Assicurarsi che la variabile d'ambiene `JAVA_HOME` sia impostata e che punti al jdk 1.5, ad es:
```
JAVA_HOME="/usr/lib/jvm/java-1.5.0-sun/"
export JAVA_HOME
```
9. Digitare il seguente comando per compilare:
```
ant DeleteClasses
ant Compila
```
10. Per caricare la webapp assicurarsi che tomcat sia disattivo, in seguito caricare la webapp tramite:
```
ant Deploy
sudo ant TomcatDeploy
```
11. Avviare tomcat e testare il funzionamento della webapp tramite il seguente comando:
```
firefox http://localhost:8180/taskspider/
```
12. Se tutto procede alla perfezione si può avviare il taskspider chiamando:
```
ant Run
```