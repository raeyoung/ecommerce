import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 1000 },
        { duration: '20s', target: 2000 },
        { duration: '30s', target: 3000 },
        { duration: '40s', target: 2000 },
        { duration: '50s', target: 1000 },
    ],
}

export default function () {

    const DEFAULT_URL = 'http://localhost:8080';
    const userId = 1;

    const params = {
        headers: { 'Content-Type': 'application/json', }
    }

    const payload = JSON.stringify({
        orderProductList: [
            {
                productId: 1,
                quantity: 1
            }
        ]
    });

    const response = http.post(`${DEFAULT_URL}/api/v1/orders?userId=${userId}`, payload, params);
    check(response, { '주문': (res) => res.status === 200 });

    sleep(1);
}