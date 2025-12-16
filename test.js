import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 50,
  duration: '10s',
};

export default function () {
  // APUNTO AL NUEVO ENDPOINT DE PRODUCTOS
  // Asumiendo que el producto creado en init-data tiene ID 1
  const res = http.post('http://localhost:8080/api/products/1/purchase',
    JSON.stringify({}),
    { headers: { 'Content-Type': 'application/json' } }
  );

  // Verificamos éxito (200 OK)
  const esExito = check(res, { 'status is 200': (r) => r.status === 200 });

  if (!esExito) {
  // Solo imprime el primer error para no spammear
     if (__ITER == 0) {
        console.log('Fallo! Status: ' + res.status + ' | Body: ' + res.body);
     }
  }
}
