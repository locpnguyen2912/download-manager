# Download Manager  
This Download Manager to allow user to interact with FileServer via CLI to view, download file 

## Features implemented
* Download Manager operations supported
  - list out all the available files on server
  - download single/multiple file in parallel
  - show download progress bar
  - notification when download completed
  
## Runtime requirements
* [JDK 11](https://jdk.java.net/11/) or later (JDK 8 is supported as well)

### Build and Run
```
mvn clean install
java -jar target/download-manager-1.0-SNAPSHOT.jar
```


