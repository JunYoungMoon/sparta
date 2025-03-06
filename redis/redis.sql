-- String
-- <명령어> <key> ~~
SET user:email alex@gmail.com
GET user:email

-- 정수가 문자열로 저장된 경우
-- INCR, DECR: ++,--
SET user:count 1
GET user:count
INCR user:board-2
GET user:count
DECR user:count

--MSET, MGET
MSET user:name alex user:email sdfsd@naver.com
MGET user:name user:email

-- List: Linked List
-- 스택, 큐
-- Push, Pop -> 왼쪽(L)이냐 오른쪽(R)이냐
LPUSH user:list alex
LPUSH user:list brad
RPUSH user:list chad
RPUSH user:list dave
LPOP user:list
RPOP user:list

-- list는 Worker Queue : 여러 Worker Application에게 일을 분배하기 위해 사용
-- Timeline : 구 트위터의 타임라인
-- 리스트의 크기
LLEN user:list
-- 범위내의 데이터를 가져오기
-- 크기보다 더큰 숫자는 상관없이 리스트 전체가 나오고 끝
LRANGE user:list 0 3
LRANGE user:list 0 100000
-- 음수 index 뒤에서 부터 읽는 요소
LRANGE user:list 0 -1
LRANGE user:list 0 -2
-- end < start 빈리스트가 들어오게 된다.
LRANGE user:list 1 0

-- 레디스 명령어는 자료형에 따라 다르게 사용된다. 아래는 사용할수 없음
-- Key에 저장된 자료형이 다른경우 오류가 발생한다.
-- GET user:list
-- LPOP user:name

-- Set : (문자열의) 집합
-- 중복을 허용하지 않는다.
SADD user:java alex
SADD user:java brad
SADD user:java chad
SREM user:java alex

SISMEMBER user:java brad
SISMEMBER user:java dave
-- 집합의 리스트
SMEMBERS user:java
-- 집합의 크기(cardinality)
SCARD user:java

-- 교집합 합집합
SADD user:python alex
SADD user:python brad
SADD user:java alex

-- 교집합
SINTER user:java user:python
-- 합집합
SUNION  user:java user:python
-- 교집합의 원소 갯수 앞에 2개가 들어갈것이다 라는걸 명시한다 user:java,user:python
-- 중복을 허용하지 않는 방문자수 세기
-- URL을 키로 만들고 사용자 ID를 넣어준다.
-- JWT 인증 토큰 블랙리스트(원래는 클라이언트가 파기해야함)
-- SISMEMBER는 : O(1)
SINTERCARD 2 user:java user:python

-- Hash
-- Filed - Value Pair
HSET user:alex name alex age 25
HSET user:alex major CSE

-- 키가 단하나의 객체로 사용되길 권장이 된다.
-- user 안에 또 user의 id 필드가 있고 value 에 json으로 관리하는건 글쎄..
HGET user:alex name
HGET user:alex age
HMGET user:alex age major
HGETALL user:alex

HKEYS user:alex
HLEN user:alex

-- 장바구니, 세션정보
HSET cart:alex computer 1 mouse 2 keyboard 10

-- Sorted Set
-- 정렬된 집합 : 중복되지 않는 데이터 + 점수
-- ZADD key score value
ZADD user:ranks 10 alex
ZADD user:ranks 9 brad 11 chad
ZADD user:ranks 8 dave 9.76 eric
ZINCRBY user:ranks 2 alex

ZRANK user:ranks alex
ZRANK user:ranks eric
ZRANK user:ranks dave

ZREVRANK user:ranks alex
ZRANGE user:ranks 0 3
ZREVRANGE user:ranks 0 3
-- 이미 있는 alex는 값을 update함
ZADD user:ranks 10 alex
-- 순위표(리더보드), timestamp를 점수로 쓰면 Rate Limiter를 사용가능

-- redis 자료구조 구애 받지 않고 공통적으로 사용할수 있는것
-- DEL : key 삭제하기
SET somekey "to be deleted"
DEL somekey
DEL user:java
DEL cart:alex

-- EXPIRE : 만료시간 설정
SET expirekey "to be expired"
EXPIRE expirekey 50
EXPIRE user:alex 5
--EXPIRETIME : 만료될 시간을 UNIX Timestamp로 반환한다.
EXPIRETIME expirekey

-- KEYS : Key를 검색한다.
KEYS *
KEYS user*

-- FLUSHDB : 모든 키를 다 제거한다.
FLUSHDB

