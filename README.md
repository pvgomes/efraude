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
```docker-compose up -d```

### Create fraude db and test db
```
docker exec -it db mysql -uroot -pefraude190 --execute="CREATE DATABASE fraude;"
docker exec -it db mysql -uroot -pefraude190 --execute="CREATE DATABASE fraude_test;"
```

### Download dependencias, migrate and start web server 
```
lein deps
lein run migrate
lein run
```

### or Run using REPL
```
(user/start)
```

### Access
access [localhost:3000](http://localhost:3000)

## before commit
- run tests: `lein test`
- run lint-fix: `lein lint-fix`

### For more
Documentation and tips for development se [here](resources/docs/docs.md)