# Day 3. Service to Service
- Amazon SQS를 사용하여 서비스 간의 통신을 구성합니다.
- DevSecOps 파이프라인을 구성하여 보안적으로 안전한 마이크로서비스를 구현하기 위해 실무적으로 필요한 사항을 습득합니다

## DynamoDB에 데이터 적재
Console > DynamoDB
`data.json`의 json object를 하나씩 복사하여 Create Item 실행

## SQS 생성
Console > SQS

## SQS 메시지 전송 확인
sample-monolith/src/main/java/app/anne/give/application/GiveCommandService.java#L83 확인

```
gradle bootRun
# 나눔 요청 POST /give
curl --location 'http://localhost:8080/give' \
--header 'Content-Type: application/json' \
--data '{
    "itemId": "01G9CS5473GMJTK9SBE2CYK65K",
    "ownerId": "9HG5qPFWcfM2U2gvCskTmBCo5QF3",
    "requesterId": "9NFzMZ0Lu9PmlkdlKensbSnTklE2"
}'

# 나눔 수락 PUT /give/accept
curl --location --request PUT 'http://localhost:8080/give/accept' \
--header 'Content-Type: application/json' \
--data '{
    "giveId": "01JAVC3WEMY160JKZCYPWN3J41",
    "ownerId": "9HG5qPFWcfM2U2gvCskTmBCo5QF3"
}'
```

## SQS 메시지 삭제 및 DynamoDB 데이터 삭제
같은 요청의 중복 수행을 위해 생성된 데이터와 메시지를 삭제해줍니다.

## item-service 코드 수정
SQS로부터 메시지를 수신하여 다음 작업을 수행
- 기존 row의 status 변경
- ~~새로운 row의 생성~~
- 데이터 원복

## item-service 배포
- Docker 이미지 빌드
  ```
  cd ~/environment/microservice-refactoring-workshop/item-service
  docker build -t item-service:v1 .

  cd ~/environment/microservice-refactoring-workshop/monolith
  docker build -t anne-api:v1 .
  ```
- docker run 커맨드로 로컬 환경에서 테스트
  ```
  docker run -dt -p 8080:8080 <account_id>.dkr.ecr.us-west-2.amazonaws.com/anne-api:v1

  # 나눔 요청 POST /give
  curl --location 'http://localhost:8080/give' \
  --header 'Content-Type: application/json' \
  --data '{
      "itemId": "01G9CS5473GMJTK9SBE2CYK65K",
      "ownerId": "9HG5qPFWcfM2U2gvCskTmBCo5QF3",
      "requesterId": "9NFzMZ0Lu9PmlkdlKensbSnTklE2"
  }'
    
  # 나눔 수락 PUT /give/accept
  curl --location --request PUT 'http://localhost:8080/give/accept' \
  --header 'Content-Type: application/json' \
  --data '{
      "giveId": "01JAVC3WEMY160JKZCYPWN3J41",
      "ownerId": "9HG5qPFWcfM2U2gvCskTmBCo5QF3"
  }'

  docker run -dt -p 8082:8080 <account_id>.dkr.ecr.us-west-2.amazonaws.com/item-service:v1
  ```
- 데이터 원복
- Docker 이미지 푸시
- Helm Chart 수정
- EC2 Instance Role에 `AmazonSQSFullAccess` 정책 부여
- ArgoCD Sync로 재배포
- 로컬환경의 포트 8080 사용 중 여부 확인
- Port-forwarding으로 작동 확인

## 나눔 요청 재생성
DynamoDB 테이블에서 다음 작업의 수행을 확인
- 기존 row의 status 변경
- ~~새로운 row의 생성~~
- 데이터 원복





