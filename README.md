# Калькулятор отпускных
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=futind_VacationPaymentCalculator&metric=alert_status&token=02827842fcc9e30397708d6e91a4aaa1c43949a1)](https://sonarcloud.io/summary/new_code?id=futind_VacationPaymentCalculator) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=futind_VacationPaymentCalculator&metric=code_smells&token=02827842fcc9e30397708d6e91a4aaa1c43949a1)](https://sonarcloud.io/summary/new_code?id=futind_VacationPaymentCalculator) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=futind_VacationPaymentCalculator&metric=coverage&token=02827842fcc9e30397708d6e91a4aaa1c43949a1)](https://sonarcloud.io/summary/new_code?id=futind_VacationPaymentCalculator) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=futind_VacationPaymentCalculator&metric=sqale_rating&token=02827842fcc9e30397708d6e91a4aaa1c43949a1)](https://sonarcloud.io/summary/new_code?id=futind_VacationPaymentCalculator) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=futind_VacationPaymentCalculator&metric=duplicated_lines_density&token=02827842fcc9e30397708d6e91a4aaa1c43949a1)](https://sonarcloud.io/summary/new_code?id=futind_VacationPaymentCalculator)

Тестовое задание для стажировки в компании Neoflex на направление Java-разработки

## Использованные технологии
Java 17, Spring Boot, Lombok, Docker, GitHub Actions.

## Задание
Приложение принимает среднюю зарплату за 12 месяцев и две даты - начало отпуска и выход из него. В ответ выдаёт сумму отпускных, которые придут сотруднику. Расчёт отпускных проводится с учётом праздников и выходных.

## Запуск
Создание изображения:
```sh
docker build --tag 'vacation-payment-calculator-0.0.1' .
```

Запуск контейнера:
```sh
docker run -d -p 8080:8080 'vacation-payment-calculator-0.0.1'
```