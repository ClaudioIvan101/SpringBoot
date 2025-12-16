import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 50,
  duration: '5s',
};

export default function () {
  const res = http.post('http://localhost:8080/api/ventas/compra/1');

  const esExito = check(res, { 'status is 200': (r) => r.status === 200 });

  if (!esExito) {
     console.log('Error detectado! Codigo HTTP: ' + res.status);
  }
}