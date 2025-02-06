## 조회 성능 개선을 위한 캐싱 및 Redis 활용 방안
### 캐시 (Cache)

- 자주 사용하는 데이터나 결과를 더 빠르게 접근할 수 있도록 저장하는 임시 저장소를 의마합니다.
- DB나 원본 저장소에 직접 접근하는 대신, 더 빠른 메모리(RAM, Redis 등)에 데이터를 저장하여
  조회 속도를 높이고 부하를 줄이는 것으로 일반적으로 데이터베이스 조회, 웹 페이지 로딩 속도 개선,
  API 응답 속도 향상 등에서 사용합니다.

### 캐시 전략
1️⃣ **Read-Through**
[![](https://mermaid.ink/img/pako:eNqFkc1Kw0AUhV9luKsKzQvMoouYrSt3MpshuTaDzUycThZSCi5EhEaoILQWU3ThUgiiGx-n20zewUnivwVnNffc78Dh3AmEKkKgMMbjDGWIgeBDzRMmiXsp10aEIuXSkN2RQGm26DyM8a8c-Ex2amf0BoOWpKS6LG3xXJ-VxK6u7dPjO9UsvYZq8R9YcVXNXkjPvs7t7NYu5sSuL2yR7_xyBj4lvU2Rk2_g4rwBXRpi78t6lXeWwPe25rk7teuHf_NU5bK-WUIfEtQJF5Frb9KYGJgYE2RA3Tfi-ogBk1PH8cyo_RMZAjU6wz5olQ1joId8NHZTlkbcfPT-qboSD5T6mjESRum97ljtzaZv2rqy5w?type=png)](https://mermaid.live/edit#pako:eNqFkc1Kw0AUhV9luKsKzQvMoouYrSt3MpshuTaDzUycThZSCi5EhEaoILQWU3ThUgiiGx-n20zewUnivwVnNffc78Dh3AmEKkKgMMbjDGWIgeBDzRMmiXsp10aEIuXSkN2RQGm26DyM8a8c-Ex2amf0BoOWpKS6LG3xXJ-VxK6u7dPjO9UsvYZq8R9YcVXNXkjPvs7t7NYu5sSuL2yR7_xyBj4lvU2Rk2_g4rwBXRpi78t6lXeWwPe25rk7teuHf_NU5bK-WUIfEtQJF5Frb9KYGJgYE2RA3Tfi-ogBk1PH8cyo_RMZAjU6wz5olQ1joId8NHZTlkbcfPT-qboSD5T6mjESRum97ljtzaZv2rqy5w)
- 요청이 캐시에 없으면 DB에서 조회하고, 그 결과를 캐시에 저장한 후 반환합니다.
- 장점: 자주 조회되는 데이터는 캐시에 쌓여서 빠른 응답 가능
- 단점: 쓰기 작업은 여전히 DB에서 직접 수행해야 합니다.
- 적합한 상황
  - 자주 조회되는 데이터가 있지만, 데이터 변경은 상대적으로 적은 경우
  - 캐시 적중률(Hit Ratio)을 높이고 싶은 경우
- 예시 : 상품 상세정보 조회 (변경이 적은 정보)


2️⃣ **Write-Through**
[![](https://mermaid.ink/img/pako:eNptkLFKA0EQhl9lmUohvsAWKeK1VnayzbA75hZzu-dmt5AQUEgj0UKwiGJEEO2EQ_SpbvMO7t1pEjBTzfx8_wzzT0BaRcBhTOeBjKRM49BhIQxLVaLzWuoSjWeHI03G79BR5vRfzgbCdGpnPOj3W5Kz-raKy6_VrGLx5TI-v7H4eB8_P37hhklsNtgB7sX36zh_YnW1iIur_W1Hs769w9dbH2b16w2Ly7t6_g09KMgVqFV6ddIYBficChLAU6vQnQkQZpo4DN4eXxgJ3LtAPXA2DHPgpzgapymUCv1fSGs1fXxi7WYmpb11R12ybcDTH3p1jkw?type=png)](https://mermaid.live/edit#pako:eNptkLFKA0EQhl9lmUohvsAWKeK1VnayzbA75hZzu-dmt5AQUEgj0UKwiGJEEO2EQ_SpbvMO7t1pEjBTzfx8_wzzT0BaRcBhTOeBjKRM49BhIQxLVaLzWuoSjWeHI03G79BR5vRfzgbCdGpnPOj3W5Kz-raKy6_VrGLx5TI-v7H4eB8_P37hhklsNtgB7sX36zh_YnW1iIur_W1Hs769w9dbH2b16w2Ly7t6_g09KMgVqFV6ddIYBficChLAU6vQnQkQZpo4DN4eXxgJ3LtAPXA2DHPgpzgapymUCv1fSGs1fXxi7WYmpb11R12ybcDTH3p1jkw)
- 데이터를 DB에 저장할 때 동시에 캐시에도 저장합니다.
- 장점: 최신 데이터를 캐시에서도 즉시 사용할 수 있습니다.
- 단점: 모든 쓰기 작업이 캐시에도 저장되므로, 불필요한 데이터까지 캐시에 남을 수 있습니다.
- 적합한 상황
  - 데이터가 자주 변경되지만, 항상 최신 상태를 유지해야 하는 경우
  - 읽기(Read) 속도와 최신 데이터 유지를 둘 다 중요하게 생각하는 경우
- 예시 : 주문 상태 정보, 로그인 상태 유지 등 사용자 세션 데이터

3️⃣ **Write-Back (Write-Behind)**
[![](https://mermaid.ink/img/pako:eNptkc9Kw0AQxl9l2VML7QvsIYeYqydvksuSrM1is1u3m4OUgpUIUhVbqNRKKxUVvQhBFCr4RO74Dm7-WAWd08zHb76Bb3o4kCHDBHfZXsJEwDxOW4rGvkC2OlRpHvAOFRpttDkT-h-dBhH7K3uuL0q1XGw6TkESZM4yWLx8phmC5QFc3yO4msDzUwXnTDOHiy2yZmapuT1FsBibk1dU81wznCC4SOFhgGB6VHkOV1Y7Rmb0WP9t5zieS1DNHF7C3Rimozoyb6k5n32sMlgOYP5ububVIdzAMVMx5aHNpJeb-FhHLGY-JrYNqdr1sS_6lqOJllv7IsBEq4Q1sJJJK8Jkh7a7dko6IdXfaa5VG822lD8zC7mWarN8QfGJ_hfFsqbg?type=png)](https://mermaid.live/edit#pako:eNptkc9Kw0AQxl9l2VML7QvsIYeYqydvksuSrM1is1u3m4OUgpUIUhVbqNRKKxUVvQhBFCr4RO74Dm7-WAWd08zHb76Bb3o4kCHDBHfZXsJEwDxOW4rGvkC2OlRpHvAOFRpttDkT-h-dBhH7K3uuL0q1XGw6TkESZM4yWLx8phmC5QFc3yO4msDzUwXnTDOHiy2yZmapuT1FsBibk1dU81wznCC4SOFhgGB6VHkOV1Y7Rmb0WP9t5zieS1DNHF7C3Rimozoyb6k5n32sMlgOYP5ububVIdzAMVMx5aHNpJeb-FhHLGY-JrYNqdr1sS_6lqOJllv7IsBEq4Q1sJJJK8Jkh7a7dko6IdXfaa5VG822lD8zC7mWarN8QfGJ_hfFsqbg)
- 데이터를 먼저 캐시에 저장하고, 나중에 비동기적으로 DB에 반영합니다.
- 장점: DB 부하 감소, 빠른 쓰기 가능합니다.
- 단점: 캐시에 저장된 데이터가 DB에 반영되기 전에 장애가 나면 데이터 유실 발생합니다.
- 적합한 상황
  - 데이터 쓰기(Write)가 매우 빈번하게 발생하고, DB 부하를 줄이고 싶은 경우
  - 쓰기 속도가 중요한 경우 (데이터 일관성이 약간 희생되어도 됨)
- 예시 : SNS의 좋아요 개수 업데이트

4️⃣ **Write-Around**
[![](https://mermaid.ink/img/pako:eNptUT1OwzAUvor1plZqLpChQ8jKxIa8WI5pLBo7uM6AqkoMgFApUodWLRVBMCCxIEWoA-I4rHHugJsEAgFP9ufvT--NgcqAgQsjdpIwQZnPyUCRCAtkT0yU5pTHRGi0N-RM6H9wQkP2F_Y9LCq0Ejr9vu-5KL_JTLotzjNkHs7M_RMym4V5fUEd8z4313f5dIHM6qJmTd-QWV6hYvncbVmVob_cHrNiM6vdavKO49S5nY_U_pYZZjW3GZcmnXVty1pZSXzPacwbdrt0y79q9bNNnq2L2zX0IGIqIjyw8x3vRBh0yCKGwbXXgKhjDFhMLI8kWh6cCgquVgnrgZLJIAT3iAxH9pXEAdFfm_lG7ZgPpWzeLOBaqv1qneVWJ58Ku8mx?type=png)](https://mermaid.live/edit#pako:eNptUT1OwzAUvor1plZqLpChQ8jKxIa8WI5pLBo7uM6AqkoMgFApUodWLRVBMCCxIEWoA-I4rHHugJsEAgFP9ufvT--NgcqAgQsjdpIwQZnPyUCRCAtkT0yU5pTHRGi0N-RM6H9wQkP2F_Y9LCq0Ejr9vu-5KL_JTLotzjNkHs7M_RMym4V5fUEd8z4313f5dIHM6qJmTd-QWV6hYvncbVmVob_cHrNiM6vdavKO49S5nY_U_pYZZjW3GZcmnXVty1pZSXzPacwbdrt0y79q9bNNnq2L2zX0IGIqIjyw8x3vRBh0yCKGwbXXgKhjDFhMLI8kWh6cCgquVgnrgZLJIAT3iAxH9pXEAdFfm_lG7ZgPpWzeLOBaqv1qneVWJ58Ku8mx)
- 데이터를 DB에만 저장하며 캐시는 건드리지 않고, 해당 데이터가 요청되면 DB에서 조회하여 캐시에 저장합니다.
- 장점: 불필요한 데이터가 캐시에 남는 것을 방지할 수 있습니다.
- 단점: 첫 조회 시 느릴 수 있습니다.
- 적합한 상황
  - 자주 조회되지 않는 데이터라서, 캐시에 굳이 저장할 필요가 없는 경우
  - 읽기보다는 쓰기가 많은 경우 (읽기 요청이 많으면 Read-Through가 더 적합함)
- 예시 : 로그 데이터 저장


## 캐시 스탬피드(Cache Stampede)란?
- 여러 사용자가 동일한 캐시 데이터를 요청했는데, 해당 데이터가 만료되면 모든 요청이 한꺼번에 DB로 쏟아지는 현상을 말합니다.

1️⃣ 다수의 클라이언트가 같은 데이터를 요청 <br>
2️⃣ 데이터가 캐시에 존재하면 빠르게 반환<br>
3️⃣ 캐시 만료(Expire)로 인해 데이터가 캐시에 없음<br>
4️⃣ 모든 요청이 한꺼번에 DB로 이동하여 조회<br>
5️⃣ DB 부하 증가로 인해 성능 저하 또는 서버 다운

### 💡 캐시 스탬피드 대응 방안
1️⃣  **Mutex (Mutual Exclusion) 락**
- 캐시에 데이터가 없을 때, 하나의 요청만 DB에서 조회하도록 락을 걸고 나머지는 대기하도록 합니다.

2️⃣ **Read 요청 병합(Batching Requests)**
- 같은 데이터 요청이 여러 개 들어오면, 하나의 요청으로 합쳐서 처리합니다.
- 예를 들어, 다수의 클라이언트가 같은 데이터를 요청하면 한 번만 DB에서 조회한 후, 결과를 모든 클라이언트에게 반환합니다.

3️⃣ **랜덤 만료 시간(Random Expiry Time) 적용**
- 모든 캐시가 동시에 만료되는 것을 방지하기 위해, 캐시 만료 시간을 일정 범위 내에서 랜덤하게 설정합니다.

4️⃣ **캐시 만료 전에 갱신**
- 캐시가 만료되기 전에 백그라운드에서 미리 데이터를 갱신하여 DB 부하 방지합니다.

5️⃣ **재시도 시간 설정**
- 캐시 미스 발생 시, 요청이 실패하면 재시도 간격을 점점 늘려서(DB 부하를 분산시켜서) 다시 요청