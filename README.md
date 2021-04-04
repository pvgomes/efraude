# fraude
efraude.app is a web application, this project uses clojure as a server language and Mysql as a database, to make all things easy we use Luminus framework with Selmer as a template engine and [Bulma](https://bulma.io/) as a css

[![Build Status](https://travis-ci.org/pvgomes/efraude.svg?branch=main)](https://travis-ci.org/pvgomes/efraude)

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run 

or just run lein into your IDE

## Copy dev-config.edn.dist 
it uses docker configuration, you can change it if you want
```
cp dev-config.edn.dist dev-config.edn
cp test-config.edn.dist test-config.edn
```

### Start Mysql
```docker-compose up```

### Create fraude db and test db
```
docker exec -it fraude_db_1 mysql -uroot -pefraude190 --execute="CREATE DATABASE fraude;"
docker exec -it fraude_db_1 mysql -uroot -pefraude190 --execute="CREATE DATABASE fraude_test;"
```

### Start web server on REPL
```clojure
(start)
```

### Run migration on REPL
```clojure
(migrate)
```

### Access
access [localhost:3000](http://localhost:3000)


### test
`lein test`


### For more
Documentation and tips for development se [here](resources/docs/docs.md)