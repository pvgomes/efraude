## Tips

### Create migration on REPL
When you want to create new migration
```clojure
(create-migration "fraude")
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
