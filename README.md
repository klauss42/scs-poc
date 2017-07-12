# scs Proof-of-Concept

Dieses Repository beinhaltet eine einfache Implementierung, um eine Frontend
Integration von verschiedenen scs Diensten sowie die Verwendung einer
PS-Engine zu testen.

Zum einfacheren Entwickeln und Testen sind alle Dienste innerhalb dieses
Repositories vorhanden. Prinzipiell bestehen die einzelnen scs Dummies aus
einem service Modul für eventuelle Backend Services sowie ein UI Modul, das (meist)
über [Thymleaf 3](http://www.thymeleaf.org/) Html erzeugt.

Die PS-Engine ist derzeit nur ein einfacher REST Controller, der bei gerader bzw.
ungerader User Id Listen von Beats zurück gibt. Bei fehlender User Id wird eine
zufällige Liste ausgewürfelt.
Die Connect- und Vehicle Services können dann mit diesen PS-Engine Beats nach
Daten gefragt werden, die dann mit Objekten mit beliebigen Daten sowie einem
Template Namen antworten. Diese Objekte werden dann vom beats-render-service
serverseitig in Html übersetzt und dann serverseitig von der home-ui Anwendung
in die Homepage integriert.

Die meisten Services registrieren sich dynamisch in einem verteilen Dienst für
Service Discovery. Daneben wird z.B. die PS-Engine als statische Url konfiguriert.
Die Konfiguration der Dienste befindet sich im Git Repository
[scs-poc-config](scs-poc-config).

Zusätzliche Informationen sind im SinnerSchrader Wiki unter
[scs - POC](https://wiki.sinnerschrader.com/display/AUDI/scs+-+POC)
sowie
[scs - POC Architektur](https://wiki.sinnerschrader.com/display/AUDI/scs+-+POC+Architektur)
nachlesbar.

# Vorschau

Die Software wird aktuell bei neuen Commits automatisch von Jenkins gebaut und
deployed. Die Software kann man sich dann
[hier](http://node1-klasch1.x-temp.ham2.szops.net:4000/home-ui/)
ansehen.

# Bauen + Starten

## Vorausetzungen

+ Java 8
+ Maven 3
+ NodeJS, npm
+ Docker, docker-compose
+ Repository [scs-poc](https://git.sinnerschrader.com/AUD/scs-poc)
    + `git clone git@git.sinnerschrader.com:AUD/scs-poc.git`
+ Repository [scs-poc-config](https://git.sinnerschrader.com/AUD/scs-poc-config)
    + `git clone git@git.sinnerschrader.com:AUD/scs-poc-config.git`
+ Repository [scs-poc-images](https://git.sinnerschrader.com/AUD/scs-poc-images) (nur für Docker)
    + `git clone git@git.sinnerschrader.com:AUD/scs-poc-images.git`

### Ohne Docker

1. Im Projekt `scs-poc-config` den [Consul](https://www.consul.io/) per
`./consulw.sh` starten
1. Im Projekt `scs-poc-config` `dev-git2consul.sh` aufrufen um die lokale Konfiguration in Consul
zu importieren
1. scs-poc bauen:
    + das erste Mal: Parent POM installieren: `cd parent; mvn clean install -DskipTests`
    + weitere Builds: `mvn clean install -DskipTests`
1. gewünschte Anwendungen per `mvn spring-boot:run` auf der
Kommandozeile oder die Spring Boot Application direkt in der IDE starten
    + `cd shared/auth`
    + `mvn spring-boot:run`

### Mit Docker

##### Vorbereitungen macOS:
Lokale Maven settings.xml editieren (`~/.m2/settings.xml`) und die IP des Docker Daemons auf macOS eintragen, z.B.: 

    <profiles>
        <profile>
            <id>inject-docker-host</id>
            <properties>
                <docker.host>tcp://192.168.99.100:2376</docker.host>
            </properties>
        </profile>
    </profiles>   
    <activeProfiles>
        <activeProfile>inject-docker-host</activeProfile>
    </activeProfiles>
        
##### Vorbereitungen Linux:
Die Maven Builds gehendavon aus, dass der Docker Daemon auf Linux unter `unix:///var/run/docker.sock` erreichbar ist.

#### Docker Images bauen
1. Docker Base Images in Projekt `scs-poc-images bauen:
    + macOS: `mvn clean install`
    + Linux: `mvn clean install -Psocket` 
    - _(wenn es Fehler beim docker push gibt, kann man auch `mvn package` aufrufen, dann wird nicht gepushed)_
1. scs-poc` bauen
    + macOS: `mvn clean install -DskipTests -Pdocker`
    + Linux: `mvn clean install -DskipTests -Pdocker,socket`
    - _(wenn es Fehler beim docker push gibt, kann man auch `mvn package` aufrufen, dann wird nicht gepushed)_
1. Starten (man braucht einiges an Speicher, um alles lokal laufen zu lassen):
    + Platform starten (consul, ELK, Zipkin): `make platform-restart`
    + Services starten: `make services-restart`
    


