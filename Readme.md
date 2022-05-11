## Телеграм бот

Это пример телеграм бота на Java+Spring+Bootstrap.


![cmd](src/main/resources/img/index.png)

Чтобы его запустить, необходимо создать файл настроек
`application.properties` в папке `resources`

В целях безопасности этот файл добавлен в игнор-лист 
индексации, поэтому на каждой локальной машине
необходимо заполнять этот файл вручную.

Общий вид параметров по данным базы

```
spring.datasource.url=jdbc:postgresql://Host:Port/Database
spring.datasource.username=User
spring.datasource.password=Password
spring.datasource.driverClassName=org.postgresql.Driver
```

Например, если данные базы такие:

<table>
  <tr>
    <td>Host</td>  
    <td>test1.compute.amazonaws.com</td>
  </tr>
  <tr>
    <td>Port</td>
<td>5432</td>
  </tr>
  <tr>
    <td>Database</td>  
   <td>dbexqe3d</td>
  </tr>
  <tr>
    <td>User</td>
  <td>bocxxsadfar</td>
  </tr>
  <tr>
    <td>Password</td>
    <td>b12dd116c55986ce49bad37a40605f8833c38f9e0f28</td>
  </tr>
  <tr>  
</tr>
</table>

То файл параметров программы будет таким:

<Tabs>
<TabItem value="application-properties" label="application.properties">

```
spring.datasource.url=jdbc:postgresql://test1.compute.amazonaws.com:5432/dbexqe3d
spring.datasource.username=bocxxsadfar
spring.datasource.password=b12dd116c55986ce49bad37a40605f8833c38f9e0f28
spring.datasource.driverClassName=org.postgresql.Driver
```
</TabItem>
</Tabs>





