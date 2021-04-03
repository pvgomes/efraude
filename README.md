# fraude
fraude application, this project uses clojure as a server language and Mysql as a database, to make all things easy we use Luminus framework with Selmer as a template engine and [Bulma](https://bulma.io/) as a css

[![Build Status](https://travis-ci.com/pvgomes/efraude.svg?token=Ha16hcqsZpMxYmV2Szvm&branch=main)](https://travis-ci.com/pvgomes/efraude)

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run 

or just run lein into your IDE

## Copy dev-config.edn.dist (it uses docker configuration, you can change it if you want)
`cp dev-config.edn.dist dev-config.edn`

## Copy test-config.edn.dist (it uses docker configuration, you can change it if you want)
`cp test-config.edn.dist test-config.edn`

### Start Mysql
```docker-compose up```

### Create fraude db and test db
```docker exec -it fraude_db_1 mysql -uroot -pefraude190 --execute="CREATE DATABASE fraude;"```
```docker exec -it fraude_db_1 mysql -uroot -pefraude190 --execute="CREATE DATABASE fraude_test;"```

### Start web server on REPL
```clojure
(start)
```

### Create migration on REPL
```clojure
(create-migration "fraude")
```

### Run migration on REPL
```clojure
(migrate)
```

### Update queries
when you update .sql files
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
