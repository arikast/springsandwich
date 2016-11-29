### What is it

This project lets you annotate security or other interceptors for your Spring web controllers in much the same way that you normally annotate your routes or endpoints.  

For example, to secure all the endpoints in your @Controller class, add a @Before annotation like this (you can also add it to individual methods for more granular control):

```
@Before(@BeforeElement( MySecurityFilter.class ))
@Controller
public class MyController { 

    @RequestMapping("/helloworld")
    @ResponseBody
    public String heyworld() {
        return "hey there big n blue";
    }

}
 
```

Then create your implementing class for MySecurityFilter and make sure it's reachable by Spring:

```
@Component
public class MySecurityFilter implements BeforeHandler {
    @Override
    public Flow handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, String[] flags) throws Exception {
        //determine if this request is authorized, using daos or any other mechanism.
        //you have full access to Spring context and autowiring here
        //to halt this request and prevent execution of the controller, return Flow.HALT 
        //you may also redirect to a login page here if desired
        return Flow.CONTINUE;
    }
}
```


I like this better than Spring Security for the folloowing reasons:

- Your security boundaries are easy to see in the controller file itself, just like mvc routes
- Unlike Spring security boundaries, it is type-safe -- if you reorganize your packages, your security still works because it's defined in the file itself
- This is subjective, but IMO for the simple yet common use case of writing code that runs before your controllers, I find this to be much simpler to use and setup.

### How to set it up

1. clone this repo and build and install to your local maven repo:

    ```
    git clone git@github.com:arikast/springsandwich.git
    cd springsandwich
    mvn clean install
    ```

2. include it in your project's pom.xml:

    ```
    <dependencies>
        <dependency>
            <groupId>com.kastkode</groupId>
            <artifactId>springsandwich</artifactId>
            <version>[1.0,)</version>
        </dependency>
    </dependencies>
    ```

3. in your application, tell Spring to pick up the library. For instance in Spring Boot you would add this annotation to your Main starting class:

    ```
    @ComponentScan(basePackages = {"com.kastkode.springsandwich.filter", "com.your-app-here.*"})
    public class Main { ... }
    ```

Notice that here we explicitly ComponentScan our Main package as well since we're overriding Spring Boot's default scanning.


### More details on how to use SpringSandwich

A full implementation for an interceptor might look like this:
    
```
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import com.kastkode.springsandwich.filter.api.BeforeHandler;
import com.kastkode.springsandwich.filter.api.Flow;

@Component
public class RestrictByRole implements BeforeHandler {

    @Override
    public Flow handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, String[] flags) throws Exception {
        System.out.println("RestrictByRole logic executed, checking for these roles");
        if(flags != null) {
            for(String arg:flags) {
                System.out.println(arg);
            }
        }

        //or return Flow.HALT to halt this request and prevent execution of the controller
        //you may also wish to redirect to a login page here
        return Flow.CONTINUE;
    }
}
```

Apply it to your controller as an annotation either at the class or the method (or both):

```
@Before( @BeforeElement(RestrictByRole.class))
```

You can also pass a list of strings for the interceptor to consider.  Here the flags "admin" and "manager" are passed in to the RestrictByRole implementation method

```
@Before(
    @BeforeElement(value = RestrictByRole.class, flags = {"admin", "manager"})
)
```


You can apply several interceptors in sequence like this

```
@Before({
    @BeforeElement(IPWhiteListCheck.class),
    @BeforeElement(LoginWall.class),
    @BeforeElement(value = RestrictByRole.class, flags = {"admin", "manager"})
})
```

There's also an After interceptor you can use

```
@After(
    @AfterElement(DoThisAfter.class)
)
```

Many common use cases have already been addressed in premade interceptors found in com/kastkode/springsandwich/filter/coldcuts/.  Consider extending them as a starting point for your interceptor


### FAQ

- How is this different from a servlet filter?
    This has two main advantages over servlet filters. 
    1. it can be directly applied via annotations, and is thus type-safe and will survive refactorings
    2. your interceptors have full access to the spring context.  For instance, it is difficult to write a standard servlet filter that uses your daos to lookup a user because typically your daos require the spring context, which won't be accessible from a servlet filter.  But in SpringSandwich, you have full access -- write your regular code like anywhere else.


