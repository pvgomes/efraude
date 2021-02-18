# fraude
fraude application, this project uses clojure as a server language and Mysql as a database, to make all things easy we use Luminus framework with Selmer as a template engine and [Bulma](https://bulma.io/) as a css

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run 

or just run lein into your IDE

## Copy dev-config.edn.dist (change something inside if you need like db connection)
`cp dev-config.edn.dist dev-config.edn`

### Start Mysql
```docker-compose up```

### Create fraude db
```docker exec -it fraude_db_1 mysql -uroot -ptoeco190 --execute="CREATE DATABASE fraude;"```

### Start web server
```clojure
(start)
```

### Create migration
```clojure
(create-migration "fraude")
```

### Run migration
```clojure
(migrate)
```


### Update queries
```clojure
(in-ns 'fraude.db.core)
(conman/bind-connection *db* "sql/queries.sql" "sql/person.sql" "sql/clone.sql")
```

### REPL test user creation/login
```clojure
(in-ns 'fraude.controllers.person)
;should return 1
(create! "joao" "jsilva@gmail.com" "123")
;should return a array map with person data
(authenticate-person "jsilva@gmail.com" "123")
```

### REPL test fraud creation
```clojure
(in-ns 'fraude.controllers.fraud)
(save! {:name "vencendo" :message "cuidado" :fk_person "1"})
```

### test
`lein test`


### Deploy flow

generate jar
`lein uberjar`


sync .jar 
scp ./target/uberjar/fraude.jar root@104.236.125.208:/home/pvgomes/


Process
```
systemctl status efraude
systemctl stop efraude
mv /var/www/efraude/fraude.jar /var/www/efraude/last-release/
cp /home/pvgomes/fraude.jar /var/www/efraude/
systemctl start efraude
systemctl status efraude
```




export env vars
`export DATABASE_URL="jdbc:mysql://root:toeco190@0.0.0.0:3306/fraude"`

run
`java -jar target/uberjar/fraude.jar`



java -version
openjdk version "1.8.0_232"






google secret:
6s2Wn_rkem5wLV0IZBoeqObA