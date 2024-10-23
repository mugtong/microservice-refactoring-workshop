# Day 3. Service to Service
- Amazon SQS를 사용하여 서비스 간의 통신을 구성합니다.
- DevSecOps 파이프라인을 구성하여 보안적으로 안전한 마이크로서비스를 구현하기 위해 실무적으로 필요한 사항을 습득합니다

## DynamoDB에 데이터 적재
Console > DynamoDB
`data.json`의 json object를 하나씩 복사하여 Create Item 실행

## SQS 생성
Console > SQS

## SQS 메시지 전송 확인
`sample-monolith/src/main/java/app/anne/give/application/GiveCommandService.java` 확인

