import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 50,
  duration: '5s',
};

export default function () {
  // AQUI ESTA LA CORRECCION: Agregamos /ventas a la ruta
  const res = http.post('http://localhost:8080/api/ventas/compra/1');
  
  const esExito = check(res, { 'status is 200': (r) => r.status === 200 });

  if (!esExito) {
     if (__ITER == 0) {
        console.log('Error! Codigo: ' + res.status + ' | URL usada: ' + res.url);
     }
  }
}
