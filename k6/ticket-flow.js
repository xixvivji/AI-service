import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  scenarios: {
    ticket_flow: {
      executor: "ramping-vus",
      startVUs: 1,
      stages: [
        { duration: "30s", target: 10 },
        { duration: "1m", target: 30 },
        { duration: "30s", target: 0 }
      ],
      gracefulRampDown: "10s"
    }
  },
  thresholds: {
    http_req_failed: ["rate<0.05"],
    http_req_duration: ["p(95)<800"]
  }
};

const baseUrl = __ENV.BASE_URL || "http://localhost:8080";

export default function () {
  const statusResponse = http.get(`${baseUrl}/api/v1/system/status`);
  check(statusResponse, {
    "status endpoint returns 200": (res) => res.status === 200
  });

  const ticketPayload = JSON.stringify({
    customerEmail: `loadtest-${__VU}-${__ITER}@example.com`,
    subject: `Load test inquiry ${__ITER}`,
    content: "I need help with a duplicate payment and want a refund update.",
    priority: "HIGH"
  });

  const createResponse = http.post(`${baseUrl}/api/v1/tickets`, ticketPayload, {
    headers: {
      "Content-Type": "application/json"
    }
  });

  check(createResponse, {
    "ticket creation returns 200 or 201": (res) => res.status === 200 || res.status === 201
  });

  const listResponse = http.get(`${baseUrl}/api/v1/tickets`);
  check(listResponse, {
    "ticket list returns 200": (res) => res.status === 200
  });

  sleep(1);
}
