<h2>After Phase 1a – Java SE (Standard Edition)</h2>

  

Having the following tables: 

  

    create table persons( 
      id integer not null generated always as identity (start with 1 increment by 1), 
      name varchar(100) not null, 
      primary key (id) 
    ); 

    
    create table movies( 
        id integer not null generated always as identity (start with 1 increment by 1), 
        name varchar(100) not null, 
        director_id integer not null references persons on delete restrict, 
        primary key (id) 
    ); 

  

- [x] Create the tables in a postgresql schema 

- [x] Create a class for connecting with the database 

- [x] Create entities/model classes 

- [ ] Create repository classes with CRUD methods 

- [x] Create service classes with CRUD methods 

- [ ] Create tests for repository/service  

  

  

 

 

 

 

  

 

<h2>After Phase 2 – Java core frameworks, libraries and platforms</h2>

  

- [ ] Add to current model classes Actor and Director and integrate them with the Person class. 

- [ ] Using one of the inheritance strategies, create tables and services for them. 

- [ ] Using annotations such @OneToMany, @ManyToOne, model a relationship between movies and actors where a movie can have multiple actors and actors can play in multiple movies. 

- [ ] Implement the methods for retrieving the actors of a movie, the movies an actor plays in and the movies having a specific director in previous services 

- [ ] Create REST endpoints for movies, actors and directors 

- [ ] Try to include some design patterns in your code
