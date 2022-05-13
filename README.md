# Diplom_2


В данном проекте при получении ответа от api ручек, извлекаются поля из JSON-ответа,
так же используется десериальзация там, где это удобно.

При прохождении тестов в первую очередь извлекается и проверяется поле "success": из JSON-ответа.

Десериальзация всех ответов невозможна т.к. например, при авторизации ответ кардинально разный
при успешной авторизации и при неверном логине/пароле. 
В случае сбоя, если User успешно авторизуется с неверным логином/паролем, будет ошибкой десериализировать
успешный ответ в класс ошибочного ответа с полями "success" и "message".

Перед запуском тестов, где это необходимо, создается User с рандомным логином, паролем и email-ом.
После прохождения тестов, если пользователь был зарегистрирован, то он удаляется.

В классе UserClient в метод deleteUser(User user, String accessToken) и в классе OrderClient  в метод 
getUserOrder(User user, String accessToken) передается параметр user, чтобы в отчете Allure 
было указано имя удаляемого пользователя.

**Тесты**

Обозначение: - позитивные, * негативные

  Регистрация пользователя

    - корректного рандомного пользователя
    * пользователя с существующим Email

    Параметризованый тест с NULL полями пользователя 
      * пользователь с NULL полем Email
      * пользователь с NULL полем password
      * пользователь с NULL полем name
      * пользователь с NULL полем Email, password
      * пользователь с NULL полем Emaill, name
      * пользователь с NULL полем password, name
      * пользователь с NULL полями Email, password, name

    Параметризованый тест с отсутствующими полями пользователя
      * пользователь без поля Email
      * пользователь без поля password
      * пользователь без поля name
      * пользователь без поля Email, password
      * пользователь без поля Email, name
      * пользователь без поля password, name
      * пользователь без полей Email, password, name


  Авторизация пользователя

    - корректного рандомного пользователя со всеми полями: name, Email, password
    - корректного рандомного пользователя с полями: Email, password
    Параметризованый тест с неверным данными пользователя
      * пользователь с неверным Email
      * пользователь с неверным password
      * пользователь с неверным Email, password
      * пользователя без поля Email
      * пользователя без поля password
      * пользователя без полей Email, password


  Изменение данных пользователя

    Параметризованый тест изменение данных пользователя после авторизации
      - изменение имени пользователяa, после авторизации
      - изменение пароля пользователяa, после авторизации
      - изменение Email пользователя, после авторизации
      - изменение имени и пароля пользователя, после авторизации
      - изменение пароля и Email пользователя, после авторизации
      - изменение имени и Email пользователя, после авторизации
      - изменение всех полей пользователя: name, Email, password, после авторизации

    Параметризованый тест изменение данных пользователя без авторизации
      * изменение имени пользователя, без авторизации
      * изменение пароля пользователяa, без авторизации
      * изменение Email пользователя, без авторизации
      * изменение имени и пароля пользователя, без авторизациии
      * изменение пароля и Email пользователя, без авторизациии
      * изменение имени и Email пользователя, без авторизации
      * изменение всех полей пользователя: name, email, password, без авторизации

    * изменение Email пользователя на Email, который уже используется

  Создание заказа

    Параметризованый тест создание заказа
      - с одним ингридиентом с авторизацией
      - c одним ингридиентом без авторизации
      - с двумя ингридиентами с авторизацией
      - с двумя ингридиентами без авторизации
      - со всеми ингридиентами с авторизацией
      - со всеми ингридиентами без авторизации

    * без ингридиентов с авторизацией
    * с неверным хешем ингредиентов с авторизацией
    * без ингридиентов без авторизации
    * с неверным хешем ингредиентов без авторизации

Получение списка заказов пользователя

    - с авторизацией
    * без авторизации

