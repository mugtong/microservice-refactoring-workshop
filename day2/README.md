# Day 2. Mircoservice 분리
- 마이크로서비스 기능 구현을 완료하고 마이크로서비스 간의 데이터 동기화를 적용합니다. 프론트엔드를 분리합니다.

## 예제 코드 클론
```
cd ~/environment
git clone https://github.com/seanlee10/container-expert-workshop.git
```

## Nginx 설치 - Manifest
```
cd ~/environment/container-expert-workshop
cd 02_kubernetes/04_nginx
# Nginx 설치
kubectl apply -f nginx.yaml

# Port Forward 설정
k9s or kubectl port-forward

# Nginx 기동 확인
curl -I localhost:8080

# Nginx 삭제
kubectl delete -f nginx.yaml
```

## Nginx 설치 - Helm Chart
```
helm template nginx .
helm install nginx .
```

## 예제 코드 이미지 빌드
```
cd ~/environment/microservice-refactoring-workshop/sample-monolith
docker build -t anne-api .
```

## 이미지 등록
Console - ECR Registry 생성
```
docker push
```

## GitOps
Helm Chart 생성 (port 8080, image_uri, tag)
git push (/gitops-repos/workload/anne-api)
		 로컬 git이 중단되었다면 https://github.com/seanlee10/eks-blueprints-workshop-gitops-apps.git

## ArgoCD 
Application 등록

## DynamoDB 테이블 생성
cd ~/environment/workshop
terraform apply

## 인스턴스 DynamoDB Permission 추가
Console > IAM > NodeRole (e.g. example-eks-node-group-20241021202745870700000001  Info)
> Attach permission > AmazonDynamoDBFullAccess

## 개발 환경 설정
- Corretto 17
  sudo yum install java-17-amazon-corretto-devel
- Gradle 8
  curl -s "https://get.sdkman.io" | bash
  source "/home/ec2-user/.sdkman/bin/sdkman-init.sh"
  sdk install gradle
- Anne API:
  cd ~/environment/microservice-refactoring-workshop/sample-monolith
  gradle bootRun
- curl localhost:8080/owner/9HG5qPFWcfM2U2gvCskTmBCo5QF3/items