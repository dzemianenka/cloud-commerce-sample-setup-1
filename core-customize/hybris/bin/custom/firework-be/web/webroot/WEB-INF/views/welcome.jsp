<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Welcome to firework integration plugin</title>
    <style>
    .container {
        margin: 12px auto;
        padding: 20px;
        border-radius: 5px;
        background-color: #f2f2f2;
        box-sizing: border-box;
        max-width: 1200px;
    }

    .container__form {
        display: flex;
        flex-direction: column;
        gap: 12px;
    }

    .container__form label div {
        margin-left: 8px;
    }

    label {
        display: flex;
        color: #333;
        font-weight: bold;
    }

    input[type="text"],
    input[type="email"],
    input[type="checkbox"] {
        width: 100%;
        padding: 10px;
        border-radius: 3px;
        border: 1px solid #ccc;
    }

    button {
        width: fit-content;
        padding: 10px 20px;
        background-color: #4CAF50;
        color: #fff;
        border: none;
        cursor: pointer;
        font-weight: bold;
        border-radius: 3px;
    }

    button:hover {
        background-color: #45a049;
    }
    </style>
    <link rel="stylesheet" href="<c:url value="/static/firework-webapp.css"/>" type="text/css"
          media="screen, projection"/>
</head>
<div class="container">
    <form class="container__form" method="POST" action="/firework/oauth/register">
        <label>
            Client Name: <div>${clientName}</div>
        </label>
        <label>
             Id: <div>${id}</div>
        </label>
        <label>
            Client Id: <div>${clientId}</div>
        </label>

        <label>
            Client Secret: <div>${clientSecret}</div>
        </label>

        <label>
            Business Id: <div>${businessId}</div>
        </label>

        <label>
            Business Name: <div>${businessName}</div>
        </label>

        <label>
            Access Token: <div>${accessToken}</div>
        </label>

        <label>
            Refresh Token: <div>${refreshToken}</div>
        </label>

        <label>
            Store Id: <div>${storeId}</div>
        </label>

        <label>
            Hmac Key: <div>${hmacKey}</div>
        </label>

        <button type="submit">Refresh Registration Data</button>
    </form>
</div>
</html>
