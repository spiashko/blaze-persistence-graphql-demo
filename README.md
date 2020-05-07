Blaze-Persistence Examples Spring Data GraphQL
==========
This is an GraphQL sample application showcasing how the Blaze-Persistence GraphQL
integration can be used to develop GraphQL interfaces with ease. 

## How to use it?

* run `doxker-compose up -d` (will not work on windows https://github.com/docker-library/postgres/issues/435)
* run `mvn spring-boot:run` 
* navigate to http://localhost:8080/graphiql where you can run your GraphQL queries.

notes:

You will see that the queries exposed there use the GraphQL Relay spec to implement pagination through cursors.
You can take a look at the generated queries to further understand what happens or you take a look into the [documentation](https://persistence.blazebit.com/documentation/core/manual/en_US/index.html#anchor-keyset-pagination) for more information on the topic.

Another thing that you will see from the generated queries is that the GraphQL selection list actually alters what is selected in the SQL query!

## Request Examples
1
```
{
  catById(id: 1) {
    id
    name
  }
}
```
Query:`["select cat0_.id as col_0_0_, cat0_.name as col_1_0_, null as col_2_0_, null as col_3_0_ from cat cat0_ where cat0_.id=?"]`

Params:`[(1)]`

2
```
{
  catById(id: 1) {
    id
    name
    owner {
      id
      name
    }
  }
}
```
Query:`["select cat0_.id as col_0_0_, cat0_.name as col_1_0_, cat0_.fk_owner as col_2_0_, person1_.name as col_3_0_ from cat cat0_ inner join person person1_ on cat0_.fk_owner=person1_.id where cat0_.id=?"]`

Params:`[(1)]`

3
```
{
  findAll(first: 1) {
    edges {
      node {
        id
        name
      }
    }
  }
}
```
Query:`["select cat0_.id as col_0_0_, cat0_.name as col_1_0_, null as col_2_0_, null as col_3_0_ from cat cat0_ order by col_0_0_ ASC limit ?"]`

Params:`[(1)]`

4
```
{
  findAll(first: 1) {
    edges {
      node {
        id
        name
        owner {
          id
          name
        }
      }
    }
  }
}
```
Query:`["select cat0_.id as col_0_0_, cat0_.name as col_1_0_, cat0_.fk_owner as col_2_0_, person1_.name as col_3_0_ from cat cat0_ inner join person person1_ on cat0_.fk_owner=person1_.id order by col_0_0_ ASC limit ?"]`

Params:`[(1)]`


## Note
This repo just extend a bit original from https://github.com/Blazebit/blaze-persistence/tree/master/examples/spring-data-graphql