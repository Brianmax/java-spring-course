#!/usr/bin/env bash
# Comprobación de endpoints REST de cupones-unit-testing (reglas de TICKET-14).
#
# Prerrequisitos:
#   - curl instalado
#   - PostgreSQL en ejecución (docker compose up -d o instancia local con base 'cupones')
#   - Aplicación arrancada (./mvnw spring-boot:run o java -jar target/cupones-unit-testing-1.0.0-SNAPSHOT.jar)
#
# Uso:
#   ./scripts/check_api.sh
#   BASE_URL=http://localhost:8080 ./scripts/check_api.sh
#   ./scripts/check_api.sh http://localhost:8080

set -euo pipefail

BASE_URL="${1:-${BASE_URL:-http://localhost:8080}}"
BASE_URL="${BASE_URL%/}"

if ! command -v curl >/dev/null 2>&1; then
  echo "ERROR: se requiere curl para ejecutar este script." >&2
  exit 1
fi

RUN_ID="$(date +%s)-$$"
ID_200="CHK-200-${RUN_ID}"
ID_400="CHK-400-${RUN_ID}"
ID_100="CHK-100-${RUN_ID}"
ID_80="CHK-80-${RUN_ID}"
ID_999="CHK-999-${RUN_ID}"

TMP_BODY="$(mktemp)"
trap 'rm -f "$TMP_BODY"' EXIT

PASSED=0

assert_request() {
  local name="$1"
  local method="$2"
  local path="$3"
  local expected_status="$4"
  local payload="$5"
  shift 5

  local status
  if [ -n "$payload" ]; then
    status="$(curl -sS -o "$TMP_BODY" -w "%{http_code}" \
      -X "$method" \
      -H "Content-Type: application/json" \
      -d "$payload" \
      "${BASE_URL}${path}")"
  else
    status="$(curl -sS -o "$TMP_BODY" -w "%{http_code}" \
      -X "$method" \
      "${BASE_URL}${path}")"
  fi

  local body
  body="$(cat "$TMP_BODY")"

  if [ "$status" != "$expected_status" ]; then
    echo "FAIL [$name]: esperado HTTP $expected_status, recibido HTTP $status" >&2
    echo "  Respuesta: $body" >&2
    exit 1
  fi

  local expected_substring
  for expected_substring in "$@"; do
    if [ -n "$expected_substring" ] && ! printf '%s' "$body" | grep -Fq "$expected_substring"; then
      echo "FAIL [$name]: la respuesta no contiene '$expected_substring'" >&2
      echo "  Respuesta: $body" >&2
      exit 1
    fi
  done

  PASSED=$((PASSED + 1))
  echo "PASS [$name]: HTTP $status"
}

echo "Verificando API en ${BASE_URL} (lote ${RUN_ID})..."

assert_request \
  "Listar cupones demo incluye DESC20 y VENCIDO10" \
  "GET" "/api/v1/cupones" "200" "" \
  "\"codigo\":\"DESC20\"" "\"codigo\":\"VENCIDO10\""

assert_request \
  "Listar pedidos demo incluye ORD-205 y ORD-207" \
  "GET" "/api/v1/pedidos" "200" "" \
  "\"id\":\"ORD-205\"" "\"id\":\"ORD-207\""

assert_request \
  "Crear pedido de S/ 200 (caso ORD-205)" \
  "POST" "/api/v1/pedidos" "201" \
  "{\"id\":\"${ID_200}\",\"subtotal\":200}" \
  "\"id\":\"${ID_200}\"" "\"totalFinal\":200"

assert_request \
  "Aplicar DESC20 a S/ 200 da descuento 40 y total 160" \
  "POST" "/api/v1/pedidos/${ID_200}/aplicar-cupon" "200" \
  "{\"codigoCupon\":\"DESC20\"}" \
  "\"descuento\":40" "\"totalFinal\":160"

assert_request \
  "Consultar pedido actualizado confirma persistencia en PostgreSQL" \
  "GET" "/api/v1/pedidos/${ID_200}" "200" "" \
  "\"descuentoAplicado\":40" "\"totalFinal\":160" "\"codigoCuponAplicado\":\"DESC20\""

assert_request \
  "Crear pedido de S/ 400 (caso tope ORD-206)" \
  "POST" "/api/v1/pedidos" "201" \
  "{\"id\":\"${ID_400}\",\"subtotal\":400}" \
  "\"id\":\"${ID_400}\""

assert_request \
  "Aplicar DESC20 a S/ 400 respeta el tope de S/ 50" \
  "POST" "/api/v1/pedidos/${ID_400}/aplicar-cupon" "200" \
  "{\"codigoCupon\":\"DESC20\"}" \
  "\"descuento\":50" "\"totalFinal\":350"

assert_request \
  "Crear pedido de S/ 100 exactos (borde TICKET-14 / ORD-207)" \
  "POST" "/api/v1/pedidos" "201" \
  "{\"id\":\"${ID_100}\",\"subtotal\":100}" \
  "\"id\":\"${ID_100}\""

assert_request \
  "Aplicar DESC20 a S/ 100 exactos da descuento 20 y total 80" \
  "POST" "/api/v1/pedidos/${ID_100}/aplicar-cupon" "200" \
  "{\"codigoCupon\":\"DESC20\"}" \
  "\"descuento\":20" "\"totalFinal\":80"

assert_request \
  "Crear pedido de S/ 80 (bajo mínimo ORD-208)" \
  "POST" "/api/v1/pedidos" "201" \
  "{\"id\":\"${ID_80}\",\"subtotal\":80}" \
  "\"id\":\"${ID_80}\""

assert_request \
  "Rechazar DESC20 en pedido de S/ 80 devuelve 409" \
  "POST" "/api/v1/pedidos/${ID_80}/aplicar-cupon" "409" \
  "{\"codigoCupon\":\"DESC20\"}" \
  "\"status\":409" "100"

assert_request \
  "Rechazar cupón inactivo VENCIDO10 devuelve 409" \
  "POST" "/api/v1/pedidos/${ID_200}/aplicar-cupon" "409" \
  "{\"codigoCupon\":\"VENCIDO10\"}" \
  "\"status\":409"

assert_request \
  "Aplicar cupón a pedido inexistente devuelve 404" \
  "POST" "/api/v1/pedidos/${ID_999}/aplicar-cupon" "404" \
  "{\"codigoCupon\":\"DESC20\"}" \
  "\"status\":404"

assert_request \
  "Rechazar pedido con subtotal <= 0 devuelve 400" \
  "POST" "/api/v1/pedidos" "400" \
  "{\"id\":\"CHK-BAD-${RUN_ID}\",\"subtotal\":0}" \
  "\"status\":400"

echo "OK: ${PASSED} comprobaciones superadas contra ${BASE_URL}."
