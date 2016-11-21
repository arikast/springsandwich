This project makes it easy to annotate security or any other interceptors for your Spring web controllers in much the same way that you currently annotate your routes or endpoints.  I like this better than Spring Security for the folloowing reasons:

- Your security boundaries are easy to see in the controller file itself, just like mvc routes
- Unlike Spring security boundaries, it is type-safe -- if you reorganize your packages, your security still works because it's defined in the file itself
- This is subjective, but IMO for the simple yet common use case of writing code that runs before your controllers, I find this to be much simpler to use and setup.

How do I set it up?

