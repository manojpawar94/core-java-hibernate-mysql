# core-java-hibernate-mysql

It is custome Hibernate Repository Implementation using java generic which basically provides default methods to persist entity object using DAO object.

The implementation allows the user to either initialize sessions outside of DAO also if the user doesn't initialize the session externally it will create an internal DAO session. It also takes care of the closing of an internal session with transaction management itself.
