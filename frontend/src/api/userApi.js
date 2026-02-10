const BASE = '/api/users'

export async function fetchUsers() {
  const res = await fetch(BASE)
  return res.json()
}

export async function fetchUserDetail(id) {
  const res = await fetch(`${BASE}/${id}/detail`)
  return res.json()
}

export async function createUser(data) {
  const res = await fetch(BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  return res.json()
}

export async function updateUser(id, data) {
  const res = await fetch(`${BASE}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  return res.json()
}

export async function deleteUser(id) {
  await fetch(`${BASE}/${id}`, { method: 'DELETE' })
}
