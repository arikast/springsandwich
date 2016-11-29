### What is it

This project makes it easy to annotate security or any other interceptors for your Spring web controllers in much the same way that you currently annotate your routes or endpoints.  I like this better than Spring Security for the folloowing reasons:

- Your security boundaries are easy to see in the controller file itself, just like mvc routes
- Unlike Spring security boundaries, it is type-safe -- if you reorganize your packages, your security still works because it's defined in the file itself
- This is subjective, but IMO for the simple yet common use case of writing code that runs before your controllers, I find this to be much simpler to use and setup.

### How to set it up

1. clone this repo and build and install to your local maven repo:

    git clone git@github.com:arikast/springsandwich.git
    cd springsandwich
    mvn clean install

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
    @ComponentScan(basePackages = {"com.kastkode.springsandwich.filter", "com,your-app-here.*"})
    public class Main { ... }
```

Notice that you also explicitly ComponentScan your Main package or anything else you want scanned, since you're overriding Spring Boot's default scanning.

4. Now the fun part -- use it!  Write your handler that you'd like to invoke before your controller method:
    
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

5. Apply it do your controller as an annotation either at the class or the method (or both):

```
    @Before( @BeforeElement(RestrictByRole.class))
```

6. You can also pass a list of strings for the interceptor to consider.  Here the flags "admin" and "manager" are passed in to the RestrictByRole implementation method

```
    @Before(
        @BeforeElement(value = RestrictByRole.class, flags = {"admin", "manager"})
    )
```

    
7. You can apply several interceptors in sequence like this

```
    @Before({
        @BeforeElement(IPWhiteListCheck.class),
        @BeforeElement(LoginWall.class),
        @BeforeElement(value = RestrictByRole.class, flags = {"admin", "manager"})
    })
```

8. There's also an After interceptor you can use

```
    @After(
        @AfterElement(DoThisAfter.class)
    )
```

9. Many common use cases have already been addressed in premade interceptors found in com/kastkode/springsandwich/filter/coldcuts/.  Consider extending them as a starting point for your interceptor


### FAQ

- How is this different from a servlet filter?
    This has two main advantages over servlet filters. 
    1. it can be directly applied via annotations
    2. your interceptors have full access to the spring context.  For instance, it is difficult to write a standard servlet filter that uses your daos to lookup a user because typically your daos require the spring context, which won't be accessible from a servlet filter.  But in SpringSandwich, you have full access -- write your regular code like anywhere else.


