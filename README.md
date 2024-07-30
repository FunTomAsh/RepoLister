Simple application written in Java using Spring framework which fetches and  lists all GitHub repositories for a given user, excluding forked repositories, and including repository details like branch names and last commit SHA. 
Also, if provided login doesn't exists, exception handler will send an error code and message.

You can use one of the following api (depends on device you are using) to run the application:
```
curl -H "Accept: application/json" http://localhost:8080/api/repos/{username}
```
OR
```
Invoke-WebRequest -Uri http://localhost:8080/api/repos/TomashKarpei -Headers @{"Accept" = "application/json"}
```

If user exists, data will be displayed like written below:
```
[
    {
        "userLogin": "TomashKarpei",
        "repository": [
            {
                "repositoryName": "AlgorithmsForFunctionOptimization",
                "branches": [
                    {
                        "branchName": "main",
                        "lastCommSHA": "8bd425e2c1b9cb0cb7dad0a0f2b127e4a8d96c5d"
                    }
                ]
            }
       
            {
                "repositoryName": "BlurFilter",
                "branches": [
                    {
                        "branchName": "main",
                        "lastCommSHA": "0a0f869631486e70ac1f1957b7801ec0a45b9c0a"
                    }
                ]
            }

        ...
        
       ]
    }
]
```
If user doesn't exists, you will see the following:
```
{
    "status": 404,
    "message": "User not found."
}
```

Important to mention that GitHub has a rate limit and allows to call it only 10 times in 1 hour. If you want to increase limit, you can modify application properties to include your GitHub token which will allow you to make more pull requests.
