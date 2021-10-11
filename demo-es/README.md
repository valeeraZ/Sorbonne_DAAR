
# Usage
1. Clone this project
2. Create your own `application.properties` in `src/main/resources`
    ```
        spring.elasticsearch.rest.uris=<ip:9200> # by default is localhost:9200
    ```
   
3. If your Elastic Search is hosted on a remote VPS and not accessible from Internet, you might need to configure SSH connection in this project.
    * append the script to your `application.properties`:
        ```
            ssh.enabled=true
            ssh.host=<ip-vps>
            ssh.port=22
            ssh.username=<username>
            ssh.privatekey=<path/to/private-key>
            ssh.forward.from_host=localhost
            ssh.forward.from_port=9200 # the port of Elastic Search, by default is 9200
            ssh.forward.to_host=localhost
            ssh.forward.to_port=9400
        ```
    * If your private key begins with `BEGIN OPENSSH PRIVATE KEY` which is not supported by JSch, you might need to convert the key to RSA format.
        * copy your private key
            ```
                cp id_rsa id_rsa-new
            ```
        * convert the new private key
            ```
                ssh-keygen -p -f id_rsa-new -m pem 
            ```
        * then modify the path to private key...